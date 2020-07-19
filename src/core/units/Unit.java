package core.units;

import utils.Vector2d;

public class Unit extends Entity {

    /**
     * Name of the unit
     */
    private final String name;

    private final int speed;
    private float range;
    private int maxHP;

    private int kills;
    private int cost;
    private int veteranLvl;

    public Unit(String name, int speed, Vector2d gridPos, Vector2d screenPos) {
        this.name = name;
        this.speed = speed;
        this.gridPos = gridPos;
        this.screenPos = screenPos;
    }

    @Override
    public Entity copy() {
        Unit copy = new Unit(name, speed, gridPos, screenPos);
        copy.setEntityId(getEntityId());
        return copy;
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

    public int getSpeed() {
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
