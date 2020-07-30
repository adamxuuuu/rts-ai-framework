package core.actions;

import core.game.GameState;

public interface Action {

    /**
     * Execute this command under game state X
     *
     * @param gs      the game state which the command is executed
     * @param elapsed
     */
    void exec(GameState gs, double elapsed);

    Action copy();

    boolean isComplete();

}
