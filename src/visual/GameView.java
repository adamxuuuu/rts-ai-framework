package visual;

import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import core.units.Unit;
import utils.Vector2d;

import javax.swing.*;
import java.awt.*;

import static core.Constants.*;

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
        for (int i = 0; i < grid.getSize(); i++) {
            for (int j = 0; j < grid.getSize(); j++) {
//                g.setColor(Color.BLACK);
//                g.drawString(i + ":" + j, j * CELL_SIZE, i * CELL_SIZE + CELL_SIZE / 2);
                float h = grid.getHeightAt(i, j);
                int value = (int) (255 * h);
                // Terrain level: Sea, Ground, Mountain
                if (h < SEA_LVL) {
                    g.setColor(new Color(value, 255, 255));
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE,
                            CELL_SIZE, CELL_SIZE);
                } else if (h < GRD_LVL) {
                    g.setColor(new Color(255, 255, value, value));
                } else {
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE,
                            CELL_SIZE, CELL_SIZE);
                }
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawUnits(Graphics2D g) {
        for (Unit u : gs.getUnits().values()) {
            Vector2d sp = u.getScreenPos();
            g.setColor(Color.BLUE);
            g.fillOval(sp.x - CELL_SIZE / 2, sp.y - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * Convert {@link Grid} position to screen position
     *
     * @param girdPos@return Screen position
     */
    public static Vector2d gridToScreen(Vector2d girdPos) {

        double x2 = girdPos.y * CELL_SIZE + CELL_SIZE / 2.0;
        double y2 = girdPos.x * CELL_SIZE + CELL_SIZE / 2.0;

        return new Vector2d((int) x2, (int) y2);
    }

    /**
     * Convert screen position to {@link Grid} position
     *
     * @param screenPos@return Grid position {@link Vector2d}
     */
    public static Vector2d screenToGrid(Vector2d screenPos) {
        double w = GameView.dimension.width;
        double h = GameView.dimension.height;
        double x2 = screenPos.y / h * h / CELL_SIZE;
        double y2 = screenPos.x / w * w / CELL_SIZE;

        return new Vector2d((int) x2, (int) y2);
    }

}
