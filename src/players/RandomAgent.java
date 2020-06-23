package players;

import core.commands.Command;
import core.game.GameState;
import utils.ElapsedCpuTimer;

import java.util.Random;

public class RandomAgent extends Agent {

    private final Random random;

    public RandomAgent(long seed) {
        super(seed);
        random = new Random(seed);
    }

    @Override
    public Command order(GameState gs, ElapsedCpuTimer ect) {
        return null;
    }

    @Override
    public Agent copy() {
        return null;
    }
}
