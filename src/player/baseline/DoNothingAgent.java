package player.baseline;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;

public class DoNothingAgent extends Agent {

    public DoNothingAgent() {
        super(0);
    }

    @Override
    public PlayerAction act(GameState gs) {
        return new PlayerAction(playerId);
    }

    @Override
    public Agent copy() {
        return new DoNothingAgent();
    }
}
