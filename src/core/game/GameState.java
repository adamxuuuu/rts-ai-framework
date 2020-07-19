package core.game;

import core.units.Unit;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static core.Constants.DEFAULT_GRID_WIDTH;

public class GameState {

    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    /**
     * The game grid
     */
    private Grid grid;

    /**
     * Current tick of the game
     */
    private int tick = 0;

    GameState() {
        grid = new Grid(DEFAULT_GRID_WIDTH);
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

    public Unit getUnit(long unitId) {
        return (Unit) grid.getEntity(unitId);
    }

    public void addUnit(Unit u) {
        grid.addUnit(u);
    }

    public Map<Long, Unit> getUnits() {
        return grid.getEntities();
    }

    public Set<Map.Entry<Long, Unit>> allUnits() {
        return getUnits().entrySet();
    }

    /**
     * Advance current game state by certain amount of time
     *
     * @param elapsed time since last update
     */
    public void update(double elapsed) {

    }
}
