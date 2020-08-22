package core.action;

import core.game.GameState;

public abstract class Action {

    boolean isComplete = false;

    /**
     * Execute this command under game state X
     *
     * @param gs the game state which the command is executed
     */
    public abstract void exec(GameState gs, double elapsed);

    public abstract Action copy();

    public abstract long actorId();

    public boolean isComplete() {
        return isComplete;
    }

    public String toString() {
        return getClass().getSimpleName();
    }

}
