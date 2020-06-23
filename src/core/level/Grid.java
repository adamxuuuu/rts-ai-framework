package core.level;

import core.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {

    // Dimension
    private int height, width;

    // Height map
    private float[][] heightMap;

    // Units
    private final List<Unit> allUnits = new ArrayList<>();

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
     * @return true if adding is successful
     */
    public void addUnit(Unit addUnit) {
        for (Unit unit : allUnits) {
            if (unit.getPosition().equals(addUnit.getPosition())) {
                return;
            }
        }
        allUnits.add(addUnit);
    }

    /**
     * Removes a {@link Unit} from the map
     *
     * @param u unit
     */
    public void removeUnit(Unit u) {
        allUnits.remove(u);
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

        for (Unit u : allUnits) {
            copyGrid.allUnits.add((Unit) u.copy(false));
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getHeightAt(int x, int y) {
        return heightMap[x][y];
    }

    public List<Unit> getAllUnits() {
        return allUnits;
    }
}
