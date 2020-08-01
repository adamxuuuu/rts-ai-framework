package player;

import core.action.Action;
import core.game.GameState;

public abstract class Agent {

    protected int playerID;
    protected long seed;

    public Agent(long seed) {
        this.seed = seed;
    }

    /**
     * Function requests an action from the agent, given current game state observation.
     *
     * @param gs - current game state.
     * @return - action to play in this game state.
     */
    public abstract Action act(GameState gs);

    public final int playerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public abstract Agent copy();

    public String toString() {
        return this.getClass().getName();
    }
}
