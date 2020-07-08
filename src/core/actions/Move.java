package core.actions;

import core.game.GameState;
import core.units.Unit;
import utils.Vector2d;

public class Move implements Action {

    private final long unitId;
    private Vector2d destination;

    public Move(long unitId, Vector2d destination) {
        this.unitId = unitId;
        this.destination = destination;
    }

    @Override
    public boolean exec(GameState gs) {
        Unit unit = (Unit) gs.getUnit(unitId);
        return false;
    }

    @Override
    public Action copy() {
        Move move = new Move(unitId, destination);
        move.setDestination(destination);
        return move;
    }

    public void setDestination(Vector2d destination) {
        this.destination = destination;
    }

    public Vector2d getDestination() {
        return destination;
    }
}
