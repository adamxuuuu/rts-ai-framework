package player.heuristics;

import core.game.GameState;

public interface StateHeuristic {

    double evaluateState(int playerId, GameState gameState);

    default double evaluateState(GameState oldState, GameState newState) {
        return 0.0;
    }

}
