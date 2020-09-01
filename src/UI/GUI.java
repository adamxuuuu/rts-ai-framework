package UI;

import core.action.Action;
import core.action.*;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.Resource;
import core.entity.Unit;
import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import player.HumanAgent;
import util.Vector2d;
import util.WindowInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private final int scale = 1;
    private final Random rnd = new Random();

    // Controller
    private final HumanAgent humanController;
    private final WindowInput wi;

    // Unit bounding box selection
    private Vector2d startDrag, endDrag;
    static ArrayList<Long> selected = new ArrayList<>();

    static SpriteSheet spriteSheet = null;

    public GUI(Game game, String title, WindowInput wi, boolean closeAppOnClosingWindow, HumanAgent humanController) {
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
        GUI_GAME_VIEW_SIZE = (int) (0.35 * screenDiagonal * scale);
        GUI_SIDE_PANEL_WIDTH = (int) (0.1 * screenDiagonal * scale);
        INFO_PANEL_HEIGHT = (int) (0.4 * screenDiagonal * scale);

        this.game = game;
        this.humanController = humanController;
        this.wi = wi;
        this.gameView = new GameView(game);
        this.infoView = new InfoView();
        initSpriteSheet();

        setFocusable(true);
        requestFocus();
        this.addKeyListener(new KeyController(gameView));

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
        BufferedImage ss = loader.loadImage("./resources/sprite/rts_spritesheet.png");
        spriteSheet = new SpriteSheet(ss);
    }

    private JPanel createGamePanel() {

        JPanel panel = new JPanel();
        if (humanController != null) {
            panel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Vector2d sp = new Vector2d(e.getX(), e.getY());
                    Vector2d gp = GameView.screenToGrid(sp);
                    // Select
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        selected.clear();
                        long selectedId = grid.selectUnitId(humanController.playerID(), sp);
                        if (selectedId == -1) {
                            return;
                        }
                        selected.add(selectedId);
                        // Interact
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (!isValidPos(gp.x, gp.y) || !grid.accessible(gp.x, gp.y)) {
                            // TODO invalid mouse location
                            return;
                        }

                        Action toExecute;
                        Entity enemy = grid.getEnemyAt(humanController.playerID(), gp);
                        if (enemy == null) {
                            // TODO optimisation needed allocation is only for moving
                            LinkedList<Vector2d> allocations = grid.allocateNearby(gp, selected.size());
                            allocations.push(gp);
                            for (long uId : selected) {
                                Unit u = grid.getUnit(uId);
                                if (u == null) {
                                    return;
                                }
                                Resource res = grid.getResourceAt(gp.x, gp.y);
                                if (u.getType().equals(Unit.Type.WORKER) && res != null) {
                                    // Add a harvest action to the human player's action map
                                    toExecute = new Harvest(uId, res.getEntityId());
                                } else {
                                    // Add a move action to the human player's action map
                                    toExecute = new Move(grid.getUnit(uId), allocations.pop(), grid);
                                }
                                humanController.addUnitAction(uId, toExecute);
                            }
                        } else {
                            for (long uId : selected) {
                                // Add a attack action to the human player's action map
                                humanController.addUnitAction(uId, new Attack(uId, enemy.getEntityId()));
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
                            ArrayList<Long> res = grid.selectUnitIds(humanController.playerID(), startDrag, endDrag);
                            if (res == null || res.isEmpty()) {
                                return;
                            }
                            selected.addAll(res);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setFocusable(true);
                    requestFocus();
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

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

//      Buttons for building units and buildings
        EntityFactory ef = EntityFactory.getInstance();
        if (humanController != null) {
            JPanel unitButtons = new JPanel();
            for (String entityName : ef.unitTable.keySet()) {
                JButton buildUnit = new JButton(entityName);

                buildUnit.addActionListener(a -> {
                    Unit unit = ef.getUnit(entityName, humanController.playerID());
                    if (!gs.checkResource(humanController.playerID(), unit.getCost())) {
                        return;
                    }
                    humanController.addTrainAction(new Train(unit));
                });

                unitButtons.add(buildUnit);
                sidePanel.add(unitButtons, c);
            }
        }

        c.gridy++;
        sidePanel.add(infoView, c);

        return sidePanel;
    }

    /**
     * Call to all components to draw
     *
     * @param gs game state
     */
    public void render(GameState gs) {
        this.gs = gs;
        this.grid = gs.grid();

        gameView.render(gs);
        infoView.render(gs);

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
