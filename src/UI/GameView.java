package UI;

import core.Constants;
import core.entity.Building;
import core.entity.Entity;
import core.entity.Resource;
import core.entity.Unit;
import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import player.Agent;
import util.FPSCounter;
import util.Utils;
import util.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import static core.Constants.CELL_SIZE;
import static core.Constants.GUI_GAME_VIEW_SIZE;

public class GameView extends JComponent {

    static int size;
    static Dimension dimension;

    // Game
    private final Game game;
    private GameState gs;
    private Grid grid;

    // Setting
    private boolean showGrid;
    private final FPSCounter fpsCounter;

    GameView(Game game) {
        dimension = new Dimension(GUI_GAME_VIEW_SIZE, GUI_GAME_VIEW_SIZE);

        this.game = game;
        this.fpsCounter = new FPSCounter();

        fpsCounter.start();
        size = game.size();
    }

    public Dimension getPreferredSize() {
        return dimension;
    }

    void render(GameState gs) {
        this.gs = gs;
        this.grid = gs.grid();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paint(g2d);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void paint(Graphics2D g) {
        if (gs == null) {
            return;
        }

        // For a better graphics, enable this: (be aware this could bring performance issues depending on your HW & OS).
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g);
        drawEntities(g);
        printInfo(g);
    }

    private void printInfo(Graphics2D g) {
        // Show resources
        int offset = 0;
        for (Agent agent : game.players()) {
            g.setColor(Constants.PLAYER_COLOR[agent.playerId]);
            int resource = gs.getResource(agent.playerID());
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Resource: " + resource, offset, dimension.height - CELL_SIZE); //added line (step 5).
            offset += dimension.width / 4;
        }
        // Print frames
        offset = 0;
        g.setColor(Color.BLACK);
        fpsCounter.incFrame(); //added line (step 4).
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("FPS: " + fpsCounter.get(), offset, dimension.height); //added line (step 5).
        offset += dimension.width / 4;

        // Print game objects
        g.drawString("Objects: " + gs.objectsCount(), offset, dimension.height); //added line (step 5).
        offset += dimension.width / 4;

        // Print ticks
        g.drawString("Ticks: " + gs.getTicks(), offset, dimension.height); //added line (step 5).
        offset += dimension.width / 4;
    }

    private void drawGrid(Graphics2D g) {
        if (grid == null) {
            return;
        }
        BufferedImage tile;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Grid.TerrainType tt = grid.getTerrainAt(i, j);
                switch (tt) {
                    case WATER -> {
                        tile = getSprite("scifiTile_13.png");
                    }
                    case LAND -> {
                        tile = getSprite("scifiTile_41.png");
                    }
                    case MOUNTAIN -> {
                        tile = getSprite("scifiTile_28.png");
                    }
                    default -> throw new IllegalArgumentException("New terrain type not implemented");
                }
                drawImage(tile, j * CELL_SIZE, i * CELL_SIZE, g);

                if (showGrid) {
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawEntities(Graphics2D g) {
        // Buildings and resource (static)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Building b = grid.getBuildingAt(i, j);
                if (b != null) {
                    drawImage(getSprite(b), j * CELL_SIZE, i * CELL_SIZE, g);
                    int barWidth = (int) ((double) b.getCurrentHP() / b.getMaxHp() * CELL_SIZE);
                    g.setColor(Color.GREEN);
                    drawHealthBar(g, j * CELL_SIZE, i * CELL_SIZE, barWidth);
                    continue;
                }
                Resource r = grid.getResourceAt(i, j);
                if (r != null) {
                    drawImage(getSprite(r), j * CELL_SIZE, i * CELL_SIZE, g);
                }
            }
        }

        // Units
        for (Unit u : grid.units()) {
            Vector2d sp = u.getScreenPos();
            BufferedImage bi = getSprite(u);
            float[] scales = {1f, 1f, 1f, 1f};
            float[] offsets = new float[4];
            // Health bar
            if (u.isDamaged()) {
                g.setColor(Color.GREEN);
                int barWidth = (int) ((double) u.getCurrentHP() / u.getMaxHp() * CELL_SIZE);
                drawHealthBar(g, sp.x, sp.y, barWidth);
            }

            if (GUI.selected.contains(u.getEntityId())) {
//                g.setColor(Color.DARK_GRAY);
//                g.drawRect(sp.x - CELL_SIZE / 2, sp.y - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                scales = new float[]{1f, 1f, 1f, 0.8f};
            }
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            BufferedImage resized = Utils.scaleDown(bi, (double) (CELL_SIZE * CELL_SIZE) / (bi.getWidth()));
            g.drawImage(resized, rop, sp.x - resized.getWidth() / 2, sp.y - resized.getHeight() / 2);
        }
    }

    private BufferedImage getSprite(Entity e) {
        if (e instanceof Resource || e instanceof Building) {
            return GUI.spriteSheet.getSubSprite(e.getSpriteKey()[0]);
        } else {
            return GUI.spriteSheet.getSubSprite(e.getSpriteKey()[e.getPlayerId()]);
        }
    }

    private BufferedImage getSprite(String path) {
        return GUI.spriteSheet.getSubSprite(path);
    }

    private void drawImage(BufferedImage bi, int x, int y, Graphics2D g) {
        g.drawImage(bi, x, y, CELL_SIZE, CELL_SIZE, this);
//        g.drawImage(bi, x, y, this);
    }

    private void drawHealthBar(Graphics2D g, int x, int y, int barWidth) {
        g.fillRect(x, y - CELL_SIZE, barWidth, CELL_SIZE / 10);
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

    public void toggleGrid() {
        showGrid = !showGrid;
    }

}
