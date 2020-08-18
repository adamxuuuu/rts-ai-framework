package core;

import util.Vector2d;

import java.awt.*;

public class Constants {
    // Default game settings
    public static int GRID_SIZE = 30;
    public static Color[] PLAYER_COLOR = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    public static Vector2d[] BASE_LOCATION = new Vector2d[]{
            new Vector2d(3, 3),
            new Vector2d(GRID_SIZE - 4, GRID_SIZE - 4),
            new Vector2d(3, GRID_SIZE - 4),
            new Vector2d(GRID_SIZE - 4, 3)};
    public static Vector2d[] RESOURCE_LOCATION = new Vector2d[]{
            new Vector2d(0, 0), new Vector2d(1, 0), new Vector2d(0, 1),
            new Vector2d(GRID_SIZE - 1, GRID_SIZE - 1), new Vector2d(GRID_SIZE - 2, GRID_SIZE - 1), new Vector2d(GRID_SIZE - 1, GRID_SIZE - 2)
    };
    public static final int STARTING_RESOURCE = 100;

    // FPS
    public static int TARGET_TPS = 24;
    public static long SECOND_MILLI = (long) 1e6;
    public static long SECOND_NANO = (long) 1e9;
    public static long TIME_PER_TICK = SECOND_NANO / TARGET_TPS;

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
    public static final int RESOURCE_NORM_CAP = 100;
    public static final int RESOURCE_RICH_CAP = 500;
    public static final int WORKER_MAX_CARRY = 5;

}
