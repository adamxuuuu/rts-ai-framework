package core.actions;

import core.game.GameState;

/**
 * Do nothing action
 */
public class None implements Action {

    @Override
    public boolean exec(GameState gs) {
        return false;
    }

    @Override
    public Action copy() {
        return new None();
    }
}
