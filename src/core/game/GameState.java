package core.game;

import core.Constants;
import core.action.Action;
import core.action.Train;
import core.entity.*;
import player.PlayerAction;
import player.PlayerActionFactory;
import util.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

import static core.Constants.NANO_PER_TICK;

public class GameState {

    /**
     * @see Grid
     */
    private Grid grid;
    private int ticks;

    // UnitId <-> unit actions
    private Map<Long, Action> unitActions;
    // PlayerId <-> build actions
    private Map<Integer, Train> trainActions;
    // PlayerId <-> resource storage
    private Map<Integer, Integer> playerResource;

    private boolean isCopy = false;
    private Game.GameMode gm;

    private GameState() {
    }

    GameState(Grid grid, Game.GameMode gm) {
        this.grid = grid;
        this.gm = gm;
        unitActions = new HashMap<>();
        trainActions = new HashMap<>();
        playerResource = new HashMap<>();
    }

    public Grid grid() {
        return grid;
    }

    public boolean gameOver() {
        return winnerId() != -1;
    }

    /**
     * @return the winner id or -1 if game not over
     */
    int winnerId() {
        int winnerId = -1;
        Set<Integer> playerSet = grid.entities().stream()
                .filter(e -> !(e instanceof Resource))
                .mapToInt(Entity::getPlayerId)
                .boxed()
                .collect(Collectors.toSet());
        boolean end = playerSet.size() == 1;

        switch (gm) {
            case Annihilation -> {
                if (end) winnerId = playerSet.iterator().next();
            }
            case Survivor -> {
                if (end) winnerId = playerSet.iterator().next();
                if (ticks >= Constants.GAME_MAX_TICKS) {
                    Optional<Map.Entry<Integer, Integer>> opEntry =
                            playerResource.entrySet().stream().max(Map.Entry.comparingByValue());
                    return opEntry.get().getKey();
                }
            }
        }
        return winnerId;
    }

    void tick(double elapsed) {
        clearDied();

        reloadUnits(elapsed);

        // Execute train actions
        for (Iterator<Train> it = trainActions.values().iterator(); it.hasNext(); ) {
            Action next = it.next();
            if (next != null) {
                if (next.isComplete()) {
                    it.remove();
                } else {
                    next.exec(this, elapsed);
                }
            }
        }

        // Execute unit actions
        for (Iterator<Action> it = unitActions.values().iterator(); it.hasNext(); ) {
            Action next = it.next();
            if (next.isComplete()) {
                it.remove();
            } else {
                next.exec(this, elapsed);
            }
        }

        ticks++;
    }

    /**
     * Remove died entities from grid
     */
    private void clearDied() {
        for (Iterator<Entity> it = grid.entities().iterator(); it.hasNext(); ) {
            Entity e = it.next();
            if (e.died()) {
                it.remove();
                grid.removeEntity(e);
            }
        }
    }

    /**
     * Reload units
     */
    private void reloadUnits(double elapsed) {
        grid.units().forEach(u -> u.reload(elapsed));
    }

    /**
     * Creates a deep copy of this game state
     *
     * @return a copy of this game state.
     */
    public GameState copy() {
        GameState copy = new GameState();
        copy.isCopy = true;
        copy.gm = gm;
        copy.grid = grid.copy();
        copy.ticks = ticks;

        // Copy unit actions
        copy.unitActions = new HashMap<>();
        for (Map.Entry<Long, Action> entry : unitActions.entrySet()) {
            copy.unitActions.put(entry.getKey(), entry.getValue().copy());
        }

        // Copy build actions
        copy.trainActions = new HashMap<>();
        for (Map.Entry<Integer, Train> entry : trainActions.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            copy.trainActions.put(entry.getKey(), (Train) entry.getValue().copy());
        }

        copy.playerResource = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : playerResource.entrySet()) {
            copy.playerResource.put(entry.getKey(), Integer.valueOf(entry.getValue()));
        }

        return copy;
    }

    public int getResource(int playerId) {
        return playerResource.getOrDefault(playerId, 0);
    }

    /**
     * Add or remove resource for a player
     *
     * @param playerId player id
     * @param amount   positive for addition, negative for remove
     */
    public void changeResource(int playerId, int amount) {
        playerResource.merge(playerId, amount, Integer::sum);
    }

    /**
     * Check if player has enough resource
     *
     * @param playerId player id
     * @param amount   needed
     * @return true if has enough
     */
    public boolean checkResource(int playerId, int amount) {
        if (amount == 0) {
            return false;
        }
        return playerResource.getOrDefault(playerId, 0) >= amount;
    }

    private void addEntity(Entity e, boolean isCopy) {
        if (!isCopy) {
            e.setEntityId(Entity.nextId++);
        }
    }

    /**
     * Only entry point for adding a building
     *
     * @return if adding is successful
     */
    public boolean addBuilding(Building b) {
        if (b == null) {
            return false;
        }
        addEntity(b, isCopy);
        return grid.addBuilding(b);
    }

    /**
     * Only entry point for adding a unit
     *
     * @param u         {@link Unit} to add
     * @param sourcePos location where the unit was build
     */
    public void addUnit(Unit u, Vector2d sourcePos) {
        if (u == null) {
            return;
        }
        Vector2d spawn = grid.findFirstNearby(sourcePos, 5);
        if (spawn == null) {
            // TODO give indication to player
            return;
        }
        addEntity(u, isCopy);
        grid.updateScreenPos(u, spawn);
        grid.addUnit(u);
    }

    /**
     * Advance current game state with given actions
     */
    public void advance(PlayerAction... pas) {
        for (PlayerAction pa : pas) {
            assign(pa);
        }
        tick(NANO_PER_TICK);
    }

    /**
     * Add a list of actions to the game state
     *
     * @param pa {@link PlayerAction}
     */
    public synchronized void assign(PlayerAction pa) {
        if (pa == null) {
            // Should not be here
            System.err.println("A null action being assigned to game state");
            return;
        }

        int playerId = pa.playerId();
        // Add train action
        Action current = trainActions.get(playerId);
        if (current == null) {
            trainActions.put(playerId, pa.trainAction());
        }

        // Add unit actions
        for (Map.Entry<Long, Action> entry : pa.unitActions().entrySet()) {
            unitActions.put(entry.getKey(), entry.getValue());
        }
    }

    public Action getUnitAction(long uId) {
        return unitActions.get(uId);
    }

    public int getTicks() {
        return ticks;
    }

    public ArrayList<Action> getAllAvailableTrainActions(int playerId) {
        ArrayList<Action> acts = new ArrayList<>();

        int resource = getResource(playerId);
        EntityFactory ef = EntityFactory.getInstance();
//        if (resource < ef.maxCost()) {
//            return acts;
//        }
        ef.unitTable.forEach((k, v) -> {
            if (resource >= v.getCost()) {
                acts.add(new Train(ef.getUnit(k, playerId)));
            }
        });
        return acts;
    }

    /**
     * @return total number of game objects
     */
    public int objectsCount() {
        return grid.entities().size();
    }

    public List<PlayerAction> sample(int playerId, int nActions, Random rnd) {
        PlayerActionFactory factory = new PlayerActionFactory(this, playerId);
        List<PlayerAction> actions = new ArrayList<>();
        while (actions.size() < nActions) {
            PlayerAction temp = factory.randomBiasedAction(rnd);
            if (temp.empty()) {
                break;
            }
            actions.add(temp);
        }

        if (actions.isEmpty()) {
            actions.add(new PlayerAction(playerId));
        }

        return actions;
    }
}
