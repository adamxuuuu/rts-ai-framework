package core.entity;

import org.json.simple.JSONObject;

public class Building extends Entity {

    public enum Type {BASE, DEFENCE}

    private Type type;

    public Building(String filename) {
        loadFromJson(filename);
    }

    private Building(Type type) {
        this.type = type;
    }

    @Override
    protected void load(JSONObject jo) {
        super.loadBaseProperties(jo);
        this.type = Type.values()[Math.toIntExact((Long) jo.get("type"))];
    }

    @Override
    public Entity copy() {
        Building copy = new Building(type);
        copy.setEntityId(entityId);
        copy.setPlayerId(playerId);
        copy.setGridPos(gridPos);
        copy.setSpriteKey(spriteKey);
        copy.setCurrentHP(currentHP);
        copy.setMaxHp(maxHp);
        return copy;
    }

    public Type getType() {
        return type;
    }
}
