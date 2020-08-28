package player.mcts;


import player.heuristics.ParameterSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MCTSParams extends ParameterSet {

    // Parameters
    public double K = Math.sqrt(2);
    public int ROLLOUT_LENGTH = 10;//10;

    public void setParameterValue(String param, Object value) {
        switch (param) {
            case "K" -> K = (double) value;
            case "ROLLOUT_LENGTH" -> ROLLOUT_LENGTH = (int) value;
            case "heuristic_method" -> heuristic_method = (int) value;
        }
    }

    public Object getParameterValue(String param) {
        return switch (param) {
            case "K" -> K;
            case "ROLLOUT_LENGTH" -> ROLLOUT_LENGTH;
            case "heuristic_method" -> heuristic_method;
            default -> null;
        };
    }

    public ArrayList<String> getParameters() {
        ArrayList<String> paramList = new ArrayList<>();
        paramList.add("K");
        paramList.add("rollout_depth");
        paramList.add("heuristic_method");
        return paramList;
    }

    public Map<String, Object[]> getParameterValues() {
        HashMap<String, Object[]> parameterValues = new HashMap<>();
        parameterValues.put("K", new Double[]{1.0, Math.sqrt(2), 2.0});
        parameterValues.put("rollout_depth", new Integer[]{5, 8, 10, 12, 15});
        parameterValues.put("heuristic_method", new Integer[]{ENTROPY_HEURISTIC, SIMPLE_HEURISTIC, DIFF_HEURISTIC});
        return parameterValues;
    }

}
