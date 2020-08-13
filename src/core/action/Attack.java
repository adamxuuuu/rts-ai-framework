package core.action;

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
            attacker.setTimeTillNextAttack(attacker.getRateOfFire() * Constants.SECOND_NANO);

            target.takeDamage(attacker.getAttack());
            // Kill the target
            if (target.getCurrentHP() <= 0) {
                grid.removeEntity(target);
            }
        } else {
            LinkedList<Vector2d> temp = targetPos.neighborhood(range, 0, grid.size(), false);
            Vector2d dest = Utils.shortestPos(attackPos, temp, grid);

            Pathfinder pf = new Pathfinder(attackPos);
            Deque<PfNode> path = pf.pathTo(dest, grid);

            isComplete = Pathfinder.move(grid, path, dest, attacker);
        }
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
