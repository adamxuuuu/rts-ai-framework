package players;

import core.commands.Command;
import core.game.GameState;
import utils.ElapsedCpuTimer;

public abstract class Agent {

    protected int playerID;
    protected long seed;

    /**
     * Default constructor
     */
    public Agent(long seed) {
        this.seed = seed;
    }

    /**
     * Function requests an command from the agent, given current game state observation.
     *
     * @param gs  - current game state.
     * @param ect - a timer that indicates when the turn time is due to finish.
     * @return - action to play in this game state.
     */
    public abstract Command order(GameState gs, ElapsedCpuTimer ect);

    /**
     * Getter for player ID
     *
     * @return - player's ID
     */
    public final int playerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public abstract Agent copy();


}
