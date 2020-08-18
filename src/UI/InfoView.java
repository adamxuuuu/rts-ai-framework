package UI;

import core.game.GameState;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

import static core.Constants.GUI_SIDE_PANEL_WIDTH;
import static core.Constants.INFO_PANEL_HEIGHT;

public class InfoView extends JComponent {

    // Dimensions of the window.
    private final Dimension size;
    private final JEditorPane textArea;

    private GameState gs;

    InfoView() {
        this.size = new Dimension(GUI_SIDE_PANEL_WIDTH, INFO_PANEL_HEIGHT);

        textArea = new JEditorPane("text/html", "");
        textArea.setPreferredSize(new Dimension(GUI_SIDE_PANEL_WIDTH, INFO_PANEL_HEIGHT));
        Font textFont = new Font(textArea.getFont().getName(), Font.PLAIN, 12);
        textArea.setFont(textFont);
        textArea.setEditable(false);
        textArea.setBackground(Color.lightGray);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

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

        this.add(new JTextField("hello"));
    }

    void render(GameState gs) {
        this.gs = gs;
    }

}
