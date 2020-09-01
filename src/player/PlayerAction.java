package player;

import core.action.Action;
import core.action.Train;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAction {

    private final int playerId;
    private Map<Long, Action> unitActions;
    private Train trainAction;

    public PlayerAction(int playerId) {
        this.playerId = playerId;
        unitActions = new ConcurrentHashMap<>();
        trainAction = null;
    }

    void addUnitAction(long uId, Action action) {
        unitActions.put(uId, action);
    }

    void addTrainAction(Train action) {
        trainAction = action;
    }

    public Map<Long, Action> unitActions() {
        return unitActions;
    }

    public Train trainAction() {
        return trainAction;
    }

    public int playerId() {
        return playerId;
    }

    public void reset() {
        unitActions.clear();
        trainAction = null;
    }

    public boolean empty() {
        return unitActions.isEmpty() && trainAction == null;
    }

    public PlayerAction copy() {
        PlayerAction copy = new PlayerAction(playerId);

        copy.unitActions = new ConcurrentHashMap<>();
        for (Map.Entry<Long, Action> entry : unitActions.entrySet()) {
            copy.unitActions.put(entry.getKey(), entry.getValue().copy());
        }

        copy.trainAction = trainAction == null ? null : (Train) trainAction.copy();

        return copy;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "unitActions=" + unitActions +
                ", trainAction=" + trainAction +
                '}';
    }
}
