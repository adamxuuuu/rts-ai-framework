//package player.mcts;
//
//import core.game.GameState;
//import player.PlayerAction;
//import player.heuristics.StateHeuristic;
//import util.ElapsedCpuTimer;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class MCTSNode {
//
//    private MCTSParams params;
//
//    private MCTSNode root;
//    private MCTSNode parent;
//    private MCTSNode[] children;
//
//    private int playerId;
//    private GameState gameState;
//    private GameState rootGameState;
//    private ArrayList<PlayerAction> playerActions;
//
//    private Random rnd;
//    private int visitCount;
//    private double totalReward;
//    private int depth;
//    private double[] bounds = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
//    private StateHeuristic heuristic;
//
//    MCTSNode(MCTSParams p, Random rnd, int num_actions, ArrayList<PlayerAction> pas, int playerID) {
//        this(p, null, rnd, num_actions, pas, null, playerID, null, null);
//    }
//
//    private MCTSNode(MCTSParams p, MCTSNode parent, Random rnd, int num_actions,
//                     ArrayList<PlayerAction> playerActions, StateHeuristic sh, int playerId, MCTSNode root, GameState gameState) {
//        this.params = p;
//        this.parent = parent;
//        this.rnd = rnd;
//        this.playerActions = playerActions;
//        this.root = root;
//        children = new MCTSNode[num_actions];
//        totalReward = 0.0;
//        this.playerId = playerId;
//        this.gameState = gameState;
//        if (parent != null) {
//            depth = parent.depth + 1;
//            this.heuristic = sh;
//        } else {
//            depth = 0;
//        }
//
//    }
//
//    void setRootGameState(MCTSNode root, GameState gs) {
//        this.gameState = gs;
//        this.root = root;
//        this.rootGameState = gs;
//        this.heuristic = params.getHeuristic();
//    }
//
//    void search(ElapsedCpuTimer elapsedTimer) {
//        double avgTimeTaken;
//        double acumTimeTaken = 0;
//        long remaining;
//        int numIters = 0;
//
//        int remainingLimit = 5;
//        boolean run = true;
//
//        while (run) {
////            System.out.println("------- " + root.actions.size() + " -------");
//            ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
//            MCTSNode selected = treePolicy();
//            double delta = selected.rollOut();
//            backUp(selected, delta);
//            numIters++;
//
//            //Stopping condition
//            if (params.stop_type == params.STOP_TIME) {
//                acumTimeTaken += (elapsedTimerIteration.elapsedMillis());
//                avgTimeTaken = acumTimeTaken / numIters;
//                remaining = elapsedTimer.remainingTimeMillis();
//                run = remaining > 2 * avgTimeTaken && remaining > remainingLimit;
//            }
//        }
//    }
//
//    private MCTSNode treePolicy() {
//        MCTSNode cur = this;
//        while (!cur.gameState.gameOver()) {
//            if (cur.notFullyExpanded()) {
//                return cur.expand();
//            } else {
//                cur = cur.uct();
//            }
//        }
//        return cur;
//    }
//
//    private MCTSNode expand() {
//
//        //No turn end, expand
//        int bestAction = -1;
//        double bestValue = -1;
//
//        for (int i = 0; i < children.length; i++) {
//            double x = rnd.nextDouble();
//            if (x > bestValue && children[i] == null) {
//                bestAction = i;
//                bestValue = x;
//            }
//        }
//
//        //Roll the state, create a new node and assign it.
//        GameState nextState = gameState.copy();
//        List<PlayerAction> availableActions = nextState.getAllAvailableActions(playerId);
//        List<PlayerAction> nextActions = advance(nextState, availableActions.get(bestAction));
//
//        MCTSNode node = new MCTSNode(params, this, this.rnd, nextActions.size(),
//                null, heuristic, this.playerId, this.depth == 0 ? this : this.root, nextState);
//        children[bestAction] = node;
//        return node;
//    }
//
//    private ArrayList<PlayerAction> advance(GameState gs, PlayerAction pa) {
//        gs.advance(pa);
////        root.fmCallsCount++;
//        return gs.getAllAvailableActions(playerId);
//    }
//
//    // --------------------------------------Getter & Setter-------------------------------------------//
//
//
//    public MCTSNode getRoot() {
//        return root;
//    }
//
//    public GameState getGameState() {
//        return gameState;
//    }
//
//    public ArrayList<PlayerAction> getPlayerActions() {
//        return playerActions;
//    }
//
//    public int getVisitCount() {
//        return visitCount;
//    }
//
//    public void setVisitCount(int visitCount) {
//        this.visitCount = visitCount;
//    }
//
//    public double getTotalReward() {
//        return totalReward;
//    }
//
//    public void setTotalReward(double totalReward) {
//        this.totalReward = totalReward;
//    }
//
//    public MCTSNode[] getChildren() {
//        return children;
//    }
//
//    public void setChildren(MCTSNode[] children) {
//        this.children = children;
//    }
//}
