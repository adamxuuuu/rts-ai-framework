package player;

import core.action.Action;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAction {

    private final int playerId;
    /**
     * One {@link Action} to one {@link core.entity.Unit}
     */
    private final Map<Long, Action> unitActions;
    private final Queue<Action> buildActions;

    PlayerAction(int playerId) {
        this.playerId = playerId;
        unitActions = new ConcurrentHashMap<>();
        buildActions = new LinkedList<>();
    }

    void addUnitAction(long uId, Action action) {
        unitActions.put(uId, action);
    }

    void addBuildAction(Action candidate) {
        buildActions.add(candidate);
    }

    public Map<Long, Action> unitActions() {
        return unitActions;
    }

    public Queue<Action> buildActions() {
        return buildActions;
    }

    public int playerId() {
        return playerId;
    }
}
