package UI;

import core.action.Attack;
import core.action.Build;
import core.action.Move;
import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import core.gameObject.Entity;
import core.gameObject.Unit;
import player.HumanAgent;
import util.Vector2d;
import util.WindowInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static core.Constants.*;

public class GUI extends JFrame {

    // Game
    private final Game game;
    private GameState gs;
    private Grid grid;

    // Panel
    private final GameView gameView;
    private final InfoView infoView;

    private final double screenDiagonal;
    private final Random rnd = new Random();
    final int scale = 1;

    // Controller
    private final HumanAgent human;
    private final WindowInput wi;

    // Unit bounding box selection
    private Vector2d startDrag, endDrag;
    static ArrayList<Long> selected = new ArrayList<>();
    static SpriteSheet spriteSheet = null;

    public GUI(Game game, String title, WindowInput wi, boolean closeAppOnClosingWindow, HumanAgent human) {
        super(title);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adjust frame size according to local machine
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        screenDiagonal = Math.sqrt(rect.width * rect.width + rect.height * rect.height);

        // Settings
        GUI_GAME_VIEW_SIZE = (int) (0.46 * screenDiagonal * scale);
        CELL_SIZE = GUI_GAME_VIEW_SIZE / game.getGrid().size() * scale;
        GUI_SIDE_PANEL_WIDTH = (int) (0.2 * screenDiagonal * scale);
        INFO_PANEL_HEIGHT = (int) (0.4 * screenDiagonal * scale);

        this.game = game;
        this.wi = wi;
        this.human = human;
        this.gameView = new GameView(game);
        this.infoView = new InfoView();
        initSpriteSheet();

        // Frame layout
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0;

        setLayout(gbl);

        // Create Panels
        JPanel gamePanel = createGamePanel();
        JPanel sidePanel = createSidePanel();

        gbc.gridx = 0;
        getContentPane().add(gamePanel, gbc);

        gbc.gridx++;
        getContentPane().add(sidePanel, gbc);

        // Frame properties
        pack();
        this.setVisible(true);
        this.setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if (closeAppOnClosingWindow) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        repaint();
    }

    private void initSpriteSheet() {
        ImageLoader loader = new ImageLoader();
        BufferedImage temp = loader.loadImage("./resources/sprite/rts_spritesheet.png");
        spriteSheet = new SpriteSheet(temp);
    }

    private JPanel createGamePanel() {

        JPanel panel = new JPanel();

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vector2d sp = new Vector2d(e.getX(), e.getY());
                Vector2d gp = GameView.screenToGrid(sp);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selected.clear();
                    Long selectedId = grid.selectUnitId(sp);
                    if (selectedId != null) {
                        selected.add(selectedId);
                    }
                    // Select
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Interact
                    if (!isValidPos(gp.x, gp.y) || !grid.accessible(gp.x, gp.y)) {
                        return;
                    }

                    Entity enemy = grid.getEnemyAt(human.playerID(), gp);

                    if (enemy == null) {
                        for (long uId : selected) {
                            // Add a move action to the human player's action map
                            human.addUnitAct(uId, new Move(uId, gp));
                        }
                    } else {
                        for (Long uId : selected) {
                            human.addUnitAct(uId, new Attack(uId, enemy.getEntityId()));
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = new Vector2d(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selected.clear();
                    endDrag = new Vector2d(e.getX(), e.getY());
                    if (startDrag != null && !endDrag.equals(startDrag)) {
                        selected.addAll(grid.selectUnitIds(startDrag, endDrag));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;
        c.weighty = 0;

        c.gridy = 0;
        panel.add(gameView, c);

        return panel;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();

        sidePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;
        c.weighty = 0;
        c.gridy = 0;
        c.gridx = 0;


        File folder = new File("./resources/unit");
        File[] listOfFiles = folder.listFiles();

        JPanel unitButtons = new JPanel();
        for (File file : listOfFiles != null ? listOfFiles : new File[0]) {
            if (file.isFile()) {
                String filename = file.getName();
                JButton buildUnit = new JButton(filename.substring(0, filename.lastIndexOf('.')));

                buildUnit.addActionListener(e -> {
                    Unit unit = new Unit(file.getPath(), Entity.nextId++, human.playerID());
                    human.addBuildAct(new Build(unit));
                });

                unitButtons.add(buildUnit);
                sidePanel.add(unitButtons, c);
            }
        }


//        c.gridy++;
//        sidePanel.add(infoView, c);

        return sidePanel;
    }

    /**
     * Call to all components to draw
     *
     * @param gs game state
     */
    public void render(GameState gs) {
        this.gs = gs;
        this.grid = gs.getGrid();
        gameView.render(gs);

        repaint();
    }

    /**
     * @return true if window is closed
     */
    public boolean isClosed() {
        return wi.windowClosed;
    }

    /**
     * Check if mouse interact location is inside game grid
     */
    private boolean isValidPos(int x, int y) {
        return x >= 0 && x < GameView.size && y >= 0 && y < GameView.size;
    }

}
