package core.action;

import core.game.GameState;
import core.game.Grid;
import core.gameObject.Unit;
import core.gameObject.pathfinding.Pathfinder;
import core.gameObject.pathfinding.PfNode;
import util.Vector2d;

import java.util.Deque;

public class Move extends Action {

    private long unitId;
    private Vector2d gridDest;
    private Deque<PfNode> path;

    public Move(Unit unit, Vector2d gridDest, Grid grid) {
        this.unitId = unit.getEntityId();
        this.gridDest = gridDest;

        path = new Pathfinder(unit.getGridPos()).pathTo(gridDest, grid);
    }

    public Move() {
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Grid grid = gs.getGrid();
        Unit unit = grid.getUnit(unitId);
        // Unit may die during the process
        if (unit == null) {
            isComplete = true;
            return;
        }

        isComplete = Pathfinder.move(grid, path, gridDest, unit);
    }

    @Override
    public Action copy() {
        return new Move();
    }

    @Override
    public long actorId() {
        return unitId;
    }
}
