package player;

import core.action.Action;
import core.game.GameState;

import java.util.Map;

public class HumanAgent extends Agent {

    private final PlayerAction playerAction;

    public HumanAgent() {
        super(0);
        playerAction = new PlayerAction(playerId);
    }

    @Override
    public PlayerAction act(GameState gs) {
        return playerAction;
    }

    @Override
    public HumanAgent copy() {
        return new HumanAgent();
    }

    public void addUnitAction(Action action) {
        playerAction.addUnitAction(action.actorId(), action);
    }

    public void addBuildAction(Action candidate) {
        playerAction.addTrainAction(candidate);
    }

    public Map<Long, Action> getUnitActions() {
        return playerAction.unitActions();
    }

    public Action firstBuildAction() {
        return playerAction.trainActions().peek();
    }

    public Action removeFirstBuildAct() {
        return playerAction.trainActions().poll();
    }

}
