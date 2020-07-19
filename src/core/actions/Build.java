package core.actions;

import core.game.GameState;
import core.units.Entity;
import core.units.Unit;

public class Build implements Action {

    private final Entity entity;
    private boolean isComplete;

    public Build(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        if (entity instanceof Unit) {
            gs.addUnit((Unit) entity);
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
