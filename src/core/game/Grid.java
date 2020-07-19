package core.game;

import core.Constants;
import core.units.Entity;
import core.units.Unit;
import utils.Vector2d;
import visual.GameView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grid {

    /**
     * Size of the grid map
     */
    private int size;

    /**
     * Stores the height value for each cell in the grid
     */
    private float[][] heightMap;

    /**
     * All entities on the grid
     */
    private final Map<Long, Unit> entities = new HashMap<>();

    public Grid() {
    }

    public Grid(int size) {
        this.size = size;

        heightMap = new float[size][size];
        randomMap();
    }

    /**
     * Add a {@link Unit} to the map
     *
     * @param addUnit unit
     */
    public void addUnit(Unit addUnit) {
        for (Unit unit : entities.values()) {
            if (unit.getScreenPos().equals(addUnit.getScreenPos())) {
                return;
            }
        }
        entities.put(addUnit.getEntityId(), addUnit);
    }

    /**
     * Removes a {@link Unit} from the map
     *
     * @param u unit
     */
    public void removeUnit(Unit u) {
        entities.remove(u.getEntityId());
    }

    public Grid copy() {
        Grid copyGrid = new Grid();
        copyGrid.size = this.size;
        copyGrid.heightMap = new float[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copyGrid.heightMap[i][j] = this.heightMap[i][j];
            }
        }

        for (Unit u : entities.values()) {
            Unit copyUnit = (Unit) u.copy();
            copyGrid.entities.put(copyUnit.getEntityId(), copyUnit);
        }

        return copyGrid;
    }

    /**
     * Randomly generating height between 0.0 - 0.1
     */
    private void randomMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                heightMap[i][j] = new Random().nextFloat();
            }
        }
    }

    public Map<Long, Unit> getEntities() {
        return entities;
    }

    public float[][] getHeightMap() {
        return heightMap;
    }

    /**
     * Get the game entity with Id
     *
     * @param eId entity Id
     * @return the entity or null if the entity doesn't exist
     */
    public Entity getEntity(long eId) {
        return entities.get(eId);
    }

    public int getSize() {
        return size;
    }

    public float getHeightAt(int x, int y) {
        return heightMap[x][y];
    }

    public boolean accessible(int x, int y) {
        float h = getHeightAt(x, y);
        return h >= Constants.SEA_LVL &&
                h <= Constants.GRD_LVL;
    }

    /**
     * Move a unit to a grid position (centre)
     *
     * @param u  {@link Unit} to move
     * @param dt movement along x and y direction
     */
    public void moveUnit(Unit u, Vector2d dt) {
        u.setScreenPos(u.getScreenPos().add(dt));
        u.setGridPos(GameView.screenToGrid(u.getScreenPos()));
    }
}
