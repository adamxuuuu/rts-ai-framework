package players;

import core.actions.Action;
import core.game.GameState;

public class HumanAgent extends Agent {

    private Action action;

    public HumanAgent() {
        super(0);
    }

    @Override
    public Action act(GameState gs) {
        return action;
    }

    @Override
    public Agent copy() {
        return null;
    }

    public void updateAction(Action newAct) {
        this.action = newAct;
    }
}
