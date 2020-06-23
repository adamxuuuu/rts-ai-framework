package core.game;

import core.level.Grid;
import players.Agent;
import visual.GUI;

import java.util.ArrayList;
import java.util.Random;

import static utils.Constants.SECOND_LONG;
import static utils.Constants.TIME_PER_FRAME;

public class Game {

    // State of the game (objects, ticks, etc).
    private GameState gs;

    // Seed for the game
    private long seed;
    private Random rnd;

    // List of players
    private ArrayList<Agent> players;

    /**
     * Default constructor
     */
    public Game() {
    }

    public void init() {
        gs = new GameState();
    }

    public void run(GUI frame) {
        // FPS counter
        long lastFpsCheck = 0L;
        int frames = 0;

        // Main game loop
        long previous = System.nanoTime();
        double lag = 0.0;
        while (!frame.isClosed()) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            boolean gameOver = gameOver();

            // Update game state according to delta time since
            while (lag >= TIME_PER_FRAME) {
                update(elapsed);
                lag -= TIME_PER_FRAME;
                frames++;
            }

            if (gameOver) {
                break;
            }

            // Update graphics at a steady rate (hopefully)
            frame.render(getGameState());


            if (System.nanoTime() > lastFpsCheck + SECOND_LONG) {
                lastFpsCheck = System.nanoTime();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    private void update(double elapsed) {
        // game update

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
     * @return the game state.
     */
    private GameState getGameState() {
        return gs;
    }
}
