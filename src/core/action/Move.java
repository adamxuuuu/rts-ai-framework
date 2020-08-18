package core.action;

import core.entity.Unit;
import core.entity.pathfinding.Pathfinder;
import core.entity.pathfinding.PfNode;
import core.game.GameState;
import core.game.Grid;
import util.Vector2d;

import java.util.Deque;

public class Move extends Action {

    private final long unitId;
    private final Vector2d gridDest;
    private final Deque<PfNode> path;

    public Move(long unitId, Vector2d gridDest, Deque<PfNode> path) {
        this.unitId = unitId;
        this.gridDest = gridDest;
        this.path = path;
    }

    public Move(Unit unit, Vector2d gridDest, Grid grid) {
        this.unitId = unit.getEntityId();
        this.gridDest = gridDest;

        path = new Pathfinder(unit.getGridPos()).pathTo(gridDest, grid);
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
        return new Move(unitId, gridDest, path);
    }

    @Override
    public long actorId() {
        return unitId;
    }
}
