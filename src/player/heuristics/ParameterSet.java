package player.heuristics;

import java.util.ArrayList;
import java.util.Map;

public abstract class ParameterSet {
    public final int STOP_TIME = 0;
    public final int STOP_ITERATIONS = 1;
    public final int STOP_FMCALLS = 2;

    public final int ENTROPY_HEURISTIC = 0;
    public final int SIMPLE_HEURISTIC = 1;
    public final int DIFF_HEURISTIC = 2;
    public int heuristic_method = SIMPLE_HEURISTIC;

    public double epsilon = 1e-6;

    // Budget settings
    public int stop_type = STOP_TIME;
    public int num_iterations = 200;
    public int num_fmcalls = 100;
    public long num_time = 1;
    public final int MAX_CHILDREN = 200;


    public abstract Object getParameterValue(String param);

    public abstract void setParameterValue(String param, Object value);

    public abstract ArrayList<String> getParameters();

    public abstract Map<String, Object[]> getParameterValues();

    public StateHeuristic getHeuristic() {
        if (heuristic_method == SIMPLE_HEURISTIC) return new SimpleHeuristic();
        return null;
    }

}
