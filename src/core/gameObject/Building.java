package core.gameObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Building extends Entity {

    public enum BuildingType {BASE, DEFENCE}

    private BuildingType bt;

    Building(BuildingType bt) {
        this.bt = bt;
    }

    public Building(String filename, long entityId, int agentId) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            load((JSONObject) obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.entityId = entityId;
        this.agentId = agentId;
    }

    public BuildingType getBt() {
        return bt;
    }

    private void load(JSONObject jo) {
        super.loadBaseProperties(jo);
        this.bt = BuildingType.values()[Math.toIntExact((Long) jo.get("type"))];
    }

    @Override
    public Entity copy() {
        Building copy = new Building(bt);
        copy.setEntityId(entityId);
        copy.setAgentId(agentId);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        return copy;
    }
}
