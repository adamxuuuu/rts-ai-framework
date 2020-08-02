package UI;

import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import core.gameObject.Building;
import core.gameObject.Entity;
import core.gameObject.Resource;
import core.gameObject.Unit;
import util.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static core.Constants.CELL_SIZE;
import static core.Constants.GUI_GAME_VIEW_SIZE;

public class GameView extends JComponent {

    // Game
    private final Game game;
    private GameState gs;
    private Grid grid;

    // Static
    static int size;
    static Dimension dimension;

    GameView(Game game) {
        this.game = game;
        this.grid = game.getGrid().copy();
        size = grid.size();

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

        Toolkit.getDefaultToolkit().sync();
    }

    private void paint(Graphics2D g) {
        if (gs == null) {
            return;
        }

        // For a better graphics, enable this: (be aware this could bring performance issues depending on your HW & OS).
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g);
        drawEntities(g);
    }

    private void drawGrid(Graphics2D g) {
        if (grid == null) {
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
//                g.setColor(Color.BLACK);
//                g.drawString(i + ":" + j, j * CELL_SIZE, i * CELL_SIZE + CELL_SIZE / 2);
                Grid.TerrainType tt = grid.getTerrainAt(i, j);
                switch (tt) {
                    case WATER -> {
                        g.setColor(Color.CYAN);
                        g.fillRect(j * CELL_SIZE, i * CELL_SIZE,
                                CELL_SIZE, CELL_SIZE);
                    }
                    case LAND -> g.setColor(Color.WHITE);
                    case MOUNTAIN -> {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(j * CELL_SIZE, i * CELL_SIZE,
                                CELL_SIZE, CELL_SIZE);
                    }
                }
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawEntities(Graphics2D g) {
        // Buildings and resource
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Building b = grid.getBuildingAt(i, j);
                if (b != null) {
                    g.drawImage(getSprite(b), i * CELL_SIZE, j * CELL_SIZE, this);
//                    Vector2d gp = b.getGridPos();
//                    g.setColor(PLAYER_COLOR[b.getAgentId()]);
//                    g.drawRoundRect(gp.x * CELL_SIZE, gp.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, CELL_SIZE / 3, CELL_SIZE / 3);
//                    g.drawString(String.valueOf(b.getCurrentHP()), gp.x * CELL_SIZE, gp.y * CELL_SIZE + CELL_SIZE / 4);
                }
                Resource r = grid.getResourcesAt(i, j);
                if (r != null) {
                    g.drawImage(getSprite(r), i * CELL_SIZE, j * CELL_SIZE, this);
//                    g.setColor(Color.GREEN);
//                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Units
        for (Unit u : gs.allUnits()) {
            Vector2d sp = u.getScreenPos();
            g.drawImage(getSprite(u), sp.x - CELL_SIZE / 2, sp.y - CELL_SIZE / 2, this);
//            g.setColor(PLAYER_COLOR[u.getAgentId()]);
//            double angle = (double) u.getCurrentHP() / u.getMaxHp() * 360;
//            g.fillArc(sp.x - CELL_SIZE / 2, sp.y - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE, 90, (int) Math.round(angle));
            if (GUI.selected.contains(u.getEntityId())) {
                g.setColor(Color.BLACK);
                g.drawRect(sp.x - CELL_SIZE / 2, sp.y - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private BufferedImage getSprite(Entity e) {
        SpriteSheet.SpriteProperty sp;
        if (e instanceof Resource) {
            sp = SpriteSheet.spritePropertyMap.get(e.getSpriteKey()[0]);
        } else {
            sp = SpriteSheet.spritePropertyMap.get(e.getSpriteKey()[e.getAgentId()]);
        }
        return GUI.spriteSheet.getSprite(sp);
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
