package core.action;

import UI.GameView;
import core.Constants;
import core.game.GameState;
import core.game.Grid;
import core.gameObject.Entity;
import core.gameObject.Unit;
import core.gameObject.pathfinding.Pathfinder;
import core.gameObject.pathfinding.PfNode;
import util.Utils;
import util.Vector2d;

import java.util.Deque;
import java.util.LinkedList;


public class Attack extends Action {

    private final long attackerId;
    private final long targetId;

    private PfNode wayPoint;

    public Attack(long attackerId, long targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Grid grid = gs.getGrid();
        Unit attacker = grid.getUnit(attackerId);
        Entity target = grid.getEntity(targetId);

        if (attacker == null || target == null) {
            isComplete = true;
            return;
        }
        int range = attacker.getRange();
        Vector2d attackPos = attacker.getGridPos();
        Vector2d targetPos = target.getGridPos();

        // Use euclidean distance to check if in range
        boolean inRange = Vector2d.euclideanDistance(attackPos, targetPos) < range;
        if (inRange) {
            if (attacker.getTimeTillNextAttack() > 0) {
                return;
            }
            System.out.println("attack!!!!!");
            attacker.setTimeTillNextAttack(attacker.getRateOfFire() * Constants.SECOND_NANO);

            target.takeDamage(attacker.getAttack());
            // Kill the target
            if (target.getCurrentHP() <= 0) {
                grid.removeEntity(target);
            }
        } else {
            Pathfinder pf = new Pathfinder(attackPos);
            LinkedList<Vector2d> temp = targetPos.neighborhood(range, 0, grid.size(), false);

            Vector2d dest = shortestPos(attackPos, temp, grid);
            Deque<PfNode> path = pf.pathTo(dest, grid);

            if (path != null && !path.isEmpty() && wayPoint == null) {
                wayPoint = path.pop();
            }

            if (wayPoint == null) {
                isComplete = true;
                return;
            }
            Vector2d wp = wayPoint.getPosition();

            Vector2d diff = GameView.gridToScreen(wp).subtract(attacker.getScreenPos());
            Vector2d dv = diff.unify().mul(attacker.getSpeed());
            dv = new Vector2d(Utils.absMin(diff.x, dv.x), Utils.absMin(diff.y, dv.y));

            // Move
            grid.updateGridPos(attacker, attacker.getScreenPos().add(dv));

            // Check if reached waypoint or destination
            Vector2d sp = attacker.getScreenPos();
            if (sp.equals(GameView.gridToScreen(wp))) {
                wayPoint = null;
                if (sp.equals(GameView.gridToScreen(dest))) {
                    isComplete = true;
                }
            }
        }
    }

    public Vector2d shortestPos(Vector2d start, LinkedList<Vector2d> ps, Grid grid) {
        double min = Double.MAX_VALUE;
        Vector2d res = null;
        for (Vector2d p : ps) {
            double tempDist = Vector2d.euclideanDistance(p, start);
            if (tempDist < min && grid.accessible(p.x, p.y)) {
                min = tempDist;
                res = p;
            }
        }
        return res;
    }

    @Override
    public Action copy() {
        return new Attack(attackerId, targetId);
    }

    @Override
    public long actorId() {
        return attackerId;
    }
}
