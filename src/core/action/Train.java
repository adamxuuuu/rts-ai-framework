package core.action;

import core.Constants;
import core.entity.Building;
import core.entity.Unit;
import core.game.GameState;

public class Train extends Action {

    // TODO should not pass a <Unit> to the constructor
    private final Unit unit;

    private final int playerId;
    private long buildTime;
    private int costRemain;

    public Train(Unit unit) {
        this.unit = unit;

        playerId = unit.getPlayerId();
        buildTime = unit.getBuildTime() * Constants.SECOND_NANO;
        costRemain = unit.getCost();
    }

    private Train(Unit unit, int playerId, long buildTime, int costRemain) {
        this.unit = unit;
        this.playerId = playerId;
        this.buildTime = buildTime;
        this.costRemain = costRemain;
    }

    @Override
    public void exec(GameState gs, double elapsed) {
        // Check resource first time execute
        if (!gs.checkResource(playerId, unit.getCost()) && costRemain != 0) {
            isComplete = true;
            return;
        } else {
            // Handle cost reduction
            gs.changeResource(playerId, -costRemain);
            costRemain = 0;
        }

        buildTime -= elapsed;
        if (buildTime > 0) {
            // Building in progress
            return;
        }

        Building source = gs.grid().getBuilding(unit.getPlayerId(), Building.Type.BASE);
        if (source == null) {
            // Building got destroyed
            isComplete = true;
            return;
        }
        gs.addUnit(unit, source.getGridPos());

        isComplete = true;
    }

    @Override
    public Action copy() {
        return new Train(unit, playerId, buildTime, costRemain);
    }

    @Override
    public String toString() {
        return "Train{" +
                "unit=" + unit +
                ", playerId=" + playerId +
                '}';
    }
}
