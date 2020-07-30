package core.entities;

public class Building extends Entity {

    public enum BuildingType {BASE, DEFENCE}

    private int maxHp;
    private final BuildingType bt;

    public Building(BuildingType bt) {
        this.bt = bt;
    }

    public BuildingType getBt() {
        return bt;
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
