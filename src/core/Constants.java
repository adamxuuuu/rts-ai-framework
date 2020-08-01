package core;

import util.Vector2d;

import java.awt.*;

public class Constants {
    // Game settings
    public static int GRID_SIZE = 20;
    public static Color[] PLAYER_COLOR = new Color[]{Color.RED, Color.BLUE, Color.GREEN};
    public static Vector2d[] BASE_LOCATION = new Vector2d[]{new Vector2d(2, 2), new Vector2d(GRID_SIZE - 3, GRID_SIZE - 3)};

    // FPS
    public static int TARGET_FPS = 60;
    public static long SECOND_NANO = (long) 1e9;
    public static long TIME_PER_FRAME = SECOND_NANO / TARGET_FPS;

    // GUI setting
    public static int CELL_SIZE;
    public static int GUI_GAME_VIEW_SIZE;
    public static int GUI_SIDE_PANEL_WIDTH;
    public static int GUI_SIDE_PANEL_HEIGHT;

    // Terrain heights
    public static final float WATER_LVL = 0.0f; // 0 - 0.1f
    public static final float LAND_LVL = 0.9f; // 0.1f - 0.8f
    public static final float OBSTACLE = 1f; // 0.8f - 1f

}
