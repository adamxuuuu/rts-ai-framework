package core.game;

import UI.GameView;
import core.Constants;
import core.entity.Building;
import core.entity.Entity;
import core.entity.Resource;
import core.entity.Unit;
import util.Utils;
import util.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

public class Grid {

    /**
     * Terrain type
     */
    public enum TerrainType {WATER, LAND, MOUNTAIN}

    /**
     * Size of the grid map
     */
    private int size;

    /**
     * Height value for each cell in the grid
     */
    private float[][] heightMap;

    /**
     * Terrain type calculated based on height value
     */
    private TerrainType[][] terrain;
    private Building[][] buildings;
    private Resource[][] resources;

    /**
     * All units
     */
    private final Map<Long, Unit> unitMap = new HashMap<>();

    /**
     * All entities
     */
    private final Map<Long, Entity> entityMap = new HashMap<>();

    private Grid() {
    }

    Grid(int size) {
        this.size = size;

        heightMap = new float[size][size];
        terrain = new TerrainType[size][size];
        buildings = new Building[size][size];
        resources = new Resource[size][size];

        //---------- loading ----------//
        randomMap();
        generateTerrain();
        initResources();
    }

    public Collection<Entity> entities() {
        return entityMap.values();
    }

    public Entity getEntity(long id) {
        return entityMap.get(id);
    }

    public Resource getResourceAt(int x, int y) {
        return resources[x][y];
    }

