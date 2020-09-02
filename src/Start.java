import UI.GUI;
import core.game.Game;
import core.game.GameSummary;
import player.Agent;
import player.HumanAgent;
import player.baseline.DoNothingAgent;
import player.baseline.RandomAgent;
import player.baseline.RandomBiasedAgent;
import player.mc.MonteCarloAgent;
import player.mcts.MCTSAgent;
import util.WindowInput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point
 */
public class Start {
    private static final int TOTAL_GAME_PLAYS = 50;

    enum PlayerType {
        DONOTHING,
        HUMAN,
        RANDOM,
        RANDOM_BIASED,
        OSLA,
        MC,
        SIMPLE,
        MCTS,
        RHEA,
        OEP
    }

    /**
     * @param playerType call agent constructor based on type
     * @return the player corresponding to type
     */
    private static Agent getAgent(PlayerType playerType) {
        Agent player = null;
        long seed = System.currentTimeMillis();
        switch (playerType) {
            case HUMAN -> player = new HumanAgent();
            case RANDOM -> player = new RandomAgent(seed);
            case DONOTHING -> player = new DoNothingAgent();
            case RANDOM_BIASED -> player = new RandomBiasedAgent(seed);
            case MC -> player = new MonteCarloAgent(seed);
            case MCTS -> player = new MCTSAgent(seed);
        }
        return player;
    }

    /**
     * Create agent instances based on given player types
     *
     * @param playerTypes types of agents
     * @return array of agents
     */
    private static Agent[] getPlayers(PlayerType[] playerTypes) {
        Agent[] players = new Agent[playerTypes.length];

        for (int i = 0; i < playerTypes.length; ++i) {
            Agent ag = getAgent(playerTypes[i]);
            if (ag != null) {
                ag.setPlayerId(i);
                players[i] = ag;
            }
        }
        return players;
    }

    /**
     * Construct the game
     *
     * @param playerTypes all player types
     * @return the game
     */
    private static Game init(PlayerType[] playerTypes) {
        Game g = new Game();
        g.init(getPlayers(playerTypes));

        return g;
    }

    private static GameSummary runGame(Game g) {
        WindowInput wi = new WindowInput();
        wi.windowClosed = false;
        GUI frame = new GUI(g, "genRTS", wi, true, g.humanController());
        frame.addWindowListener(wi);

        g.run(frame);
        return g.summary();
    }

    public static void main(String[] args) {
        int[] tickPerGame = new int[TOTAL_GAME_PLAYS];
        Map<Integer, Integer> winnerMap = new HashMap<>();

//        PlayerType[] players = new PlayerType[]{PlayerType.HUMAN, PlayerType.RANDOM};
//        PlayerType[] players = new PlayerType[]{PlayerType.HUMAN, PlayerType.MCTS};
//        PlayerType[] players = new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM_BIASED};
//        PlayerType[] players = new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM};
//        PlayerType[] players = new PlayerType[]{PlayerType.MC, PlayerType.DONOTHING};
//        PlayerType[] players = new PlayerType[]{PlayerType.MC, PlayerType.RANDOM_BIASED};
//        PlayerType[] players = new PlayerType[]{PlayerType.RANDOM, PlayerType.RANDOM, PlayerType.RANDOM, PlayerType.RANDOM};
//        PlayerType[] players = new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM_BIASED, PlayerType.RANDOM_BIASED, PlayerType.RANDOM_BIASED};
//        PlayerType[] players = new PlayerType[]{PlayerType.MC, PlayerType.MC, PlayerType.MC, PlayerType.MC};
//        PlayerType[] players = new PlayerType[]{PlayerType.MCTS, PlayerType.MCTS, PlayerType.MCTS, PlayerType.MCTS};
        PlayerType[] players = new PlayerType[]{PlayerType.RANDOM, PlayerType.RANDOM_BIASED, PlayerType.MC, PlayerType.MCTS};

        for (int i = 0; i < TOTAL_GAME_PLAYS; i++) {
            System.out.println("Running game number: " + i);
            Game g = init(players);
            GameSummary gameSummary = runGame(g);

            tickPerGame[i] = gameSummary.totalTick;
            winnerMap.merge(gameSummary.winner.playerId, 1, Integer::sum);

            // I think we need GC here
            Runtime.getRuntime().gc();
        }

        System.out.println();
        System.out.println(Arrays.toString(tickPerGame));
        System.out.println(winnerMap);
        System.exit(0);
    }

}
