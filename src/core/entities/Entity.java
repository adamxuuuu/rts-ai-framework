package core.entities;

import utils.Vector2d;

public abstract class Entity {

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

    /**
     * Time (second) for this entity to be built
     */
    protected long buildTime;

    /**
     * Method to provide a copy of this actor.
     *
     * @return new copy fo the Actor
     */
    public abstract Entity copy();

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
}
