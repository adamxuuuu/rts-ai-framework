package player.mc;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;
import player.PlayerActionFactory;
import player.heuristics.StateHeuristic;
import util.ElapsedCpuTimer;
import util.StatSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarloAgent extends Agent {

    private final Random rnd;
    private final MCParams params;
    private StateHeuristic heuristic;
    private int fmCalls;

    public MonteCarloAgent(long seed, MCParams params) {
        super(seed);
        rnd = new Random(seed);
        this.params = params;
    }

    @Override
    public PlayerAction act(GameState gs) {


        //Set up the heuristic for this player
        this.heuristic = params.getHeuristic();

        //Gather all available actions:
        List<PlayerAction> allActions = sampling(gs);

        fmCalls = 0;
        boolean end = false;

        StatSummary[] scores = new StatSummary[allActions.size()];

        PlayerAction bestAction = null;
        double maxQ = Double.NEGATIVE_INFINITY;
        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        while (!end) {
            int rootActionIndex = rnd.nextInt(allActions.size());
            PlayerAction act = allActions.get(rootActionIndex);

            double score = rollout(gs, act);

            //Update scores and keep a reference to the action with the highest average.
            if (scores[rootActionIndex] == null)
                scores[rootActionIndex] = new StatSummary();

            scores[rootActionIndex].add(score);
            if (scores[rootActionIndex].mean() > maxQ) {
                maxQ = scores[rootActionIndex].mean();
                bestAction = act;
            }

            //Stop conditions:
            if (params.stop_type == params.STOP_TIME && ect.elapsedNanos() >= params.num_time)
                end = true;
        }

//        System.out.println("[Player: " + playerId + "] Tick " + gs.getTicks() + ", num actions: " +
//                ", FM calls: " + fmCalls + ". Executing " + (bestAction != null ? bestAction.toString() : null));

        return bestAction;
    }

    private double rollout(GameState gs, PlayerAction act) {
        GameState gsCopy = copyGameState(gs);
        boolean end = false;
        int step = 0;

        while (!end) {
            advance(gsCopy, act);

            //Check if it's time to end this rollout. 1) either because it's a game end, 2) we've reached the end of it...
            step++;
            end = gsCopy.gameOver() || (step == params.ROLLOUT_LENGTH);

            // ... or 3) we have no more thinking time available (agent's budget)
            boolean budgetOver = (params.stop_type == params.STOP_FMCALLS && fmCalls >= params.num_fmcalls);
            end |= budgetOver;

        }

        //We evaluate the state found at the end of the rollout with an heuristic.
        return heuristic.evaluateState(gs, gsCopy);
    }

    public GameState copyGameState(GameState gs) {
        return gs.copy();
    }

    private void advance(GameState gs, PlayerAction act) {
        gs.advance(act);
        fmCalls++;
    }

    private List<PlayerAction> sampling(GameState gs) {
        PlayerActionFactory factory = new PlayerActionFactory(gs, playerId);
        List<PlayerAction> allActions = new ArrayList<>();
        while (allActions.size() < params.MAX_CHILDREN) {
            PlayerAction temp = factory.randomAction(rnd);
            if (temp.empty()) {
                break;
            }
            allActions.add(temp);
        }

        if (allActions.isEmpty()) {
            allActions.add(new PlayerAction(playerId));
        }
        return allActions;
    }

    @Override
    public Agent copy() {
        return null; //not needed.
    }
}
