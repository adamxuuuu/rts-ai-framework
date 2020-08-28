package player;

import core.action.Action;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAction {

    private final int playerId;
    private final Map<Long, Action> unitActions;
    private final Queue<Action> trainActions;

    public PlayerAction(int playerId) {
        this.playerId = playerId;
        unitActions = new ConcurrentHashMap<>();
        trainActions = new LinkedList<>();
    }

    void addUnitAction(long uId, Action action) {
        unitActions.put(uId, action);
    }

    void addTrainAction(Action action) {
        trainActions.add(action);
    }

    public Map<Long, Action> unitActions() {
        return unitActions;
    }

    public Queue<Action> trainActions() {
        return trainActions;
    }

    public int playerId() {
        return playerId;
    }

    public void reset() {
        unitActions.clear();
        trainActions.clear();
    }

    public boolean empty() {
        return unitActions.isEmpty() && trainActions.isEmpty();
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "unitActions=" + unitActions +
                ", trainActions=" + trainActions +
                '}';
    }
}
