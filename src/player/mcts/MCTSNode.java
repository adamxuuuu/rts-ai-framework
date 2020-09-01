package player.mcts;

import core.game.GameState;
import player.PlayerAction;
import player.heuristics.StateHeuristic;
import util.ElapsedCpuTimer;

import java.util.List;
import java.util.Random;

public class MCTSNode {

    private final MCTSParams params;

    private MCTSNode root;
    private final MCTSNode parent;
    private MCTSNode[] children;
    private int type; // 0 : max, 1 : min, -1: Game-over

    private final int playerId;
    private GameState gameState;
    private GameState rootGameState;
    private final List<PlayerAction> playerActions;

    private final Random rnd;
    private int visitCount;
    private double totalReward;
    private final int depth;
    private final double[] bounds = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    private StateHeuristic heuristic;

    MCTSNode(MCTSParams p, Random rnd, int num_actions, List<PlayerAction> pas, int playerID) {
        this(p, null, rnd, num_actions, pas, null, playerID, null, null);
    }

    private MCTSNode(MCTSParams p, MCTSNode parent, Random rnd, int num_actions,
                     List<PlayerAction> playerActions, StateHeuristic sh, int playerId, MCTSNode root, GameState gameState) {
        this.params = p;
        this.parent = parent;
        this.rnd = rnd;
        this.playerActions = playerActions;
        this.root = root;
        children = new MCTSNode[num_actions];
        totalReward = 0.0;
        this.playerId = playerId;
        this.gameState = gameState;
        if (parent != null) {
            depth = parent.depth + 1;
            this.heuristic = sh;
        } else {
            depth = 0;
        }

    }

    void setRootGameState(MCTSNode root, GameState gs) {
        this.gameState = gs;
        this.root = root;
        this.rootGameState = gs;
        this.heuristic = params.getHeuristic();
    }

    void search() {
        boolean run = true;

        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        while (run) {
            MCTSNode selected = treePolicy();
            double delta = selected.rollOut(ect);
            backUp(selected, delta);

            //Stopping condition
            if (params.stop_type == params.STOP_TIME && ect.elapsedNanos() >= params.num_time)
                run = false;
        }
    }

    private MCTSNode treePolicy() {
        MCTSNode cur = this;
        while (!cur.gameState.gameOver()) {
            if (cur.notFullyExpanded()) {
                return cur.expand();
            } else {
                cur = cur.uct();
            }
        }
        return cur;
    }

    private MCTSNode expand() {
        int bestAction = -1;
        double bestValue = -1;

        for (int i = 0; i < children.length; i++) {
            double x = rnd.nextDouble();
            if (x > bestValue && children[i] == null) {
                bestAction = i;
                bestValue = x;
            }
        }

        //Roll the state, create a new node and assign it.
        GameState nextState = gameState.copy();
        List<PlayerAction> availableActions = sample(nextState);
        List<PlayerAction> nextActions = null;
        try {
            nextActions = advance(nextState, availableActions.get(bestAction));
        } catch (IndexOutOfBoundsException e) {
            nextActions = advance(nextState, availableActions.get(0));
        }

        MCTSNode node = new MCTSNode(params, this, this.rnd, nextActions.size(),
                null, heuristic, this.playerId, this.depth == 0 ? this : this.root, nextState);
        children[bestAction] = node;
        return node;
    }

    private List<PlayerAction> advance(GameState gs, PlayerAction pa) {
        gs.advance(pa);
        return sample(gs);
    }

    private MCTSNode uct() {
        MCTSNode best = null;
        double bestScore = 0;
        for (MCTSNode pate : children) {
            double exploitation = pate.totalReward / pate.visitCount;
            double exploration = Math.sqrt(Math.log(visitCount) / pate.visitCount);
            if (type == 0) {
                // max node:
                exploitation = (bounds[1] + exploitation) / (2 * bounds[1]);
            } else {
                exploitation = (bounds[0] - exploitation) / (2 * bounds[0]);
            }
            //            System.out.println(exploitation + " + " + exploration);

            double tmp = params.K * exploitation + exploration;
            if (best == null || tmp > bestScore) {
                best = pate;
                bestScore = tmp;
            }
        }
        return best;
    }

