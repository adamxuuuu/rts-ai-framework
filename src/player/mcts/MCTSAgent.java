/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.mcts;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;

public class MCTSAgent extends Agent {

    MCTSParams mctsParams;

    public MCTSAgent(long seed) {
        super(seed);
        mctsParams = new MCTSParams();
    }

    @Override
    public PlayerAction act(GameState gs) {
        return null;
    }

    @Override
    public Agent copy() {
        return null;
    }
}
