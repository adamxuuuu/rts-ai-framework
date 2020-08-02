package core.gameObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Building extends Entity {

    public enum Type {BASE, DEFENCE}

    private Type type;

    Building(Type type) {
        this.type = type;
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

    private void load(JSONObject jo) {
        super.loadBaseProperties(jo);
        this.type = Type.values()[Math.toIntExact((Long) jo.get("type"))];
    }

    @Override
    public Entity copy() {
        Building copy = new Building(type);
        copy.setEntityId(entityId);
        copy.setAgentId(agentId);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        copy.setSpriteKey(spriteKey);
        return copy;
    }

    public Type getType() {
        return type;
    }
}
