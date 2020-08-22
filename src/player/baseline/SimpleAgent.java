package player.baseline;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;

public class SimpleAgent extends Agent {

    public SimpleAgent() {
        super(0);
    }

    @Override
    public PlayerAction act(GameState gs) {
        PlayerAction pa = new PlayerAction(playerId);
        return null;
    }

    @Override
    public Agent copy() {
        return new SimpleAgent();
    }
}
