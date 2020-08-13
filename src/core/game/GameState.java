package core.game;

import core.action.Action;
import core.action.Build;
import core.gameObject.Building;
import core.gameObject.Unit;
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
    private final Map<Long, Action> unitActions;
    // PlayerId <-> build actions
    private final Map<Integer, Queue<Action>> buildActions;
    // PlayerId <-> resource storage
    private final Map<Integer, Integer> resources;

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
        // Reload unit every tick
        for (Unit u : grid.getUnits().values()) {
            u.reload(-TIME_PER_TICK);
        }

        // Execute build actions
        for (Iterator<Queue<Action>> it = buildActions.values().iterator(); it.hasNext(); ) {
            Queue<Action> next = it.next();
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

        return copy;
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
     * @param u        {@link Unit} to add
     * @param fromBase if the unit is produced from base, calculate a nearby available position
     */
    public void addUnit(Unit u, boolean fromBase) {
        if (u == null) {
            return;
        }
        if (fromBase) {
            Vector2d facPos = grid.getBuilding(u.getAgentId(), Building.Type.BASE).getGridPos();
            Vector2d spawn = grid.findNearby(facPos, 5);
            if (spawn == null) {
                // TODO give indication to player
                return;
            }
            grid.updateScreenPos(u, spawn);
        }
        grid.addUnit(u);
    }

    public Set<Long> allUnitIds() {
        return grid.getUnits().keySet();
    }

    public Unit[] allUnits() {
        return grid.getUnits().values().toArray(Unit[]::new);
    }

    /**
     * Advance current game state with given action
     */
    public void advance(Action act) {
    }

    /**
     * @return all available actions at current game state
     */
    public ArrayList<Action> getAllActions() {
        return null;
    }

    public synchronized void addAction(int playerId, Action act) {
        if (act instanceof Build) {
            Queue<Action> actList = buildActions.get(playerId);
            if (actList == null) {
                actList = new LinkedList<>();
                actList.add(act);
                buildActions.put(playerId, actList);
            } else {
                actList.add(act);
            }
        } else {
            unitActions.put(act.actorId(), act);
        }
    }
}
