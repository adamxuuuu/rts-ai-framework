package core.units.pathfinding;

import utils.Vector2d;

/**
 * Find the optimal path from point root to goal
 * A* algorithm
 */
public class Pathfinder {

    private final Vector2d root;
    private Vector2d destination;

    public Pathfinder(Vector2d root) {
        this.root = root;
    }

    /**
     * return the vector from root to goal
     *
     * @return the vector point towards the goal
     */
    public Vector2d pathTo() {
        int dx = destination.x - root.x;
        int dy = destination.y - root.y;

        return new Vector2d(dx, dy);
    }

    public void setDestination(Vector2d destination) {
        this.destination = destination;
    }
}
