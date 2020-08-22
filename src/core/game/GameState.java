package core.game;

import core.action.Action;
import core.action.None;
import core.action.Train;
import core.entity.*;
import player.PlayerAction;
import util.Vector2d;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static core.Constants.TIME_PER_TICK;

public class GameState {

    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    /**
     * @see Grid
     */
    private Grid grid;
    private int ticks;

    // UnitId <-> unit actions
    private Map<Long, Action> unitActions;
    // PlayerId <-> build actions
    private Map<Integer, Queue<Action>> buildActions;
    // PlayerId <-> resource storage
    private Map<Integer, Integer> playerResource;

    private GameState() {
    }

    GameState(Grid grid) {
        this.grid = grid;
        unitActions = new HashMap<>();
        buildActions = new HashMap<>();
        playerResource = new HashMap<>();
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * @return the winner id or -1 if game not over
     */
    int gameOver() {
        Set<Integer> playerSet = grid.entities().stream()
                .filter(e -> !(e instanceof Resource))
                .mapToInt(Entity::getAgentId)
                .boxed()
                .collect(Collectors.toSet());
        if (playerSet.size() == 1) {
            return playerSet.iterator().next();
        }
        return -1;
    }

    void tick(double elapsed) {
        // Remove died entities first
        for (Iterator<Entity> it = grid.entities().iterator(); it.hasNext(); ) {
            Entity e = it.next();
            if (e.died()) {
                it.remove();
                grid.removeEntity(e);
            }
        }

        // Reload unit before action perform
        for (Unit u : grid.units()) {
            u.reload(elapsed);
        }

        // Execute build actions
        for (Queue<Action> next : buildActions.values()) {
            Action build = next.peek();
            if (build != null) {
                if (build.isComplete()) {
                    next.poll();
                } else {
                    build.exec(this, elapsed);
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
     * Creates a deep copy of this game state
     *
     * @return a copy of this game state.
     */
    public GameState copy() {
        GameState copy = new GameState();

        copy.grid = grid.copy();
        copy.ticks = ticks;

        // Copy unit actions
        copy.unitActions = new HashMap<>();
        for (Map.Entry<Long, Action> entry : unitActions.entrySet()) {
            copy.unitActions.put(entry.getKey(), entry.getValue().copy());
        }

        // Copy build actions
        copy.buildActions = new HashMap<>();
        for (Map.Entry<Integer, Queue<Action>> entry : buildActions.entrySet()) {
            Queue<Action> copyActions = new LinkedList<>();
            for (Action action : entry.getValue()) {
                copyActions.add(action.copy());
            }
            copy.buildActions.put(entry.getKey(), copyActions);
        }

        copy.playerResource = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : playerResource.entrySet()) {
            copy.playerResource.put(entry.getKey(), entry.getValue());
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
    public void handleResource(int playerId, int amount) {
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

    /**
     * Only entry point for adding a building
     *
     * @return if adding is successful
     */
    public boolean addBuilding(Building b) {
        if (b == null) {
            return false;
        }
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
        grid.updateScreenPos(u, spawn);
        grid.addUnit(u);
    }

    /**
     * Advance current game state with given actions
     */
    public void advance(List<PlayerAction> pas) {
        pas.forEach(this::addPlayerAction);
        tick(TIME_PER_TICK);
    }

    /**
     * Update a player's action to the game state
     *
     * @param pa {@link PlayerAction}
     */
    public synchronized void addPlayerAction(PlayerAction pa) {
        int playerId = pa.playerId();

        // Handle build actions
        Queue<Action> buildActs = buildActions.get(playerId);
        if (buildActs == null) {
            buildActions.put(playerId, new LinkedList<>(pa.buildActions()));
        } else {
            buildActs.addAll(pa.buildActions());
        }

        // Handle unit actions
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

    public ArrayList<Action> calBuildActions(int playerId) {
        ArrayList<Action> acts = new ArrayList<>();

        int resource = getResource(playerId);
        EntityFactory ef = EntityFactory.getInstance();
        ef.unitTable.forEach((k, v) -> {
            if (resource >= v.getCost()) {
                acts.add(new Train(ef.train(k, playerId)));
            } else {
                acts.add(new None());
            }
        });
        return acts;
    }

}
