package core.units;

import utils.Vector2d;

public class Unit extends Entity {

    /**
     * Name of the unit
     */
    private final String name;

    private final float speed;
    private float range;
    private int maxHP;

    private int kills;
    private int cost;
    private int veteranLvl;


    public Unit(String name, float speed, Vector2d gridPos, Vector2d realPos) {
        this.name = name;
        this.speed = speed;
        this.gridPos = gridPos;
        this.screenPos = realPos;

        setEntityId(nextId++);
    }

    public Unit(Vector2d pos, String name, float speed, float range, int maxHP, int cost, int kills, int veteranLvl) {
        this.gridPos = pos;
        this.name = name;
        this.speed = speed;
        this.range = range;
        this.maxHP = maxHP;
        this.cost = cost;
        this.kills = kills;
        this.veteranLvl = veteranLvl;

        setEntityId(nextId++);
    }

    @Override
    public Entity copy() {
        return new Unit(name, speed, gridPos, screenPos);
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", position=" + gridPos +
                '}';
    }

    public String getName() {
        return name;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getVeteranLvl() {
        return veteranLvl;
    }

    public void setVeteranLvl(int veteranLvl) {
        this.veteranLvl = veteranLvl;
    }

}
