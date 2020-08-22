package player.baseline;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;
import player.PlayerActionFactory;

import java.util.Random;

public class RandomBiasedAgent extends Agent {

    private final Random rnd;

    public RandomBiasedAgent(long seed) {
        super(seed);
        rnd = new Random(seed);
    }

    @Override
    public PlayerAction act(GameState gs) {
        PlayerActionFactory factory = new PlayerActionFactory(gs, playerId);

        return factory.randomBiasedAction(rnd);
    }

    @Override
    public Agent copy() {
        return new RandomBiasedAgent(seed);
    }
}
