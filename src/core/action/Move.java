package core.action;

import UI.GameView;
import core.game.GameState;
import core.game.Grid;
import core.gameObject.Unit;
import core.gameObject.pathfinding.Pathfinder;
import core.gameObject.pathfinding.PfNode;
import util.Utils;
import util.Vector2d;

import java.util.Deque;

public class Move extends Action {

    private final long unitId;
    private final Vector2d gridDest;

    private PfNode wayPoint;

    public Move(long unitId, Vector2d gridDest) {
        this.unitId = unitId;
        this.gridDest = gridDest;
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
        Vector2d gp = unit.getGridPos();

        Pathfinder pf = new Pathfinder(gp);

        // Calculate Way points/path
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
        Vector2d dv = diff.unify().mul(unit.getSpeed());
        dv = new Vector2d(Utils.absMin(diff.x, dv.x), Utils.absMin(diff.y, dv.y));

        // Move
        grid.updateGridPos(unit, unit.getScreenPos().add(dv));

        // Check if reached waypoint or destination
        Vector2d sp = unit.getScreenPos();
        if (sp.equals(GameView.gridToScreen(wp))) {
            wayPoint = null;
            if (sp.equals(GameView.gridToScreen(gridDest))) {
                isComplete = true;
            }
        }

    }

    @Override
    public Action copy() {
        return new Move(unitId, gridDest);
    }

    @Override
    public long actorId() {
        return unitId;
    }
}
