package core.game;

import UI.GUI;
import core.Constants;
import core.entity.Building;
import core.entity.EntityFactory;
import core.entity.Unit;
import player.Agent;
import player.HumanAgent;
import player.PlayerAction;
import util.FPSCounter;

import java.util.Random;

import static core.Constants.*;

public class Game {

    enum GameMode {Annihilation, Survivor}

    private final static int RUN_MODE = 1; // 0: fast, 1: flexible, 2: fixed
    // State of the game (objects, ticks, etc).

    private GameState gs;
    // Seed for the game

    private long seed;
    private Random rnd;
    // List of players

    private Agent[] players;
    private int numPlayers;
    private final FPSCounter fpsCounter = new FPSCounter();

    private final GameSummary gameSummary = new GameSummary();
    private final GameMode gm = GameMode.Annihilation;


    /**
     * Default constructor
     */
    public Game() {
    }

    /**
     * Game initialisation
     */
    public void init(Agent[] players) {
        this.gs = new GameState(new Grid(GRID_SIZE), gm);

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
            Building base = ef.getBuilding("base", playerId);
            base.setGridPos(BASE_LOCATION[i]);
            if (!gs.addBuilding(base)) {
                throw new IllegalArgumentException("Map does not have a base location");
            }

            Unit worker = ef.getUnit("worker", playerId);
            gs.addUnit(worker, BASE_LOCATION[i]);

            gs.changeResource(players[i].playerID(), Constants.STARTING_RESOURCE);
        }
    }


    /**
     * Main game loop
     *
     * @param frame {@link GUI}
     */
    public void run(GUI frame) {
        // Main game loop
        fpsCounter.start();
        long previous = System.nanoTime();
        long lag = 0;
        while (!frame.isClosed()) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            lag -= _run(elapsed, lag);

            // Check game termination
            int winnerId = gs.winnerId();
            if (winnerId != -1) {
                //TODO Post game processing
                postGameProcess(frame, winnerId);

                // Close frame in the end
                frame.dispose();
                break;
            }

            // Render as fast as possible
            frame.render(gs.copy());
//            Runtime.getRuntime().gc();
        }
    }

    // return the time updated

    private long _run(long elapsed, long lag) {
        long temp = lag;
        long res = 0L;
        if (RUN_MODE == 0) {
            update(elapsed);
            fpsCounter.incFrame();
            res = 0;
        } else if (RUN_MODE == 1) {
            if (lag >= NANO_PER_TICK) {
                fpsCounter.incFrame();
                update(lag);
                res = 0;
            }
        } else if (RUN_MODE == 2) {
            int count = 0;
            while (temp >= NANO_PER_TICK) {
                fpsCounter.incFrame();
                update(NANO_PER_TICK);
                temp -= NANO_PER_TICK;
                count++;
            }
            res = count * NANO_PER_TICK;
        } else {
            throw new IllegalArgumentException("Run mode not supported, exiting..");
        }

        return res;
    }
    private void update(long elapsed) {
        System.out.print("Current tick: " + gs.getTicks() + " TPS: " + fpsCounter.get() + '\r');
        for (Agent agent : players) {
            PlayerAction pa = agent.act(gs);
            if (pa == null) {
                continue;
            }
            gs.assign(pa);
            // Reset for human controller
            if (agent instanceof HumanAgent) {
                pa.reset();
            }
        }

        // Advance game state by 16ms
        gs.tick(elapsed);
    }

    private void postGameProcess(GUI frame, int winnerId) {
        // TODO add post game analyses
        // Do one last draw to clear board
        gs.tick(0);
        frame.render(gs.copy());

        fpsCounter.stop();
        gameSummary.conclude(gs.getTicks(), players()[winnerId]);

    }

    public int size() {
        return gs.grid().size();
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

    public GameSummary summary() {
        return gameSummary;
    }
}
