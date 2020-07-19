package core.game;

import core.actions.Action;
import players.Agent;
import players.HumanAgent;
import utils.FPSCounter;
import visual.GUI;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static core.Constants.TIME_PER_FRAME;

public class Game {

    // State of the game (objects, ticks, etc).
    private GameState gs;

    // Seed for the game
    private long seed;
    private Random rnd;

    // List of players
    private Agent[] players;
    private int numPlayers;

    /**
     * Default constructor
     */
    public Game() {
    }

    /**
     * Default initialisation
     */
    public void init(Agent[] players) {
        gs = new GameState();

        this.players = players;
    }

    public void run(GUI frame) {
        FPSCounter fpsCounter = new FPSCounter();

        // Main game loop
        long previous = System.nanoTime();
        double lag = 0.0;
        while (!frame.isClosed()) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            // Update game state according to delta time since
            while (lag >= TIME_PER_FRAME) {
                update(elapsed);
                lag -= TIME_PER_FRAME;
                fpsCounter.count();
            }

            boolean gameOver = gameOver();
            if (gameOver) {
                //TODO Post game processing
                break;
            }

            // Render as fast as possible (hopefully)
            frame.render(getGameState());

            fpsCounter.printResult(System.nanoTime());
        }
    }

    private void update(double elapsed) {
        for (Agent agent : players) {
//            System.out.println(agent.toString());
            if (agent instanceof HumanAgent) {
                Iterator<Map.Entry<Long, Action>> it = ((HumanAgent) agent).getActions().entrySet().iterator();
                while (it.hasNext()) {
                    Action next = it.next().getValue();
                    if (next.isComplete()) {
                        it.remove();
                    } else {
                        next.exec(gs, elapsed);
                    }
                }
            }
        }
        // game update
        gs.update(elapsed);
        // increment tick
        gs.tick();
    }

    private boolean gameOver() {
        return gs.gameOver();
    }

    /**
     * @return the game grid
     */
    public Grid getGrid() {
        return gs.getGrid();
    }

    /**
     * @return a copy of the current game state.
     */
    private GameState getGameState() {
        //TODO return a copy
        return gs.copy();
    }

    /**
     * Return the human player
     *
     * @return the human player
     */
    public HumanAgent getHuman() {
        for (Agent ag : players) {
            if (ag instanceof HumanAgent) {
                return (HumanAgent) ag;
            }
        }
        return null;
    }
}
