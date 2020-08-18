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

    private final long harvesterId;
    private long resourceId;

    public Harvest(long harvesterId) {
        this.harvesterId = harvesterId;
    }

    public Harvest(long harvesterId, long resourceId) {
        this.harvesterId = harvesterId;
        this.resourceId = resourceId;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        Grid grid = gs.getGrid();
        Unit harvester = grid.getUnit(harvesterId);

        if (harvester == null) {
            isComplete = true;
            return;
        }

        Pathfinder pf = new Pathfinder(harvester.getGridPos());
        Deque<PfNode> path;

        if (harvester.full()) {
            // Return to base
            int playerId = harvester.getAgentId();
            Vector2d basePos = grid.getBuilding(playerId, Building.Type.BASE).getGridPos();
            LinkedList<Vector2d> temp = basePos.neighborhood(1, 0, grid.size(), false);
            Vector2d dest = Utils.shortestPos(basePos, temp, grid);

            path = pf.pathTo(dest, grid);
            boolean atBase = Pathfinder.move(grid, path, dest, harvester);
            if (atBase) {
                gs.manageResource(playerId, harvester.getCarry());
                harvester.dump();
            } else {
                return;
            }
        }

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
        return new Harvest(harvesterId);
    }

    @Override
    public long actorId() {
        return harvesterId;
    }
}
