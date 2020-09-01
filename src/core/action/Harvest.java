package core.action;

import core.entity.Building;
import core.entity.Resource;
import core.entity.Unit;
import core.entity.pathfinding.Pathfinder;
import core.entity.pathfinding.PfNode;
import core.game.GameState;
import core.game.Grid;
import util.Utils;
import util.Vector2d;

import java.util.Deque;
import java.util.LinkedList;

public class Harvest extends Action {

    private final long workerId;
    private long resourceId;

    public Harvest(long workerId, long resourceId) {
        this.workerId = workerId;
        this.resourceId = resourceId;
    }

    private Harvest(long workerId) {
        this.workerId = workerId;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Grid grid = gs.grid();
        Unit harvester = grid.getUnit(workerId);

        if (harvester == null) {
            isComplete = true;
            return;
        }

        Pathfinder pf = new Pathfinder(harvester.getGridPos());
        Deque<PfNode> path;

        // Return to base
        if (harvester.isFull()) {
            int playerId = harvester.getPlayerId();
            Building base = grid.getBuilding(playerId, Building.Type.BASE);
            if (base == null) {
                isComplete = true;
                return;
            }

            Vector2d basePos = base.getGridPos();
            LinkedList<Vector2d> temp = basePos.neighborhood(1, 0, grid.size(), false);
            Vector2d dest = Utils.shortestPos(basePos, temp, grid);

            path = pf.pathTo(dest, grid);
            boolean atBase = Pathfinder.move(grid, path, dest, harvester);
            if (atBase) {
                gs.changeResource(playerId, harvester.getCarry());
                harvester.dump();
            } else {
                return;
            }
        }

        // Find resource
        Resource resource = (Resource) grid.getEntity(resourceId);
        if (resource == null || resource.getCurrentHP() == 0) {
            // If current resource is depleted find nearest
            resource = grid.findNearestResource(harvester.getGridPos());
            if (resource == null) {
                // No resource left nearby
                isComplete = true;
                return;
            }
            resourceId = resource.getEntityId();
        }

        // Go to resource or harvest if in range
        Vector2d resPos = resource.getGridPos();
        Vector2d myPos = harvester.getGridPos();
        if (myPos.equals(resPos)) {
            //Harvest
            if (harvester.canAttack()) {
                harvester.harvest();
                resource.takeDamage(harvester.getAttack());
            }
        } else {
            //Go to
            path = pf.pathTo(resPos, grid);
            Pathfinder.move(grid, path, resPos, harvester);
        }
    }

    @Override
    public Action copy() {
        return new Harvest(workerId);
    }

}