    public Resource findNearestResource(Vector2d p) {
        for (int radius = 1; radius < 5; radius++) {
            for (Vector2d pos : p.neighborhood(radius, 0, size, true)) {
                Resource res = getResourceAt(pos.x, pos.y);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    public Entity getEnemyAt(int playerId, Vector2d gp) {
        Building b = getBuildingAt(gp.x, gp.y);
        if (b != null && b.getAgentId() != playerId) {
            return b;
        }

        Optional<Unit> enemy = units().stream().filter(u -> u.getGridPos().equals(gp) && u.getAgentId() != playerId).findFirst();
        return enemy.orElse(null);
    }

    public ArrayList<Entity> getEnemyInRange(Unit unit) {
        ArrayList<Entity> enemies = entities().stream().filter(e ->
                !(e instanceof Resource) &&
                        e.getAgentId() != unit.getAgentId() &&
                        Vector2d.euclideanDistance(unit.getGridPos(), e.getGridPos()) < unit.getRange())
                .collect(Collectors.toCollection(ArrayList::new));


        return enemies;
    }

    void addUnit(Unit addUnit) {
        Vector2d gp = addUnit.getGridPos();
        if (!accessible(gp.x, gp.y) && occupied(gp.x, gp.y)) {
            return;
        }

        unitMap.put(addUnit.getEntityId(), addUnit);
        entityMap.put(addUnit.getEntityId(), addUnit);
    }

    public void removeEntity(Entity e) {
        long id = e.getEntityId();
        Vector2d gp = e.getGridPos();
        if (e instanceof Unit) {
            unitMap.remove(id);
        } else if (e instanceof Building) {
            buildings[gp.x][gp.y] = null;
        } else if (e instanceof Resource) {
            resources[gp.x][gp.y] = null;
        }
        entityMap.remove(id);
    }

    /**
     * Find valid position near give grid position
     *
     * @param gp   grid position
     * @param need how many location needed
     * @return a list of position
     */
    public LinkedList<Vector2d> allocateNearby(Vector2d gp, int need) {
        LinkedList<Vector2d> res = new LinkedList<>();
        if (need == 1) {
            return res;
        }
        int find = 0;
        int startRange = 1;
        while (find < need) {
            for (Vector2d pos : gp.neighborhood(startRange, 0, size, true)) {
                if (accessible(pos.x, pos.y)) {
                    res.add(pos);
                    find++;
                    if (need - find == 1) {
                        return res;
                    }
                }
            }
            find = 0;
            startRange++;
            res.clear();
        }
        return res;
    }

    /**
     * Find first valid location near given grid position
     *
     * @param gp       grid position
     * @param maxRange how far you want to search
     * @return the first find
     */
    public Vector2d findFirstNearby(Vector2d gp, int maxRange) {
        for (int radius = 1; radius < maxRange; radius++) {
            for (Vector2d pos : gp.neighborhood(radius, 0, size, true)) {
                if (accessible(pos.x, pos.y) && !occupied(pos.x, pos.y)) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Find all valid position near gp within a range
     *
     * @param gp     grip position
     * @param radius search range
     * @return list of position
     */
    public ArrayList<Vector2d> findAllNearby(Vector2d gp, int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("Range cannot be zero");
        }
        ArrayList<Vector2d> res = new ArrayList<>();
        for (Vector2d pos : gp.neighborhood(radius, 0, size, true)) {
            if (accessible(pos.x, pos.y) && !occupied(pos.x, pos.y)) {
                res.add(pos);
            }
        }
        return res;
    }

    public Building getBuildingAt(int x, int y) {
        return buildings[x][y];
    }

    /**
     * @param playerId
     * @param bt       {@link Building.Type}
     * @return
     */
    public Building getBuilding(long playerId, Building.Type bt) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Building b = getBuildingAt(i, j);
                if (b != null && b.getType() == bt && b.getAgentId() == playerId) {
                    return b;
                }
            }
        }
        return null;
    }

    boolean addBuilding(Building b) {
        Vector2d gp = b.getGridPos();
        if (!accessible(gp.x, gp.y) || buildings[gp.x][gp.y] != null) {
            return false;
        }

        buildings[gp.x][gp.y] = b;
        entityMap.put(b.getEntityId(), b);
        return true;
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
        copyGrid.resources = new Resource[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copyGrid.heightMap[i][j] = this.heightMap[i][j];
                copyGrid.terrain[i][j] = this.terrain[i][j];
                copyGrid.buildings[i][j] = this.buildings[i][j];
                copyGrid.resources[i][j] = this.resources[i][j];
            }
        }

        for (Entity e : entityMap.values()) {
            Entity copy = e.copy();
            if (copy instanceof Unit) {
                copyGrid.unitMap.put(copy.getEntityId(), (Unit) copy);
            }
            copyGrid.entityMap.put(copy.getEntityId(), copy);
        }

        return copyGrid;
    }

    public Vector2d randomPos(Random rnd) {
        int x = rnd.nextInt(size);
        int y = rnd.nextInt(size);
        return new Vector2d(x, y);
    }

    /**
     * Randomly generating height between -0.1 - 1
     */
    private void randomMap() {
        int offset = size / 6;
        for (int i = offset; i < size - offset; i++) {
            for (int j = offset; j < size - offset; j++) {
                heightMap[i][j] = Utils.nextFloatBetween((float) -0.1, 1);
            }
        }
    }

    /**
     * Generate Terrain based on height map
     */
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

    /**
     * Generate Resources (using default location)
     */
    private void initResources() {
        for (Vector2d loc : Constants.RESOURCE_LOCATION) {
            Resource resource = new Resource(Resource.Type.RICH);
            resource.setGridPos(loc);
            resources[loc.x][loc.y] = resource;
            entityMap.put(resource.getEntityId(), resource);
        }
    }

    /**
     * Get all unit belong to a player
     *
     * @param playerId player id
     * @return all the units or am empty list
     */
    public List<Unit> getUnits(int playerId) {
        return unitMap.values().stream().filter(unit -> unit.getAgentId() == playerId).collect(Collectors.toList());
    }

    public Collection<Unit> units() {
        return unitMap.values();
    }

    public Set<Long> unitIds() {
        return unitMap.keySet();
    }

    /**
     * Select a single unit with player id
     * and screen position with error of half the cell size
     *
     * @param playerId player id
     * @param sp       screen position
     * @return the first unit or -1 if not unit is find or
     */
    public long selectUnitId(int playerId, Vector2d sp) {
        for (Unit u : unitMap.values()) {
            if (playerId != u.getAgentId()) {
                continue;
            }
            if (u.getScreenPos().equalsPlusError(sp, Constants.CELL_SIZE / 2.0)) {
                return u.getEntityId();
            }
        }
        return -1;
    }

    /**
     * Select multiple unit with a bounding box and player id
     *
     * @param playerId player id
     * @param start    top left (bottom right) of the bounding box
     * @param end      bottom right (top left) of the bounding box
     * @return a list of units or a empty list is not unit is selected
     */
    public ArrayList<Long> selectUnitIds(int playerId, Vector2d start, Vector2d end) {
        ArrayList<Long> res = new ArrayList<>();
        for (Unit u : unitMap.values()) {
            if (playerId != u.getAgentId()) {
                continue;
            }
            Vector2d sp = u.getScreenPos();
            if ((sp.greater(start) && sp.less(end))
                    || (sp.less(start) && sp.greater(end))) {
                res.add(u.getEntityId());
            }
        }
        return res;
    }

    /**
     * Get the unit with specific id
     *
     * @param unitId id
     * @return the entity or null if the entity doesn't exist
     */
    public Unit getUnit(long unitId) {
        return unitMap.get(unitId);
    }

    /**
     * Check if a position is movable (Land)
     *
     * @param x X position
     * @param y Y position
     * @return true if the position is movable or the opposite
     */
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
        for (Entity e : unitMap.values()) {
            if (e.getGridPos().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update a unit grid position given screen position
     *
     * @param u  unit
     * @param sp screen position
     */
    public void updateGridPos(Unit u, Vector2d sp) {
        u.setScreenPos(sp);
        u.setGridPos(GameView.screenToGrid(sp));
    }

    /**
     * Update a unit screen position given grid position
     *
     * @param u  unit
     * @param gp grid position
     */
    public void updateScreenPos(Unit u, Vector2d gp) {
        u.setGridPos(gp);
        u.setScreenPos(GameView.gridToScreen(gp));
    }

    public float getHeightAt(int x, int y) {
        return heightMap[x][y];
    }

    public TerrainType getTerrainAt(int x, int y) {
        return terrain[x][y];
    }

    public int size() {
        return size;
    }
}
