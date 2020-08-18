package core.action;

import core.Constants;
import core.entity.Building;
import core.entity.Entity;
import core.entity.Unit;
import core.game.GameState;

public class Build extends Action {

    private final Entity entity;

    private int playerId;
    private long buildTime;
    private int cost;

    public Build(Entity entity) {
        this.entity = entity;

        playerId = entity.getAgentId();
        buildTime = entity.getBuildTime() * Constants.SECOND_NANO;
        cost = entity.getCost();
    }

    private Build(Entity entity, long buildTime) {
        this.entity = entity;
        this.buildTime = buildTime;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        // Cost resource first time execute
        if (!gs.enoughResource(playerId, entity.getCost())) {
            isComplete = true;
            return;
        }

        if (cost != 0) {
            gs.manageResource(playerId, -cost);
            cost = 0;
        }

        buildTime -= elapsed;
        if (buildTime > 0) {
//            System.out.println("building...");
            return;
        }

        if (entity instanceof Unit) {
            // TODO: add build source
            Building source = gs.getGrid().getBuilding(entity.getAgentId(), Building.Type.BASE);
            gs.addUnit((Unit) entity, source.getGridPos());
        } else if (entity instanceof Building) {
            gs.addBuilding((Building) entity);
        }
        isComplete = true;
    }

    @Override
    public Action copy() {
        return new Build(entity, buildTime);
    }

    @Override
    public long actorId() {
        return 0;
    }
}
