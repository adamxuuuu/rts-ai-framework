package visual;

import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import core.units.Unit;
import utils.Vector2d;

import javax.swing.*;
import java.awt.*;

import static utils.Constants.*;

public class GameView extends JComponent {

    // Game
    private final Game game;
    private GameState gs;
    private Grid grid;

    // Static
    private static Dimension dimension;

    GameView(Game game) {
        this.game = game;
        this.grid = game.getGrid().copy();

        dimension = new Dimension(GUI_GAME_VIEW_SIZE, GUI_GAME_VIEW_SIZE);
    }

    void render(GameState gs) {
        this.gs = gs;
        this.grid = gs.getGrid();
    }

    public Dimension getPreferredSize() {
        return dimension;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paint(g2d);
    }

    private void paint(Graphics2D g) {
        if (gs == null) {
            return;
        }

        // For a better graphics, enable this: (be aware this could bring performance issues depending on your HW & OS).
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, dimension.width, dimension.height);

        drawGrid(g);
        drawUnits(g);

    }

    private void drawGrid(Graphics2D g) {
        if (grid == null) {
            return;
        }
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                float factor = grid.getHeightAt(i, j);
                int value = (int) (255 * factor);
                // Terrain level: Sea, Ground, Mountain
                if (factor < 0.1) {
                    g.setColor(new Color(value, 255, 255, value));
                } else if (factor < 0.6) {
                    g.setColor(new Color(255, 255, value, value));
                } else {
                    g.setColor(new Color(255, value, 255, value));
                }
                g.drawRect(j * CELL_SIZE_WIDTH, i * CELL_SIZE_HEIGHT,
                        CELL_SIZE_WIDTH, CELL_SIZE_HEIGHT);
//                g.setColor(Color.BLACK);
//                g.drawString(i + ":" + j, j * CELL_SIZE_WIDTH, i * CELL_SIZE_HEIGHT + CELL_SIZE_HEIGHT / 2);
            }
        }
    }

    private void drawUnits(Graphics2D g) {
        for (Unit u : gs.getUnits().values()) {
            Vector2d p = u.getScreenPos();
            //Vector2d ps = gridToScreen(p.x, p.y);

            g.setColor(Color.BLACK);
            g.fillOval(p.x, p.y, CELL_SIZE_WIDTH / 2, CELL_SIZE_WIDTH / 2);
        }
    }

    /**
     * Convert {@link Grid} position to screen position
     *
     * @param x row
     * @param y column
     * @return Screen position
     */
    public static Vector2d gridToScreen(double x, double y) {

        double y2 = (int) x * CELL_SIZE_HEIGHT;
        double x2 = (int) y * CELL_SIZE_WIDTH;

        return new Vector2d((int) x2, (int) y2);
    }

    /**
     * Convert screen position to {@link Grid} position
     *
     * @param x height
     * @param y width
     * @return Grid position {@link Vector2d}
     */
    public static Vector2d screenToGrid(double x, double y) {
        int w = GameView.dimension.width;
        int h = GameView.dimension.height;
        double y2 = x / w * w / CELL_SIZE_WIDTH;
        double x2 = y / h * h / CELL_SIZE_HEIGHT;

        //TODO debug
//        System.out.println((int) x2 + ":" + (int) y2);

        return new Vector2d((int) x2, (int) y2);
    }

}
