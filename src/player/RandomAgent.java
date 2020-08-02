package player;

import core.action.Action;
import core.action.Build;
import core.action.None;
import core.game.GameState;
import core.gameObject.Entity;
import core.gameObject.Unit;

import java.util.Random;

public class RandomAgent extends Agent {

    private static final int ACTION_COUNT = 5;

    private final Random random;
    private int acted;

    public RandomAgent(long seed) {
        super(seed);
        random = new Random(seed);
        acted = 0;
    }

    @Override
    public Action act(GameState gs) {
        if (acted++ > ACTION_COUNT) {
            return new None();
        }
//        ArrayList<Action> actions = gs.getAllActions();
//        if (actions == null || actions.isEmpty()) {
//            return new None();
//        }
//        return actions.get(random.nextInt(actions.size()));
        return new Build(new Unit("./resources/unit/light.json", Entity.nextId++, this.playerID));
    }

    @Override
    public Agent copy() {
        return null;
    }
}
