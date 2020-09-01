package player.mc;

import player.heuristics.ParameterSet;

import java.util.ArrayList;
import java.util.Map;

public class MCParams extends ParameterSet {

    public final int ROLLOUT_LENGTH = 10;

    @Override
    public Object getParameterValue(String param) {
        return null;
    }

    @Override
    public void setParameterValue(String param, Object value) {

    }

    @Override
    public ArrayList<String> getParameters() {
        return null;
    }

    @Override
    public Map<String, Object[]> getParameterValues() {
        return null;
    }
}
