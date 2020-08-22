package core.entity;

import core.Constants;
import core.action.*;
import core.game.GameState;
import core.game.Grid;
import org.json.simple.JSONObject;
import util.Vector2d;

import java.util.ArrayList;
import java.util.List;

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

    // Template constructor
    public Unit(String filename) {
        loadFromJson(filename);
    }

    public Unit(int rateOfFire, int attack, int speed, int range, int carry, Type type, long timeTillNextAttack) {
        this.rateOfFire = rateOfFire;
        this.attack = attack;
        this.speed = speed;
        this.range = range;
        this.carry = carry;
        this.type = type;
        this.timeTillNextAttack = timeTillNextAttack;
    }

    @Override
    protected void load(JSONObject jo) {
        super.loadBaseProperties(jo);
        this.type = Type.values()[Math.toIntExact((Long) jo.get("type"))];
        this.range = Math.toIntExact((Long) jo.get("range"));
        this.attack = Math.toIntExact((Long) jo.get("attack"));
        this.speed = Math.toIntExact((Long) jo.get("speed"));
        this.rateOfFire = Math.toIntExact((Long) jo.get("rateOfFire"));
    }

    /**
     * Calculate all action this unit can perform given a game state
     *
     * @param gs game state
     * @return A list of possible actions
     */
    public List<Action> calculateActions(GameState gs) {
        ArrayList<Action> acts = new ArrayList<>();
        if (gs == null) {
            return acts;
        }

        Grid grid = gs.getGrid();
        // Move actions
        for (Vector2d pos : grid.findAllNearby(gridPos, 5)) {
            acts.add(new Move(this, pos, grid));
        }

        // Attack and Harvest actions
        if (this.type == Type.WORKER) {
            Resource res = grid.findNearestResource(gridPos);
            if (res != null) {
                acts.add(new Harvest(entityId, res.entityId));
            }
        } else {
            for (Entity enemy : grid.getEnemyInRange(this)) {
                acts.add(new Attack(this.entityId, enemy.entityId));
            }
        }

        // Idle
        acts.add(new None());

        return acts;
    }

    public void reload(double elapsed) {
        if (timeTillNextAttack <= 0) {
            return;
        }
        this.timeTillNextAttack -= elapsed;
    }

    public boolean canAttack() {
        return getTimeTillNextAttack() <= 0;
    }

    public void harvest() {
        carry = Math.min(carry + attack, Constants.WORKER_MAX_CARRY);
        setTimeTillNextAttack(getRateOfFire() * Constants.SECOND_NANO);
    }

    public boolean isFull() {
        return carry >= Constants.WORKER_MAX_CARRY;
    }

    public void dump() {
        carry = 0;
    }

    @Override
    public Entity copy() {
        Unit copy = new Unit(rateOfFire, attack, speed, range, carry, type, timeTillNextAttack);
        super.copy(copy);
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
