import UI.GUI;
import core.game.Game;
import player.Agent;
import player.HumanAgent;
import player.baseline.DoNothingAgent;
import player.baseline.RandomAgent;
import player.baseline.RandomBiasedAgent;
import util.WindowInput;

/**
 * Entry point
 */
public class Start {

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
        switch (playerType) {
            case HUMAN -> player = new HumanAgent();
            case RANDOM -> player = new RandomAgent(System.currentTimeMillis());
            case DONOTHING -> player = new DoNothingAgent();
            case RANDOM_BIASED -> player = new RandomBiasedAgent(System.currentTimeMillis());
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

    private static void runGame(Game g) {
        WindowInput wi = new WindowInput();
        wi.windowClosed = false;
        GUI frame = new GUI(g, "genRTS", wi, true, g.humanController());
        frame.addWindowListener(wi);

        g.run(frame);
    }

    public static void main(String[] args) {
//        Game g = init(new PlayerType[]{PlayerType.HUMAN, PlayerType.RANDOM});
//        Game g = init(new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM});
//        Game g = init(new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM_BIASED});
        Game g = init(new PlayerType[]{PlayerType.RANDOM_BIASED, PlayerType.RANDOM, PlayerType.RANDOM_BIASED, PlayerType.RANDOM});
        runGame(g);
    }

}
