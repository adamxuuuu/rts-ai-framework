package core.entity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class EntityFactory {

    private static EntityFactory single_instance = null;

    private static final String UNIT_TEMPLATES_PATH = "./resources/unit/";
    private static final String BUILDING_TEMPLATES_PATH = "./resources/building/";

    public Map<String, Unit> unitTable;
    public Map<String, Building> buildingTable;

    public static EntityFactory getInstance() {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new EntityFactory();
        }
        return single_instance;
    }

    private EntityFactory() {
        unitTable = new HashMap<>();
        buildingTable = new HashMap<>();
        loadUnitTemplates();
        loadBuildingTemplates();
    }

    public Unit getUnit(String name, int playerId) {
        Unit unit = (Unit) unitTable.get(name).copy();
        unit.setAgentId(playerId);
        unit.setEntityId(Entity.nextId++);
        return unit;
    }

    public Building getBuilding(String name, int playerId) {
        Building building = (Building) buildingTable.get(name).copy();
        building.setAgentId(playerId);
        building.setEntityId(Entity.nextId++);
        return building;
    }

    private void loadUnitTemplates() {
        File folder = new File(UNIT_TEMPLATES_PATH);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles != null ? listOfFiles : new File[0]) {
            if (file.isFile()) {
                String filename = file.getName();
                String unitName = filename.substring(0, filename.lastIndexOf('.'));
                unitTable.put(unitName, new Unit(UNIT_TEMPLATES_PATH + filename));
            }
        }
    }

    private void loadBuildingTemplates() {
        File folder = new File(BUILDING_TEMPLATES_PATH);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles != null ? listOfFiles : new File[0]) {
            if (file.isFile()) {
                String filename = file.getName();
                String unitName = filename.substring(0, filename.lastIndexOf('.'));
                buildingTable.put(unitName, new Building(BUILDING_TEMPLATES_PATH + filename));
            }
        }
    }

    public int maxCost() {
        OptionalInt max = unitTable.values().stream().mapToInt(Entity::getCost).max();
        return max.orElse(0);
    }
}
