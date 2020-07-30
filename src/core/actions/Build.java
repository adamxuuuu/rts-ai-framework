package core.actions;

import core.Constants;
import core.entities.Entity;
import core.entities.Unit;
import core.game.GameState;

public class Build implements Action {

    private final Entity entity;
    private boolean isComplete;
    private long buildTime;

    public Build(Entity entity) {
        this.entity = entity;
        this.buildTime = entity.getBuildTime() * Constants.SECOND_NANO;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        buildTime -= Constants.TIME_PER_FRAME;
        if (buildTime > 0) {
//            System.out.println("building...");
            return;
        }

        if (entity instanceof Unit) {
            gs.addUnit((Unit) entity, true);
        }
        isComplete = true;
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
