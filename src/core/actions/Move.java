package core.actions;

import core.game.GameState;
import core.game.Grid;
import core.units.Unit;
import core.units.pathfinding.Pathfinder;
import core.units.pathfinding.PfNode;
import utils.Vector2d;
import visual.GameView;

import java.util.Deque;

public class Move implements Action {

    private final long unitId;
    private final Vector2d gridDest;
    private final Vector2d screenDest;
    private boolean isComplete;

    private PfNode wayPoint;

    public Move(long unitId, Vector2d screenDest, Vector2d gridDest) {
        this.unitId = unitId;
        this.screenDest = screenDest;
        this.gridDest = gridDest;
        this.isComplete = false;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Unit unit = gs.getUnit(unitId);
        Grid grid = gs.getGrid();
        // Unit may die during the process
        if (unit == null) {
            isComplete = true;
            return;
        }
        Vector2d gp = unit.getGridPos(); // Unit grid position

        Pathfinder pf = new Pathfinder(gp);

        // Way points
        Deque<PfNode> path = pf.pathTo(gridDest, grid);

        if (path != null && !path.isEmpty() && wayPoint == null) {
            wayPoint = path.pop();
        }

        if (wayPoint == null) {
            isComplete = true;
            return;
        }
        Vector2d wp = wayPoint.getPosition();

        Vector2d diff = GameView.gridToScreen(wp).subtract(unit.getScreenPos());
//        Vector2d dt = wayPoint.getPosition().subtract(unit.getGridPos());
//        float deltaTime = (float) (elapsed / Constants.SECOND_LONG);
        Vector2d dt = diff.nonZero().mul(unit.getSpeed());
        dt = new Vector2d(absMin(diff.x, dt.x), absMin(diff.y, dt.y));

        grid.moveUnit(unit, dt);

        Vector2d sp = unit.getScreenPos();
        if (sp.equals(GameView.gridToScreen(wp))) {
            wayPoint = null;
            if (sp.equals(screenDest)) {
                isComplete = true;
            }
        }

    }

    private int absMin(int a, int b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }

    @Override
    public Action copy() {
        return new Move(unitId, screenDest, gridDest);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
