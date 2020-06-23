package core.units;

import core.commands.Command;
import utils.Vector2d;

import java.util.LinkedList;
import java.util.List;

public class Unit extends Entity {

    /**
     * Name of the unit
     */
    private final String name;

    /**
     * Properties of the unit
     */
    private final float speed;
    private float range;
    private int maxHP;
    private int cost;

    /**
     * Statistic of the unit
     */
    private int kills;
    private int veteranLvl;

    private final List<Command> commandList = new LinkedList<>();

    public Unit(String name, float speed, Vector2d pos) {
        this.name = name;
        this.speed = speed;
        this.position = pos;
    }

    public Unit(Vector2d pos, String name, float speed, float range, int maxHP, int cost, int kills, int veteranLvl) {
        this.position = pos;
        this.name = name;
        this.speed = speed;
        this.range = range;
        this.maxHP = maxHP;
        this.cost = cost;
        this.kills = kills;
        this.veteranLvl = veteranLvl;
    }

    public void update(double elapsed) {
        for (Command c : commandList) {
            //c.exec();
        }
    }

    @Override
    public Entity copy(boolean hideInfo) {
        return null;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
