package core.game;

import core.action.Action;
import core.entity.Building;
import core.entity.Entity;
import core.entity.Unit;
import player.PlayerAction;
import util.Vector2d;

import java.util.*;
import java.util.logging.Logger;

import static core.Constants.GRID_SIZE;
import static core.Constants.TIME_PER_TICK;

public class GameState {

    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    // The game grid
    private Grid grid;

    // UnitId <-> unit actions
    private Map<Long, Action> unitActions;
    // PlayerId <-> build actions
    private Map<Integer, Queue<Action>> buildActions;
    // PlayerId <-> resource storage
    private Map<Integer, Integer> resources;

    GameState() {
        grid = new Grid(GRID_SIZE);

        unitActions = new HashMap<>();
        buildActions = new HashMap<>();
        resources = new HashMap<>();
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean gameOver() {
        return false;
    }

    public void tick() {
        for (Iterator<Entity> it = grid.entities().iterator(); it.hasNext(); ) {
            Entity e = it.next();
            if (e.died()) {
                it.remove();
                grid.removeEntity(e);
            }
        }
        // Reload unit every tick
        for (Unit u : grid.unitMap().values()) {
            u.reload(-TIME_PER_TICK);
        }

        // Execute build actions
        for (Queue<Action> next : buildActions.values()) {
            Action build = next.peek();
            if (build != null) {
                if (build.isComplete()) {
                    next.poll();
                } else {
                    build.exec(this, TIME_PER_TICK);
                }
            }
        }

        // Execute unit actions
        for (Iterator<Action> it = unitActions.values().iterator(); it.hasNext(); ) {
            Action next = it.next();
            if (next.isComplete()) {
                it.remove();
            } else {
                next.exec(this, TIME_PER_TICK);
            }
        }

    }

    /**
     * Creates a deep copy of this game state
     *
     * @return a copy of this game state.
     */
    public GameState copy() {
        GameState copy = new GameState();

        copy.grid = this.grid.copy();

        copy.unitActions = new HashMap<>();
        copy.buildActions = new HashMap<>();
        copy.resources = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : resources.entrySet()) {
            copy.resources.put(entry.getKey(), entry.getValue());
        }

        return copy;
    }

    public void manageResource(int playerId, int amount) {
        resources.merge(playerId, amount, Integer::sum);
    }

    public boolean enoughResource(int playerId, int amount) {
        if (amount == 0) {
            return false;
        }
        return resources.getOrDefault(playerId, 0) >= amount;
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
     * @param sourcePos location where the unit build from
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

    public Set<Long> allUnitIds() {
        return grid.unitMap().keySet();
    }

    public Unit[] allUnits() {
        return grid.unitMap().values().toArray(Unit[]::new);
    }

    /**
     * Advance current game state with given actions
     */
    public void advance(List<Action> acts) {
    }

    /**
     * @return all available actions at current game state
     */
    public ArrayList<Action> getAllActions() {
        return null;
    }

    public synchronized void addPlayerAction(PlayerAction pa) {
        int playerId = pa.playerId();
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

    public int getResource(int playerId) {
        return resources.getOrDefault(playerId, 0);
    }
}
