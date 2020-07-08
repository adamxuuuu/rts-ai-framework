package core.actions;

import core.game.GameState;

public interface Action {

    /**
     * Execute this command under game state X
     *
     * @param gs the game state which the command is executed
     * @return true if the command can be executed
     */
    boolean exec(GameState gs);

    Action copy();

}
