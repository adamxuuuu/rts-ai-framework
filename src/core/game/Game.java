package core.game;

import UI.GUI;
import core.Constants;
import core.entity.Building;
import core.entity.EntityFactory;
import core.entity.Unit;
import player.Agent;
import player.HumanAgent;
import player.PlayerAction;

import java.util.Random;

import static core.Constants.*;

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
     * Game initialisation
     */
    public void init(Agent[] players) {
        this.gs = new GameState(new Grid(GRID_SIZE));

        this.seed = System.currentTimeMillis();
        this.rnd = new Random(seed);

        this.players = players;
        this.numPlayers = players.length;

        initBaseAndResource();
    }

    private void initBaseAndResource() {
        if (numPlayers > Constants.BASE_LOCATION.length) {
            throw new IllegalArgumentException("Too many players");
        }

        EntityFactory ef = EntityFactory.getInstance();
        for (int i = 0; i < numPlayers; i++) {
            int playerId = players[i].playerID();
            Building base = ef.build("base", playerId);
            base.setGridPos(BASE_LOCATION[i]);
            if (!gs.addBuilding(base)) {
                throw new IllegalArgumentException("Map does not have a base location");
            }

            Unit worker = ef.train("worker", playerId);
            gs.addUnit(worker, BASE_LOCATION[i]);

            gs.handleResource(players[i].playerID(), Constants.STARTING_RESOURCE);
        }
    }


    /**
     * Main game loop, Update at a fix rate (16ms).
     * Render as fast as it can
     *
     * @param frame the game graphical interface
     */
    public void run(GUI frame) {
        // Main game loop
        long previous = System.nanoTime();
        long lag = 0;
        while (!frame.isClosed()) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            // update at a fix time stamp
            while (lag >= TIME_PER_TICK) {
                tick();
                lag -= TIME_PER_TICK;
            }

            int winnerId = gameOver();
            if (winnerId != -1) {
                //TODO Post game processing
                System.out.println("Game Over! The winner is: " + players()[winnerId].toString());
                postGameProcess();
                break;
            }

            // Render as fast as possible
            frame.render(gsCopy());
        }
    }

    private void tick() {
        System.out.print("Current tick: " + gs.getTicks() + '\r');
        for (Agent agent : players) {
            PlayerAction pa = agent.act(gs);
            if (pa == null) {
                continue;
            }
            gs.addPlayerAction(pa);
            // Reset for human controller
            if (agent instanceof HumanAgent) {
                pa.reset();
            }
        }

        // Advance game state by 16ms
        gs.tick(TIME_PER_TICK);
    }

    public int gameOver() {
        return gs.gameOver();
    }

    private void postGameProcess() {
        // TODO add post game analyses
        gs.tick(0);
        System.out.println("Total tick: " + gs.getTicks());
    }

    public int size() {
        return gs.getGrid().size();
    }

    /**
     * @return a copy of the current game state.
     */
    private GameState gsCopy() {
        return gs.copy();
    }

    /**
     * @return the human player
     */
    public HumanAgent humanController() {
        for (Agent ag : players) {
            if (ag instanceof HumanAgent) {
                return (HumanAgent) ag;
            }
        }
        return null;
    }

    public Agent[] players() {
        return players;
    }
}
