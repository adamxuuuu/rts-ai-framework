package core.entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import util.Vector2d;

import java.io.FileReader;

public abstract class Entity {

    // Unique identifier of each entity
    public static long nextId = 1000;

    /**
     * Unique ID of this entity.
     */
    long entityId = -1;

    /**
     * Id of the player this entity belongs to.
     */
    protected int agentId = -1;

    /**
     * Position of this entity in the grid
     */
    protected Vector2d gridPos;

    /**
     * Position of this entity in the screen
     */
    protected Vector2d screenPos;

    protected String name; // Name of the unit
    protected int cost; // Resource needed to build
    protected long buildTime; // Time (second) for this entity to be built
    protected int maxHp; // Max hit point
    protected int currentHP; // Current hip point
    protected String[] spriteKey;

    protected void loadBaseProperties(JSONObject jo) {
        this.name = (String) jo.get("name");
        this.cost = Math.toIntExact((Long) jo.get("cost"));
        this.maxHp = Math.toIntExact((Long) jo.get("maxHp"));
        this.buildTime = Math.toIntExact((Long) jo.get("buildTime"));
        JSONArray ja = (JSONArray) jo.get("spriteKey");
        spriteKey = new String[ja.size()];
        for (int i = 0; i < ja.size(); i++) {
            spriteKey[i] = (String) ja.get(i);
        }
        currentHP = maxHp;
    }

    //-----------------------------------------public--------------------------------------------//

    /**
     * Method to provide a copy of this actor.
     *
     * @return new copy fo the Actor
     */
    public abstract Entity copy();

    public void copy(Entity copy) {
        copy.setEntityId(entityId);
        copy.setAgentId(agentId);
        copy.setName(name);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        copy.setMaxHp(maxHp);
        copy.setCurrentHP(currentHP);
        copy.setSpriteKey(spriteKey);
        copy.setCost(cost);
        copy.setBuildTime(buildTime);
    }

    public void loadFromJson(String path) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            load((JSONObject) obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void load(JSONObject jo);

    public void takeDamage(int amount) {
        currentHP -= amount;
    }

    public boolean isDamaged() {
        return currentHP < maxHp;
    }

    public boolean died() {
        return currentHP <= 0;
    }

    //----------------------------Getter&Setter----------------------------//

    public long getEntityId() {
        return entityId;
    }
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public int getAgentId() {
        return agentId;
    }
    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public Vector2d getGridPos() {
        return gridPos;
    }
    public void setGridPos(Vector2d gridPos) {
        this.gridPos = gridPos;
    }

    public Vector2d getScreenPos() {
        return screenPos;
    }
    public void setScreenPos(Vector2d screenPos) {
        this.screenPos = screenPos;
    }

    public long getBuildTime() {
        return buildTime;
    }
    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getMaxHp() {
        return maxHp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    public int getCost() {
        return cost;
    }

    public String[] getSpriteKey() {
        return spriteKey;
    }

    public void setSpriteKey(String[] spriteKey) {
        this.spriteKey = spriteKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
