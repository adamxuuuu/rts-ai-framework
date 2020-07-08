package core.actions;

import core.game.GameState;
import core.units.Entity;

public class Attack implements Action {

    private final Entity attacker;
    private final Entity target;

    public Attack(Entity attacker, Entity target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public boolean exec(GameState gs) {
        return false;
    }

    @Override
    public Action copy() {
        return null;
    }
}
