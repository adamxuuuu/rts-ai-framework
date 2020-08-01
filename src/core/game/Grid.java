package core.game;

import UI.GameView;
import core.Constants;
import core.gameObject.Building;
import core.gameObject.Entity;
import core.gameObject.Unit;
import util.Utils;
import util.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Grid {


    public enum TerrainType {WATER, LAND, MOUNTAIN}

    /**
     * Size of the grid map
     */
    private int size;

    /**
     * Height value for each cell in the grid
     */
    private float[][] heightMap;

    private TerrainType[][] terrain;

    private Building[][] buildings;

    /**
     * All units on the grid
     */
    private final Map<Long, Unit> units = new HashMap<>();
    private final Map<Long, Entity> allEntities = new HashMap<>();

    public Grid() {
    }

    public Grid(int size) {
        this.size = size;

        heightMap = new float[size][size];
        terrain = new TerrainType[size][size];
        buildings = new Building[size][size];

        //---------- loading ----------//
        randomMap();
        generateTerrain();
    }

    public Entity getEntity(long id) {
        return allEntities.get(id);
    }

    public Entity getEnemyAt(int playerID, Vector2d gp) {
        Building b = getBuildingAt(gp.x, gp.y);
        if (b == null) {
            return null;
        }

        if (b.getAgentId() != playerID) {
            return b;
        }

        Optional<Unit> enemy = units.values().stream().filter(u -> u.getGridPos().equals(gp) && u.getAgentId() != playerID).findFirst();
        return enemy.orElse(null);
    }

    /**
     * Add a {@link Unit} to the map
     *
     * @param addUnit unit
     */
    void addUnit(Unit addUnit) {
        Vector2d gp = addUnit.getGridPos();
        if (!accessible(gp.x, gp.y) && occupied(gp.x, gp.y)) {
            return;
        }

        units.put(addUnit.getEntityId(), addUnit);
        allEntities.put(addUnit.getEntityId(), addUnit);
    }

    /**
     * @param e {@link Entity} to be removed
     */
    public void removeEntity(Entity e) {
        if (e instanceof Unit) {
            units.remove(e.getEntityId());
        } else if (e instanceof Building) {
            Vector2d gp = e.getGridPos();
            buildings[gp.x][gp.y] = null;
        }
        allEntities.remove(e.getEntityId());
    }


    public Vector2d findNearby(Vector2d gp, int maxRange) {
        for (int radius = 1; radius < maxRange; radius++) {
            for (Vector2d pos : gp.neighborhood(radius, 0, size, true)) {
                if (accessible(pos.x, pos.y) && !occupied(pos.x, pos.y)) {
                    return pos;
                }
            }
        }
        return null;
    }

    public Building getBuildingAt(int x, int y) {
        return buildings[x][y];
    }

    boolean addBuilding(Building b) {
        Vector2d gp = b.getGridPos();
        if (!accessible(gp.x, gp.y) || buildings[gp.x][gp.y] != null) {
            return false;
        }

        buildings[gp.x][gp.y] = b;
        allEntities.put(b.getEntityId(), b);
        return true;
    }

    /**
     * @param agentId owner
     * @param bt      {@link core.gameObject.Building.BuildingType}
     * @return the {@link Building}
     */
    public Building getBuilding(long agentId, Building.BuildingType bt) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Building b = getBuildingAt(i, j);
                if (b != null && b.getBt() == bt && b.getAgentId() == agentId) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * @return a deep copy of the grid
     */
    public Grid copy() {
        Grid copyGrid = new Grid();
        copyGrid.size = this.size;
        copyGrid.heightMap = new float[size][size];
        copyGrid.terrain = new TerrainType[size][size];
        copyGrid.buildings = new Building[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copyGrid.heightMap[i][j] = this.heightMap[i][j];
                copyGrid.terrain[i][j] = this.terrain[i][j];
                copyGrid.buildings[i][j] = this.buildings[i][j];
            }
        }

        for (Unit u : units.values()) {
            Unit copy = (Unit) u.copy();
            copyGrid.units.put(copy.getEntityId(), copy);
        }

        return copyGrid;
    }

    /**
     * Randomly generating height between 0.0 - 0.1
     */
    private void randomMap() {
        int offset = size / 6;
        for (int i = offset; i < size - offset; i++) {
            for (int j = offset; j < size - offset; j++) {
                heightMap[i][j] = Utils.nextFloatBetween((float) -0.1, 1);
            }
        }
    }

    private void generateTerrain() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float h = getHeightAt(i, j);
                if (h < Constants.WATER_LVL) {
                    terrain[i][j] = TerrainType.WATER;
                } else if (h < Constants.LAND_LVL) {
                    terrain[i][j] = TerrainType.LAND;
                } else {
                    terrain[i][j] = TerrainType.MOUNTAIN;
                }
            }
        }
    }

    public Map<Long, Unit> getUnits() {
        return units;
    }

    /**
     * Single unit selection
     */
    public Long selectUnitId(Vector2d sp) {
        for (Unit u : units.values()) {
            if (u.getScreenPos().equalsPlusError(sp, Constants.CELL_SIZE / 2.0)) {
                return u.getEntityId();
            }
        }
        return null;
    }

    /**
     * Bounding box selection
     */
    public ArrayList<Long> selectUnitIds(Vector2d start, Vector2d end) {
        ArrayList<Long> res = new ArrayList<>();
        for (Unit u : units.values()) {
            Vector2d sp = u.getScreenPos();
            if ((sp.greater(start) && sp.less(end))
                    || (sp.less(start) && sp.greater(end))) {
                res.add(u.getEntityId());
            }
        }
        return res;
    }

    /**
     * Get the game entity with Id
     *
     * @param eId entity Id
     * @return the entity or null if the entity doesn't exist
     */
    public Unit getUnit(long eId) {
        return units.get(eId);
    }

    public int size() {
        return size;
    }

    public float getHeightAt(int x, int y) {
        return heightMap[x][y];
    }

    public TerrainType getTerrainAt(int x, int y) {
        return terrain[x][y];
    }

    public boolean accessible(int x, int y) {
        float h = getHeightAt(x, y);
        return h >= Constants.WATER_LVL &&
                h <= Constants.LAND_LVL;
    }

    public boolean occupied(int x, int y) {
        if (getBuildingAt(x, y) != null) {
            return true;
        }
        Vector2d pos = new Vector2d(x, y);
        for (Entity e : units.values()) {
            if (e.getGridPos().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update a unit grid position given screen position
     */
    public void updateGridPos(Unit u, Vector2d newScreenPos) {
        u.setScreenPos(newScreenPos);
        u.setGridPos(GameView.screenToGrid(newScreenPos));
    }

    /**
     * Update a unit screen position given grid position
     */
    public void updateScreenPos(Unit u, Vector2d newGridPos) {
        u.setGridPos(newGridPos);
        u.setScreenPos(GameView.gridToScreen(newGridPos));
    }
}
