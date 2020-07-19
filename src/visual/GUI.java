package visual;

import core.actions.Build;
import core.actions.Move;
import core.game.Game;
import core.game.GameState;
import core.units.Entity;
import core.units.Unit;
import players.HumanAgent;
import utils.Vector2d;
import utils.WindowInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Random;

import static core.Constants.*;

public class GUI extends JFrame {

    // Game
    private final Game game;
    private GameState gs;

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
                    System.out.println(GameView.gridToScreen(gp));
                    System.out.println(GameView.screenToGrid(sp));
                    // Select
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Interact
                    if (game.getGrid().accessible(gp.x, gp.y)) {
                        for (Map.Entry<Long, Unit> entry : gs.allUnits()) {
                            long uId = entry.getKey();
                            // Add a move action to the human player's action map
                            human.addAction(uId, new Move(uId, sp, gp));
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
                endDrag = new Vector2d(e.getX(), e.getY());

                if (startDrag != null && !endDrag.equals(startDrag)) {
                    //TODO unit selection
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
        build.setSize(50, 20);
        build.addActionListener(e -> {
            int gx = rnd.nextInt(game.getGrid().getSize()), gy = rnd.nextInt(game.getGrid().getSize());
            if (game.getGrid().accessible(gx, gy)) {
                Vector2d gp = new Vector2d(gx, gy);
                Unit unit = new Unit("Test", rnd.nextInt(5) + 1, gp, GameView.gridToScreen(gp));
                unit.setEntityId(Entity.nextId++);
                unit.setAgentId(human.playerID());
                human.addAction(0, new Build(unit));
            }
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
