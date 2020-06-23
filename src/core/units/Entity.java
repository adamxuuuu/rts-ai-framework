package core.units;

import utils.Vector2d;

public abstract class Entity {

    /**
     * Unique ID of this entity.
     */
    int entityId = -1;

    /**
     * Id of the tribe this actor belongs to.
     */
    protected int commanderId = -1;

    /**
     * Position of this actor in the board
     */
    protected Vector2d position;

    /**
     * Method to provide a copy of this actor.
     *
     * @param hideInfo indicates if information of this actor should be copied or hidden for
     *                 partial observability.
     * @return new copy fo the Actor
     */
    public abstract Entity copy(boolean hideInfo);

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getCommanderId() {
        return commanderId;
    }

    public void setCommanderId(int commanderId) {
        this.commanderId = commanderId;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }
}
