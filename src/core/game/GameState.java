package core.game;

import core.actions.Action;
import core.units.Entity;
import core.units.Unit;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static utils.Constants.DEFAULT_GRID_HEIGHT;
import static utils.Constants.DEFAULT_GRID_WIDTH;

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

    /**
     * List for storing all actions
     */
    private final List<Action> allActions;

    GameState() {
        grid = new Grid(DEFAULT_GRID_HEIGHT, DEFAULT_GRID_WIDTH);

        allActions = new LinkedList<>();
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
     * Creates a deep copy of this game state, given player index. Sets up the game state so that it contains
     * only information available to the given player. If -1, state contains all information.
     *
     * @return a copy of this game state.
     */
    public GameState copy() {
        GameState copy = new GameState();

        copy.grid = this.grid.copy();
        copy.tick = this.tick;

        return copy;
    }

    public Entity getUnit(long unitId) {
        return grid.getEntity(unitId);
    }

    public void addUnit(Unit u) {
        grid.addUnit(u);
    }

    public Map<Long, Unit> getUnits() {
        return grid.getEntities();
    }

    /**
     * Advance current game state by certain amount of time
     *
     * @param elapsed time since last update
     */
    public void update(double elapsed) {

    }
}
