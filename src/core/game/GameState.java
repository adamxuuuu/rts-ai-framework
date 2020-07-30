package core.game;

import core.entities.Building;
import core.entities.Unit;
import utils.Vector2d;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static core.Constants.GRID_SIZE;

public class GameState {

    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    // The game grid
    private Grid grid;

    // Ticks/frames of the game
    private int tick = 0;

    GameState() {
        grid = new Grid(GRID_SIZE);
    }

    public Grid getGrid() {
        return grid;
    }

    public int getTick() {
        return tick;
    }

    public boolean gameOver() {
        return false;
    }

    public void tick() {
        tick++;
    }

    /**
     * Creates a deep copy of this game state
     *
     * @return a copy of this game state.
     */
    public GameState copy() {
        GameState copy = new GameState();

        copy.grid = this.grid.copy();
        copy.tick = this.tick;

        return copy;
    }

    boolean addBuilding(Building b) {
        return grid.addBuilding(b);
    }

    public Unit getUnit(long unitId) {
        return grid.getUnit(unitId);
    }

    /**
     * Add a unit to the game
     * If the unit is produced from base
     * Calculate a nearby available position
     *
     * @param u        {@link Unit} to add
     * @param fromBase if the unit is produced from base
     */
    public void addUnit(Unit u, boolean fromBase) {
        if (u == null) {
            return;
        }
        if (fromBase) {
            Vector2d facPos = grid.getBuilding(u.getAgentId(), Building.BuildingType.BASE).getGridPos();
            Vector2d spawn = grid.findNearby(facPos);
            if (spawn == null) {
                // TODO give indication to player
                return;
            }
            grid.updateScreenPos(u, spawn);
        }
        grid.addUnit(u);
    }

    public Set<Long> allUnitIds() {
        return units().keySet();
    }

    public Collection<Unit> allUnits() {
        return units().values();
    }

    Map<Long, Unit> units() {
        return grid.getUnits();
    }

    /**
     * Advance current game state by certain amount of time
     */
    public void update() {

    }
}
