package core.action;

import core.game.GameState;

/**
 * Do nothing action
 */
public class None extends Action {

    @Override
    public void exec(GameState gs, double elapsed) {
    }

    @Override
    public Action copy() {
        return new None();
    }

    @Override
    public long actorId() {
        return 0;
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
