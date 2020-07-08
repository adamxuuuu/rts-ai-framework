package core.game;

import core.units.Entity;
import core.units.Unit;
import utils.Vector2d;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grid {

    // Dimension
    private int height, width;

    // Height map
    private float[][] heightMap;

    // Units
    private final Map<Long, Unit> entities = new HashMap<>();

    public Grid() {
    }

    public Grid(int height, int width) {
        this.height = height;
        this.width = width;

        heightMap = new float[height][width];
        randomMap();
    }

    /**
     * Add a {@link Unit} to the map
     *
     * @param addUnit unit
     */
    public void addUnit(Unit addUnit) {
//        for (Unit unit : entities.values()) {
//            if (unit.getGridPos().equals(addUnit.getGridPos())) {
//                return;
//            }
//        }
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
        copyGrid.height = this.height;
        copyGrid.width = this.width;
        copyGrid.heightMap = new float[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                heightMap[i][j] = new Random().nextFloat();
            }
        }
    }

    public Map<Long, Unit> getEntities() {
        return entities;
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getHeightAt(int x, int y) {
        return heightMap[x][y];
    }

    /**
     * Move a unit in x/y direction
     *
     * @param u  {@link Unit} to move
     * @param dx movement in x
     * @param dy movement in y
     */
    public void moveUnit(Unit u, int dx, int dy) {
        Vector2d p = u.getScreenPos();
        u.setScreenPos(new Vector2d(p.x + dx, p.y + dy));
    }
}
