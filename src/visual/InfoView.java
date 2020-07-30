package visual;

import javax.swing.*;
import java.awt.*;

import static core.Constants.GUI_SIDE_PANEL_HEIGHT;
import static core.Constants.GUI_SIDE_PANEL_WIDTH;

public class InfoView extends JComponent {

    // Dimensions of the window.
    private final Dimension size;

    InfoView() {
        this.size = new Dimension(GUI_SIDE_PANEL_WIDTH, GUI_SIDE_PANEL_HEIGHT);
    }

}