    private double rollOut(ElapsedCpuTimer ect) {
        if (params.ROLOUTS_ENABLED) {
            GameState rolloutState = gameState.copy();
            int thisDepth = this.depth;
            while (!finishRollout(rolloutState, thisDepth, ect)) {
                List<PlayerAction> temp = sample(rolloutState);
                PlayerAction next = temp.get(rnd.nextInt(temp.size()));
                advance(rolloutState, next);
                thisDepth++;
            }
            return normalise(this.heuristic.evaluateState(playerId, rolloutState), 0, 1);
        }

        return normalise(this.heuristic.evaluateState(playerId, this.gameState), 0, 1);
    }

    private boolean finishRollout(GameState rollerState, int depth, ElapsedCpuTimer ect) {
        if (depth >= params.ROLLOUT_LENGTH)
            return true;

        //end of game
        return rollerState.gameOver() || ect.elapsedNanos() > params.num_time;
    }

    private List<PlayerAction> sample(GameState gs) {
        return gs.sample(playerId, params.MAX_CHILDREN, rnd);
    }

    private void backUp(MCTSNode node, double result) {
        MCTSNode n = node;
        while (n != null) {
            n.visitCount++;
            n.totalReward += result;
            if (result < n.bounds[0]) {
                n.bounds[0] = result;
            }
            if (result > n.bounds[1]) {
                n.bounds[1] = result;
            }
            n = n.parent;
        }
    }

    int mostVisitedAction() {
        int selected = -1;
        double bestValue = -Double.MAX_VALUE;
        boolean allEqual = true;
        double first = -1;

        for (int i = 0; i < children.length; i++) {

            if (children[i] != null) {
                if (first == -1)
                    first = children[i].visitCount;
                else if (first != children[i].visitCount) {
                    allEqual = false;
                }

                double childValue = children[i].visitCount;
                childValue = noise(childValue, params.epsilon, this.rnd.nextDouble());     //break ties randomly
                if (childValue > bestValue) {
                    bestValue = childValue;
                    selected = i;
                }
            }
        }

        if (selected == -1) {
            selected = 0;
        } else if (allEqual) {
            //If all are equal, we opt to choose for the one with the best Q.
            selected = bestAction();
        }

        return selected;
    }

    private int bestAction() {
        int selected = -1;
        double bestValue = -Double.MAX_VALUE;

        for (int i = 0; i < children.length; i++) {

            if (children[i] != null) {
                double childValue = children[i].totalReward / (children[i].visitCount + params.epsilon);
                childValue = noise(childValue, params.epsilon, this.rnd.nextDouble());     //break ties randomly
                if (childValue > bestValue) {
                    bestValue = childValue;
                    selected = i;
                }
            }
        }

        if (selected == -1) {
            System.out.println("Unexpected selection!");
            selected = 0;
        }

        return selected;
    }

    private boolean notFullyExpanded() {
        for (MCTSNode child : children) {
            if (child == null) {
                return true;
            }
        }

        return false;
    }

    private double normalise(double a_value, double a_min, double a_max) {
        if (a_min < a_max)
            return (a_value - a_min) / (a_max - a_min);
        else    // if bounds are invalid, then return same value
            return a_value;
    }

    private double noise(double input, double epsilon, double random) {
        return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
    }

    // --------------------------------------Getter & Setter-------------------------------------------//


    public MCTSNode getRoot() {
        return root;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<PlayerAction> getPlayerActions() {
        return playerActions;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(double totalReward) {
        this.totalReward = totalReward;
    }

    public MCTSNode[] getChildren() {
        return children;
    }

    public void setChildren(MCTSNode[] children) {
        this.children = children;
    }

}
