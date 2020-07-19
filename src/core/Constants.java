package core;

public class Constants {
    // Game settings
    public static int DEFAULT_GRID_WIDTH = 40;

    // FPS
    public static int TARGET_FPS = 60;
    public static long SECOND_LONG = (long) 1e9;
    public static long TIME_PER_FRAME = SECOND_LONG / TARGET_FPS;

    // GUI setting
    public static boolean VISUAL = true;

    // Geometry setting
    public static int GUI_GAME_VIEW_SIZE;
    public static int CELL_SIZE;
    public static int GUI_SIDE_PANEL_WIDTH;
    public static int GUI_SIDE_PANEL_HEIGHT;

    // Terrain heights
    public static final float SEA_LVL = 0.1f; // 0 - 0.1f
    public static final float GRD_LVL = 0.9f; // 0.1f - 0.8f
    public static final float OBSTACLE = 1f; // 0.8f - 1f

}
