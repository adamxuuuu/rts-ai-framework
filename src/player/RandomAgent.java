package player;

import core.action.Build;
import core.entity.Unit;
import core.game.GameState;

import java.util.Random;

public class RandomAgent extends Agent {

    private static final int ACTION_COUNT = 5;

    private final Random rnd;
    private int acted;

    public RandomAgent(long seed) {
        super(seed);
        rnd = new Random(seed);
        acted = 0;
    }

    @Override
    public PlayerAction act(GameState gs) {
        if (acted++ > ACTION_COUNT) {
            return playerAction;
        }
//        ArrayList<Action> actions = gs.getAllActions();
//        if (actions == null || actions.isEmpty()) {
//            return new None();
//        }
//        return actions.get(random.nextInt(actions.size()));
        playerAction.addBuildAction(new Build(new Unit("./resources/unit/light.json", this.playerID)));
        return playerAction;
    }

    @Override
    public Agent copy() {
        return null;
    }
}
