package UI;

import javax.swing.*;
import java.awt.*;

import static core.Constants.GUI_SIDE_PANEL_WIDTH;
import static core.Constants.INFO_PANEL_HEIGHT;

public class InfoView extends JComponent {

    // Dimensions of the window.
    private final Dimension size;

    InfoView() {
        this.size = new Dimension(GUI_SIDE_PANEL_WIDTH, INFO_PANEL_HEIGHT);
    }

}
