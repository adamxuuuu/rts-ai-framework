package players;

import core.actions.Action;
import core.game.GameState;

import java.util.Random;

public class RandomAgent extends Agent {

    private final Random random;

    public RandomAgent(long seed) {
        super(seed);
        random = new Random(seed);
    }

    @Override
    public Action act(GameState gs) {
        return null;
    }

    @Override
    public Agent copy() {
        return null;
    }
}
