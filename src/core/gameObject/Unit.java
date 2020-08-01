package core.gameObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Unit extends Entity {

    // Properties belong to a unit
    private int rateOfFire; // Time between each shot
    private int attack; // Damage output
    private int speed; // Pixels per frame
    private int range; // Attack range measured in pixel

    private int kills;
    private int veteranLvl;

    public Unit(String name, int speed, long entityId, int agentId) {
        this.entityId = entityId;
        this.agentId = agentId;
        this.name = name;
        this.speed = speed;
    }

    public Unit(String filename, long entityId, int agentId) {
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
        this.range = Math.toIntExact((Long) jo.get("range"));
        this.attack = Math.toIntExact((Long) jo.get("attack"));
        this.speed = Math.toIntExact((Long) jo.get("speed"));
        this.rateOfFire = Math.toIntExact((Long) jo.get("rateOfFire"));
        currentHP = maxHP;
    }

    @Override
    public Entity copy() {
        Unit copy = new Unit(name, speed, entityId, agentId);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        return copy;
    }

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public int getKills() {
        return kills;
    }

    public int getVeteranLvl() {
        return veteranLvl;
    }

    public int getAttack() {
        return attack;
    }

}
