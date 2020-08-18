package UI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter {

    private final GameView gv;

    KeyController(GameView gv) {
        this.gv = gv;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
        gv.toggleGrid();
    }
}
