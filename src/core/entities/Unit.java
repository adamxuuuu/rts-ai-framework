package core.entities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Unit extends Entity {

    // Properties should be read from file
    private String name; // Name of the unit
    private int speed; // Pixels per frame
    private int range; // Attack range measured in pixel
    private int cost; // Resource needed to build
    private int maxHP; // Max hit point
    private int currentHP;

    private int kills;
    private int veteranLvl;

    // Testing constructor
    public Unit(String name, int speed, long buildTime, long entityId, int agentId) {
        this.entityId = entityId;
        this.agentId = agentId;
        this.name = name;
        this.speed = speed;
        this.buildTime = buildTime;
    }

    public Unit(String filename, long entityId, int agentId) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            initFromJson((JSONObject) obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.entityId = entityId;
        this.agentId = agentId;
    }

    private void initFromJson(JSONObject unit) {
        this.name = (String) unit.get("name");
        this.speed = Math.toIntExact((Long) unit.get("speed"));
        this.range = Math.toIntExact((Long) unit.get("range"));
        this.cost = Math.toIntExact((Long) unit.get("cost"));
        this.maxHP = Math.toIntExact((Long) unit.get("maxHp"));
        currentHP = maxHP;
        this.buildTime = Math.toIntExact((Long) unit.get("buildTime"));
    }

    @Override
    public Entity copy() {
        Unit copy = new Unit(name, speed, buildTime, entityId, agentId);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        return copy;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", position=" + gridPos +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public float getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getVeteranLvl() {
        return veteranLvl;
    }

    public void setVeteranLvl(int veteranLvl) {
        this.veteranLvl = veteranLvl;
    }
}
