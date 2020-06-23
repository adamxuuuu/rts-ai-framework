import core.game.Game;
import utils.WindowInput;
import visual.GUI;

import static utils.Constants.VISUAL;

class Run {
    /**
     * Runs 1 game.
     *
     * @param g - game to run
     */
    static void runGame(Game g) {
        WindowInput wi;
        GUI frame = null;
        if (VISUAL) {
            wi = new WindowInput();
            wi.windowClosed = false;
            frame = new GUI(g, "genRTS", wi, true);
            frame.addWindowListener(wi);
            //frame.addKeyListener(ki);
        }

        g.run(frame);
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.init();

        runGame(g);
    }

}
