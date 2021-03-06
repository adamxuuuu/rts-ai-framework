package core;

import util.Vector2d;

import java.awt.*;

public class Constants {
    // Default game settings
    public static int GRID_SIZE = 15;
    public static Color[] PLAYER_COLOR = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.GRAY};
    public static Vector2d[] BASE_LOCATION = new Vector2d[]{
            new Vector2d(3, 3),
            new Vector2d(GRID_SIZE - 4, GRID_SIZE - 4),
            new Vector2d(3, GRID_SIZE - 4),
            new Vector2d(GRID_SIZE - 4, 3)};
    public static Vector2d[] RESOURCE_LOCATION = new Vector2d[]{
            new Vector2d(0, 0), new Vector2d(1, 0), new Vector2d(0, 1),
            new Vector2d(GRID_SIZE - 1, 0), new Vector2d(GRID_SIZE - 2, 0), new Vector2d(GRID_SIZE - 1, 1),
            new Vector2d(0, GRID_SIZE - 1), new Vector2d(0, GRID_SIZE - 2), new Vector2d(1, GRID_SIZE - 1),
            new Vector2d(GRID_SIZE - 1, GRID_SIZE - 1), new Vector2d(GRID_SIZE - 2, GRID_SIZE - 1), new Vector2d(GRID_SIZE - 1, GRID_SIZE - 2)
    };
    public static final int GAME_MAX_TICKS = 5000;

    // FPS
    public static int TARGET_TPS = 24;
    public static long SECOND_MILLI = (long) 1e3;
    public static long SECOND_NANO = (long) 1e9;
    public static long NANO_PER_TICK = SECOND_NANO / TARGET_TPS;
    public static long MILLI_PER_TICK = SECOND_MILLI / TARGET_TPS;

    // GUI setting
    public static int CELL_SIZE = 30;
    public static int GUI_GAME_VIEW_SIZE;
    public static int GUI_SIDE_PANEL_WIDTH;
    public static int INFO_PANEL_HEIGHT;

    // Terrain heights
    public static final float WATER_LVL = 0.0f; // 0 - 0.1f
    public static final float LAND_LVL = 0.9f; // 0.1f - 0.8f
    public static final float OBSTACLE = 1f; // 0.8f - 1f

    // Resource setting
    public static final int STARTING_RESOURCE = 50;
    public static final int RESOURCE_NORM_CAP = 50;
    public static final int RESOURCE_RICH_CAP = 100;
    public static final int WORKER_MAX_CARRY = 5;

    // Unit settings
    public static final int MOVE_RANGE = 5; //1;

}
