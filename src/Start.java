import UI.GUI;
import core.game.Game;
import player.Agent;
import player.HumanAgent;
import player.RandomAgent;
import util.WindowInput;

/**
 * Entry point
 */
public class Start {

    enum PlayerType {
        DONOTHING,
        HUMAN,
        RANDOM,
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
        switch (playerType) {
            case HUMAN -> {
                return new HumanAgent();
            }
            case RANDOM -> {
                return new RandomAgent(System.currentTimeMillis());
            }
        }
        return null;
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
                ag.setPlayerID(i);
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
        GUI frame = new GUI(g, "genRTS", wi, true, g.humanPlayer());
        frame.addWindowListener(wi);

        g.run(frame);
    }

    public static void main(String[] args) {
        Game g = init(new PlayerType[]{PlayerType.HUMAN, PlayerType.RANDOM});
        runGame(g);
    }

}