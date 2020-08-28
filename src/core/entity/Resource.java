package core.entity;

import core.Constants;
import org.json.simple.JSONObject;

public class Resource extends Entity {

    public enum Type {NORMAL, RICH}

    private final Type type;

    public Resource(Type type) {
        this.entityId = Entity.nextId++;
        this.type = type;
        switch (type) {
            case NORMAL -> {
                maxHp = Constants.RESOURCE_NORM_CAP;
                spriteKey = new String[]{"scifiEnvironment_9.png"};
            }
            case RICH -> {
                maxHp = Constants.RESOURCE_RICH_CAP;
                spriteKey = new String[]{"scifiEnvironment_10.png"};
            }
        }
        currentHP = maxHp;
    }

    private Resource(Type type, int capacity) {
        this.type = type;
        this.maxHp = capacity;
    }

    @Override
    public Entity copy() {
        Resource copy = new Resource(type, maxHp);
        super.copy(copy);
        copy.spriteKey = spriteKey;
        return copy;

    }

    @Override
    protected void load(JSONObject jo) {

    }

    public Type getType() {
        return type;
    }
}
