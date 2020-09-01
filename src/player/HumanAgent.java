package player;

import core.action.Action;
import core.action.Train;
import core.game.GameState;

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

    public void addUnitAction(long uId, Action action) {
        playerAction.addUnitAction(uId, action);
    }

    public void addTrainAction(Train candidate) {
        playerAction.addTrainAction(candidate);
    }

}
