package visual;

import core.game.Game;
import core.game.GameState;
import core.units.Unit;
import utils.Vector2d;
import utils.WindowInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static utils.Constants.*;

public class GUI extends JFrame {

    // Game
    private final Game game;
    private GameState gs;

    // Frames
    private final GameView gameView;
    private final WindowInput wi;

    private final double screenDiagonal;
    private final Vector2d panTranslate;
    final int scale = 1;

    public GUI(Game game, String title, WindowInput wi, boolean closeAppOnClosingWindow) {
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
        CELL_SIZE_HEIGHT = GUI_GAME_VIEW_SIZE / game.getGrid().getHeight() * scale;
        CELL_SIZE_WIDTH = GUI_GAME_VIEW_SIZE / game.getGrid().getWidth() * scale;

        this.game = game;
        this.wi = wi;

        panTranslate = new Vector2d(0, 0);
        gameView = new GameView(game);

        // Create Panels
        JPanel gamePanel = createGamePanel();
        getContentPane().add(gamePanel);

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
                // Left mouse button
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Vector2d p = GameView.screenToGrid(e.getX(), e.getY());

                    gs.addUnit(new Unit("Test", 1, p));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    for (Unit u : gs.getGrid().getAllUnits()) {
                        //if u.isSeleted()
                        //u.addAction(Move)
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

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
//        panel.add(Box.createRigidArea(new Dimension(0, GUI_COMP_SPACING / 2)), c);

//        c.gridy++;
        panel.add(gameView, c);

//        c.gridy++;
//        panel.add(Box.createRigidArea(new Dimension(0, GUI_COMP_SPACING / 2)), c);

        return panel;
    }

    /**
     * Render objects after update
     *
     * @param gs to be rendered
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
