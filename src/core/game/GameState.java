package core.game;

import core.action.Action;
import core.gameObject.Building;
import core.gameObject.Unit;
import util.Vector2d;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import static core.Constants.GRID_SIZE;

public class GameState {

    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    // The game grid
    private Grid grid;

    GameState() {
        grid = new Grid(GRID_SIZE);
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean gameOver() {
        return false;
    }

    public void tick() {
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
            Vector2d facPos = grid.getBuilding(u.getAgentId(), Building.BuildingType.BASE).getGridPos();
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
     * Advance current game state by certain amount of time
     */
    public void update() {
    }

    /**
     * @return all available actions at current game state
     */
    public ArrayList<Action> getAllActions() {
        return null;
    }

}
