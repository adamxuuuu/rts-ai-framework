package player;

import core.game.GameState;

public class HumanAgent extends Agent {

    public HumanAgent() {
        super(0);
    }

    @Override
    public PlayerAction act(GameState gs) {
        return playerAction;
    }

    public HumanAgent copy() {
        return new HumanAgent();
    }

}
