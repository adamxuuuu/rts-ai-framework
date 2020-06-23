package core.game;

import core.level.Grid;
import core.units.Unit;

import java.util.List;

import static utils.Constants.DEFAULT_GRID_HEIGHT;
import static utils.Constants.DEFAULT_GRID_WIDTH;

public class GameState {

    /**
     * The game grid
     */
    private Grid grid;

    /**
     * Current tick of the game
     */
    private int tick = 0;

    GameState() {
        grid = new Grid(DEFAULT_GRID_HEIGHT, DEFAULT_GRID_WIDTH);
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

    public Unit getUnit(int unitId) {
        return null;
    }

    public void addUnit(Unit u) {
        grid.addUnit(u);
    }

    public List<Unit> getUnits() {
        return grid.getAllUnits();
    }
}
