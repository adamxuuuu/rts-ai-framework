package core.action;

import core.Constants;
import core.game.GameState;

/**
 * Do nothing action
 */
public class None extends Action {

    private long duration;

    public None() {
        this.duration = 0;
    }

    public None(int idleSecond) {
        this.duration = idleSecond * Constants.SECOND_NANO;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        duration -= elapsed;
        if (duration > 0) {
            // Building in progress
            return;
        }

        isComplete = true;
    }

    @Override
    public Action copy() {
        return new None();
    }

    @Override
    public long actorId() {
        return -1;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
