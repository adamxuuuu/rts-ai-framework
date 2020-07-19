package core.actions;

import core.game.GameState;

/**
 * Do nothing action
 */
public class None implements Action {

    @Override
    public void exec(GameState gs, double elapsed) {
    }

    @Override
    public Action copy() {
        return new None();
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
