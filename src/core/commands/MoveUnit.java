package core.commands;

import core.game.GameState;
import core.units.Unit;
import utils.Vector2d;

public class MoveUnit implements Command {

    private final int unitId;
    private Vector2d destination;

    public MoveUnit(int unitId) {
        this.unitId = unitId;
    }

    @Override
    public boolean exec(GameState gs) {
        Unit unit = gs.getUnit(unitId);
        return false;
    }

    @Override
    public Command copy() {
        return null;
    }

    public void setDestination(Vector2d destination) {
        this.destination = destination;
    }
}
