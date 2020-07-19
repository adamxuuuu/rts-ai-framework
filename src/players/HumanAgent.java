package players;

import core.actions.Action;
import core.game.GameState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HumanAgent extends Agent {

    private Map<Long, Action> unitActions;

    public HumanAgent() {
        super(0);
        unitActions = new ConcurrentHashMap<>();
    }

    public HumanAgent(ConcurrentHashMap<Long, Action> unitActions) {
        super(0);
        this.unitActions = new ConcurrentHashMap<>(unitActions);
    }

    public void addAction(long uId, Action candidate) {
        unitActions.put(uId, candidate);
    }

    public void removeAction(long uId) {
        unitActions.remove(uId);
    }

    public Map<Long, Action> getActions() {
        return unitActions;
    }

    @Override
    public Action act(GameState gs) {
        return null;
    }

    public HumanAgent copy() {
        HumanAgent copy = new HumanAgent();
        copy.unitActions = new ConcurrentHashMap<>(unitActions);
        return copy;
    }

    public void reset() {
        unitActions.clear();
    }
}
