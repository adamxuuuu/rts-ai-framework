package core.actions;

import core.game.GameState;
import core.units.Building;
import utils.Vector2d;

public class Build implements Action {

    private final Building building;
    private final Vector2d gridPos;

    public Build(Building building, Vector2d gridPos) {
        this.building = building;
        this.gridPos = gridPos;
    }

    @Override
    public boolean exec(GameState gs) {
        return false;
    }

    @Override
    public Action copy() {
        return null;
    }
}
