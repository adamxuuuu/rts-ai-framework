package core.gameObject;

import core.Constants;

public class Resource extends Entity {

    public enum Type {NORMAL, RICH}

    private final Type type;

    public Resource(Type type) {
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
    }

    Resource(Type type, int capacity) {
        this.type = type;
        this.maxHp = capacity;
    }

    @Override
    public Entity copy() {
        Resource copy = new Resource(type, maxHp);
        copy.spriteKey = spriteKey;
        return copy;

    }

    public Type getType() {
        return type;
    }
}
