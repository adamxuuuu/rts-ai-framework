package player;

import core.game.GameState;

public abstract class Agent {

    public int playerId;
    public long seed;

    public Agent(long seed) {
        this.seed = seed;
    }

    /**
     * Function requests an action from the agent, given current game state observation.
     *
     * @param gs - current game state.
     * @return - action to play in this game state.
     */
    public abstract PlayerAction act(GameState gs);

    public abstract Agent copy();

    public final int playerID() {
        return playerId;
    }

    public final void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String toString() {
        return this.getClass().getName() + playerId;
    }
}
