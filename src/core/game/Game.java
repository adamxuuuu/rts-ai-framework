package core.game;

import UI.GUI;
import core.Constants;
import core.action.Action;
import core.gameObject.Building;
import core.gameObject.Entity;
import player.Agent;
import player.HumanAgent;

import java.util.Iterator;
import java.util.Random;

import static core.Constants.BASE_LOCATION;
import static core.Constants.TIME_PER_TICK;

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

        this.seed = System.currentTimeMillis();
        this.rnd = new Random(seed);

        this.players = players;
        this.numPlayers = players.length;
        initBase();
    }

    private void initBase() {
        if (numPlayers > Constants.BASE_LOCATION.length) {
            throw new IllegalArgumentException("Too many players");
        }

        for (int i = 0; i < numPlayers; i++) {
            Building base = new Building("./resources/building/base.json", Entity.nextId++, players[i].playerID());
            base.setGridPos(BASE_LOCATION[i]);
            if (!gs.addBuilding(base)) {
                throw new IllegalArgumentException("Map does not have a base location");
            }
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

            boolean gameOver = gameOver();
            if (gameOver) {
                //TODO Post game processing
                frame.dispose();
                break;
            }

            // Render as fast as possible
            frame.render(gsCopy());
        }
    }

    private void tick() {
        // Process human input/actions
        processInput();
        for (Agent agent : players) {
            if (agent instanceof HumanAgent) {
                continue;
            }
            Action act = agent.act(gs);
            gs.addAction(agent.playerID(), act);
        }
        // Advance game state by 16ms
        gs.tick();
    }

    private void processInput() {
        HumanAgent human = humanPlayer();
        Action act = human.getBuildAct();
        if (act != null) {
            if (act.isComplete()) {
                human.poll();
            } else {
                act.exec(gs, TIME_PER_TICK);
            }
        }
        Iterator<Action> it = human.getUnitActs().values().iterator();
        while (it.hasNext()) {
            Action next = it.next();
            if (next.isComplete()) {
                it.remove();
            } else {
                next.exec(gs, TIME_PER_TICK);
            }
        }
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
    private GameState gsCopy() {
        return gs.copy();
    }

    /**
     * Return the human player
     *
     * @return the human player
     */
    public HumanAgent humanPlayer() {
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
