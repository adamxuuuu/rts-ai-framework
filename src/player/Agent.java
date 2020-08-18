package player;

import core.action.Action;
import core.game.GameState;

import java.util.Map;

public abstract class Agent {

    int playerID;
    long seed;
    PlayerAction playerAction;

    public Agent(long seed) {
        this.seed = seed;
        playerAction = new PlayerAction(playerID);
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
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void addUnitAction(long uId, Action action) {
        playerAction.addUnitAction(uId, action);
    }

    public void addBuildAction(Action candidate) {
        playerAction.addBuildAction(candidate);
    }

    public Map<Long, Action> getUnitActions() {
        return playerAction.unitActions();
    }

    public Action firstBuildAction() {
        return playerAction.buildActions().peek();
    }

    public Action removeFirstBuildAct() {
        return playerAction.buildActions().poll();
    }

    public String toString() {
        return this.getClass().getName();
    }
}
