package core.entity;

import core.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Unit extends Entity {

    public enum Type {COMBAT, WORKER}

    // Properties belong to a unit
    private int rateOfFire; // Time between each shot
    private int attack; // Damage output
    private int speed; // Pixels per frame
    private int range; // Attack range measured in grid distance
    private int carry; // Resources carried
    private Type type;

    private long timeTillNextAttack;

    private Unit(String name, int speed, long entityId, int agentId) {
        this.entityId = entityId;
        this.agentId = agentId;
        this.name = name;
        this.speed = speed;
    }

    public Unit(String filename, int agentId) {
        super.create();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            load((JSONObject) obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.agentId = agentId;
        currentHP = maxHp;
        timeTillNextAttack = 0L;
    }

    private void load(JSONObject jo) {
        super.loadBaseProperties(jo);
        this.type = Type.values()[Math.toIntExact((Long) jo.get("type"))];
        this.range = Math.toIntExact((Long) jo.get("range"));
        this.attack = Math.toIntExact((Long) jo.get("attack"));
        this.speed = Math.toIntExact((Long) jo.get("speed"));
        this.rateOfFire = Math.toIntExact((Long) jo.get("rateOfFire"));
    }

    public void reload(double delta) {
        if (timeTillNextAttack <= 0) {
            return;
        }
        this.timeTillNextAttack += delta;
    }

    public boolean canAttack() {
        return getTimeTillNextAttack() <= 0;
    }

    public void harvest() {
        carry = Math.min(carry + attack, Constants.WORKER_MAX_CARRY);
        setTimeTillNextAttack(getRateOfFire() * Constants.SECOND_NANO);
    }

    public boolean full() {
        return carry >= Constants.WORKER_MAX_CARRY;
    }

    public void dump() {
        carry = 0;
    }

    @Override
    public Entity copy() {
        Unit copy = new Unit(name, speed, entityId, agentId);
        copy.setGridPos(gridPos);
        copy.setScreenPos(screenPos);
        copy.setMaxHp(maxHp);
        copy.setCurrentHP(currentHP);
        copy.setSpriteKey(spriteKey);
        copy.setType(type);
        return copy;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public int getAttack() {
        return attack;
    }

    public long getTimeTillNextAttack() {
        return timeTillNextAttack;
    }

    public void setTimeTillNextAttack(long timeTillNextAttack) {
        this.timeTillNextAttack = timeTillNextAttack;
    }

    public int getCarry() {
        return carry;
    }
}
