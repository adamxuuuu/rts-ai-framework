package core.action;

import core.Constants;
import core.entity.Entity;
import core.entity.Unit;
import core.entity.pathfinding.Pathfinder;
import core.entity.pathfinding.PfNode;
import core.game.GameState;
import core.game.Grid;
import util.Utils;
import util.Vector2d;

import java.util.Deque;
import java.util.LinkedList;


public class Attack extends Action {

    private final long attackerId;
    private final long targetId;

    public Attack(long attackerId, long targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Grid grid = gs.grid();
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
            if (!attacker.canAttack()) {
                return;
            }
            attacker.setTimeTillNextAttack(attacker.getRateOfFire() * Constants.SECOND_NANO);
            target.takeDamage(attacker.getAttack());
        } else {
            LinkedList<Vector2d> temp = targetPos.neighborhood(range, 0, grid.size(), false);
            Vector2d dest = Utils.shortestPos(attackPos, temp, grid);

            Deque<PfNode> path = new Pathfinder(attackPos).pathTo(dest, grid);

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
