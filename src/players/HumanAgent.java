package players;

import core.actions.Action;
import core.game.GameState;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class HumanAgent extends Agent {

    private Map<Long, Action> unitActions;
    private Queue<Action> buildActions;

    public HumanAgent() {
        super(0);
        unitActions = new ConcurrentHashMap<>();
        buildActions = new LinkedList<>();
    }

    public void addUnitAct(long uId, Action candidate) {
        unitActions.put(uId, candidate);
    }

    public void addBuildAct(Action candidate) {
        buildActions.add(candidate);
    }

    public Map<Long, Action> getUnitActs() {
        return unitActions;
    }

    public Action getBuildAct() {
        return buildActions.peek();
    }

    public void pop() {
        buildActions.remove();
    }

    @Override
    public Action act(GameState gs) {
        return null;
    }

    public HumanAgent copy() {
        HumanAgent copy = new HumanAgent();
        copy.unitActions = new ConcurrentHashMap<>(unitActions);
        copy.buildActions = new LinkedList<>(buildActions);
        return copy;
    }

}
