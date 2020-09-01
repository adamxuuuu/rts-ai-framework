/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.mcts;

import core.game.GameState;
import player.Agent;
import player.PlayerAction;

import java.util.List;
import java.util.Random;

public class MCTSAgent extends Agent {

    private final MCTSParams params;
    private final Random rnd;

    public MCTSAgent(long seed) {
        super(seed);
        rnd = new Random(seed);
        params = new MCTSParams();
    }

    @Override
    public PlayerAction act(GameState gs) {

        List<PlayerAction> rootActions = gs.sample(playerId, params.MAX_CHILDREN, rnd);

        MCTSNode m_root = new MCTSNode(params, rnd, rootActions.size(), rootActions, playerId);
        m_root.setRootGameState(m_root, gs);

        m_root.search();

        return rootActions.get(m_root.mostVisitedAction());
    }

    @Override
    public Agent copy() {
        return null;
    }
}
