package core.action;

import core.Constants;
import core.game.GameState;
import core.gameObject.Building;
import core.gameObject.Entity;
import core.gameObject.Unit;

public class Build extends Action {

    private final Entity entity;
    private long buildTime;

    public Build(Entity entity) {
        this.entity = entity;
        this.buildTime = entity.getBuildTime() * Constants.SECOND_NANO;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        buildTime -= elapsed;
        if (buildTime > 0) {
//            System.out.println("building...");
            return;
        }

        if (entity instanceof Unit) {
            gs.addUnit((Unit) entity, true);
        } else if (entity instanceof Building) {
            gs.addBuilding((Building) entity);
        }
        isComplete = true;
    }

    @Override
    public Action copy() {
        return null;
    }
}
