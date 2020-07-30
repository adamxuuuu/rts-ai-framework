package core.game;

import core.Constants;
import core.actions.Action;
import core.entities.Building;
import core.entities.Entity;
import players.Agent;
import players.HumanAgent;
import utils.FPSCounter;
import visual.GUI;

import java.util.Iterator;
import java.util.Random;

import static core.Constants.BASE_LOCATION;
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
            Building base = new Building(Building.BuildingType.BASE);
            base.setEntityId(Entity.nextId++);
            base.setAgentId(players[i].playerID());
            base.setGridPos(BASE_LOCATION[i]);
            if (!gs.addBuilding(base)) {
                //TODO something went wrong
            }
        }
    }

    /**
     * Main game loop
     * Update at a fix rate (16ms).
     * Render as fast as it can
     *
     * @param frame the game frame
     */
    public void run(GUI frame) {
        FPSCounter fpsCounter = new FPSCounter();

        // Main game loop
        long previous = System.nanoTime();
        long lag = 0;
        while (!frame.isClosed()) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            // catch up
            while (lag >= TIME_PER_FRAME) {
                update();
                lag -= TIME_PER_FRAME;
                fpsCounter.count();
            }
//            update(elapsed);
//            fpsCounter.count();

            boolean gameOver = gameOver();
            if (gameOver) {
                //TODO Post game processing
                break;
            }

            fpsCounter.printResult(System.nanoTime());
            // Render as fast as possible (hopefully)
            frame.render(gsCopy());

        }
    }

    private void update() {
        for (Agent agent : players) {
            if (agent instanceof HumanAgent) {
                Action act = ((HumanAgent) agent).getBuildAct();
                if (act != null) {
                    act.exec(gs, TIME_PER_FRAME);
                    if (act.isComplete()) {
                        ((HumanAgent) agent).pop();
                    }
                }
                Iterator<Action> it = ((HumanAgent) agent).getUnitActs().values().iterator();
                while (it.hasNext()) {
                    Action next = it.next();
                    if (next.isComplete()) {
                        it.remove();
                    } else {
                        next.exec(gs, TIME_PER_FRAME);
                    }
                }
            } else {
                Action act = agent.act(gs);
                if (act != null) {
                    act.exec(gs, TIME_PER_FRAME);
                }
            }
        }
        // game update
        gs.update();
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
