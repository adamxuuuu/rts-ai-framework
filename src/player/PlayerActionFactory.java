package player;

import core.action.Action;
import core.action.Attack;
import core.action.Harvest;
import core.action.Train;
import core.entity.Unit;
import core.game.GameState;

import java.util.*;

public class PlayerActionFactory {

    private final GameState gs;
    private final int playerId;

    /**
     * Unit id ---- List of actions
     */
    private Map<Long, List<Action>> unitChoices;
    private List<Action> trainChoices;

    public PlayerActionFactory(GameState gs, int playerId) {
        this.gs = gs;
        this.playerId = playerId;

        generate();
    }

    private void generate() {
        unitChoices = new HashMap<>();
        trainChoices = new ArrayList<>();

        for (Unit u : gs.grid().getUnits(playerId)) {
            long uId = u.getEntityId();
            Action currentAct = gs.getUnitAction(uId);
            if (currentAct == null || currentAct instanceof Attack) {
                List<Action> acts = u.calculateActions(gs);
                if (acts.isEmpty()) {
                    continue;
                }
                unitChoices.put(uId, acts);
            }
        }

        trainChoices.addAll(gs.getAllAvailableTrainActions(playerId));
    }

    /**
     * Pure random selection from all possible candidates
     */
    public PlayerAction randomAction(Random rnd) {
        PlayerAction pa = new PlayerAction(playerId);

        addRandomUnitAction(pa, rnd, false);

        addRandomTrainAction(pa, rnd);

        return pa;
    }

    /**
     * If unit actions contain attack or harvest
     * then remove move actions
     */
    public PlayerAction randomBiasedAction(Random rnd) {
        PlayerAction pa = new PlayerAction(playerId);

        addRandomUnitAction(pa, rnd, true);

        addRandomTrainAction(pa, rnd);

        return pa;
    }

    /**
     * @return first non move action if biased
     * or a random action is selected
     */
    private Action selectUnitAction(List<Action> choices, Random rnd, boolean biased) {
        if (biased) {
            for (Action a : choices) {
                if (a instanceof Attack || a instanceof Harvest) {
                    return a;
                }
            }
        }
        return choices.remove(rnd.nextInt(choices.size()));
    }

    private void addRandomTrainAction(PlayerAction pa, Random rnd) {
        if (!trainChoices.isEmpty()) {
            pa.addTrainAction((Train) trainChoices.remove(rnd.nextInt(trainChoices.size())));
        }
    }

    private void addRandomUnitAction(PlayerAction pa, Random rnd, boolean biased) {
        if (!unitChoices.isEmpty()) {
            unitChoices.forEach((k, v) -> {
                if (!v.isEmpty()) {
                    pa.addUnitAction(k, selectUnitAction(v, rnd, biased));
                }
            });
        }
    }

}
