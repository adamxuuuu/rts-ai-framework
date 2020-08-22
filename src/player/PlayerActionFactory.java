package player;

import core.action.Action;
import core.action.Attack;
import core.action.Harvest;
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
    private List<Action> buildChoices;

    public PlayerActionFactory(GameState gs, int playerId) {
        this.gs = gs;
        this.playerId = playerId;

        generate();
    }

    public void generate() {
        unitChoices = new HashMap<>();
        buildChoices = new ArrayList<>();

        for (Unit u : gs.getGrid().getUnits(playerId)) {
            long uId = u.getEntityId();
            if (gs.getUnitAction(uId) == null) {
                List<Action> acts = u.calculateActions(gs);
                unitChoices.put(uId, acts);
            }
        }

        buildChoices.addAll(gs.calBuildActions(playerId));
    }

    /**
     * Pure random selection from all possible candidates
     */
    public PlayerAction randomAction(Random rnd) {
        PlayerAction pa = new PlayerAction(playerId);

        if (!unitChoices.isEmpty()) {
            unitChoices.forEach((k, v) -> pa.addUnitAction(k, v.get(rnd.nextInt(v.size()))));
        }

        randomBuildAction(pa, rnd);

        return pa;
    }

    /**
     * If unit actions contain attack or harvest
     * then remove move actions
     */
    public PlayerAction randomBiasedAction(Random rnd) {
        PlayerAction pa = new PlayerAction(playerId);

        if (!unitChoices.isEmpty()) {
            unitChoices.forEach((k, v) -> pa.addUnitAction(k, biasedUnitAction(v, rnd)));
        }

        randomBuildAction(pa, rnd);

        return pa;
    }

    /**
     * @return first non move action if has any
     * or a random action is selected
     */
    private Action biasedUnitAction(List<Action> choices, Random rnd) {
        for (Action a : choices) {
            if (a instanceof Attack || a instanceof Harvest) {
                return a;
            }
        }
        return choices.get(rnd.nextInt(choices.size()));
    }

    private void randomBuildAction(PlayerAction pa, Random rnd) {
        if (!buildChoices.isEmpty()) {
            pa.addBuildAction(buildChoices.get(rnd.nextInt(buildChoices.size())));
        }
    }

}
