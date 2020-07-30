package visual;

import core.actions.Build;
import core.actions.Move;
import core.entities.Entity;
import core.entities.Unit;
import core.game.Game;
import core.game.GameState;
import core.game.Grid;
import players.HumanAgent;
import utils.Vector2d;
import utils.WindowInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import static core.Constants.*;

public class GUI extends JFrame {

    // Game
    private final Game game;
    private GameState gs;
    private Grid grid;

    // Frames
    private final GameView gameView;
    private final InfoView infoView;
    private final WindowInput wi;

    private final double screenDiagonal;
    private final Random rnd = new Random();
    final int scale = 1;

    // Human controller
    private final HumanAgent human;

    // Unit bounding box selection
    private Vector2d startDrag, endDrag;
    static ArrayList<Long> selected = new ArrayList<>();

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
        CELL_SIZE = GUI_GAME_VIEW_SIZE / game.getGrid().getSize() * scale;
        GUI_SIDE_PANEL_WIDTH = (int) (0.2 * screenDiagonal * scale);
        GUI_SIDE_PANEL_HEIGHT = (int) (0.4 * screenDiagonal * scale);

        this.game = game;
        this.wi = wi;
        this.human = human;
        this.gameView = new GameView(game);
        this.infoView = new InfoView();

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
                    if (!grid.accessible(gp.x, gp.y)) {
                        return;
                    }
                    for (long id : selected) {
                        // Add a move action to the human player's action map
                        human.addUnitAct(id, new Move(id, sp, gp));
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
        JPanel buttons = new JPanel();

        JButton build = new JButton("Build");
        build.addActionListener(e -> {
            Unit unit = new Unit("./resources/unit/light.json", Entity.nextId++, human.playerID());
            human.addBuildAct(new Build(unit));
        });

        buttons.add(build);
        sidePanel.add(buttons, c);

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
}
