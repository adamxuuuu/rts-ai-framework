package player;

import core.action.Action;
import core.action.None;
import core.game.GameState;

import java.util.ArrayList;
import java.util.Random;

public class RandomAgent extends Agent {

    private final Random random;

    public RandomAgent(long seed) {
        super(seed);
        random = new Random(seed);
    }

    @Override
    public Action act(GameState gs) {
        ArrayList<Action> actions = gs.getAllActions();
        if (actions == null || actions.isEmpty()) {
            return new None();
        }
        return actions.get(random.nextInt(actions.size()));
    }

    @Override
    public Agent copy() {
        return null;
    }
}
