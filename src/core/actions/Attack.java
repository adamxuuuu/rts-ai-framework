package core.actions;

import core.entities.Entity;
import core.game.GameState;

public class Attack implements Action {

    private final Entity attacker;
    private final Entity target;

    public Attack(Entity attacker, Entity target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
