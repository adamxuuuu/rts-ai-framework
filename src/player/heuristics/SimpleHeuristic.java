package player.heuristics;

import core.entity.Unit;
import core.game.GameState;

public class SimpleHeuristic implements StateHeuristic {

    private static final double RESOURCE_MUL = 10.0;
    private static final double UNIT_MUL = 30.0;

    @Override
    public double evaluateState(int playerId, GameState gameState) {
        return score(playerId, gameState);
    }

    private double score(int playerId, GameState gs) {
        double score = gs.getResource(playerId) * RESOURCE_MUL;

        for (Unit u : gs.grid().getUnits(playerId)) {
            if (u.getType() == Unit.Type.WORKER) {
                score += u.getCarry() * RESOURCE_MUL;
            }
            score += (u.getCost() * u.getCurrentHP()) / (double) u.getMaxHp() * UNIT_MUL;
        }

        return score;
    }

}
