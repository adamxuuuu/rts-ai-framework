package core.commands;

import core.game.GameState;

public interface Command {

    /**
     * Execute this command under game state X
     *
     * @param gs the game state which the command is executed
     * @return true if the command can be executed
     */
    boolean exec(GameState gs);

    Command copy();

}
