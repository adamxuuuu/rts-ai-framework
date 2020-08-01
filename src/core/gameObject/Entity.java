package core.gameObject;

import org.json.simple.JSONObject;
import util.Vector2d;

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
    protected int maxHP; // Max hit point
    protected int currentHP; // Current hip point

    public void takeDamage(int amount) {
        currentHP -= amount;
    }

    protected void loadBaseProperties(JSONObject jo) {
        this.name = (String) jo.get("name");
        this.cost = Math.toIntExact((Long) jo.get("cost"));
        this.maxHP = Math.toIntExact((Long) jo.get("maxHp"));
        this.buildTime = Math.toIntExact((Long) jo.get("buildTime"));
        currentHP = maxHP;
    }

    /**
     * Method to provide a copy of this actor.
     *
     * @return new copy fo the Actor
     */
    public abstract Entity copy();

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

    public int getCurrentHP() {
        return currentHP;
    }

    public int getCost() {
        return cost;
    }

}
