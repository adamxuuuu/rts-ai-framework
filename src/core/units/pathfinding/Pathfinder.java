package core.units.pathfinding;

import core.Constants;
import core.game.Grid;
import utils.Vector2d;

import java.util.*;

/**
 * Find the optimal path from point root to goal
 * Grid based path finding algorithm
 */
public class Pathfinder {

    /**
     * Current grid position
     */
    public PfNode root;

    public HashSet<PfNode> nodes;

    public Pathfinder(Vector2d curPos) {
        this.root = new PfNode(curPos);
    }

    /**
     * Shortest path between goal and root
     *
     * @param goalPos goal position
     * @param grid    the game grid
     * @return all node along the path
     */
    public Deque<PfNode> pathTo(Vector2d goalPos, Grid grid) {
        PfNode goal = new PfNode(goalPos);
        PfNode n = null;
        nodes = new HashSet<>();

        PriorityQueue<PfNode> openList = new PriorityQueue<>();
        PriorityQueue<PfNode> closedList = new PriorityQueue<>();

        root.setTotalCost(0.0);
        double dist = Vector2d.chebychevDistance(root.getPosition(), goal.getPosition());
        root.setEstimatedCost(dist);
        openList.add(root);
        nodes.add(root);

        while (openList.size() != 0) {
            n = openList.poll();
            closedList.add(n);

            if (n.getX() == goal.getX() && n.getY() == goal.getY())
                return calculatePath(n);

            ArrayList<PfNode> neighbours = new ArrayList<>();
            for (Vector2d neigh : n.getPosition().neighborhood(1, 0, grid.getSize())) {
                float h = grid.getHeightAt(neigh.x, neigh.y);
                if (h > Constants.GRD_LVL || h < Constants.SEA_LVL) {
                    // Only add accessible tile
                    continue;
                }
                neighbours.add(new PfNode(neigh));
            }

            for (PfNode nb : neighbours) {
                double nbCost = nb.getTotalCost();

                PfNode neighbour = null;
                if (nodes != null) {
                    for (PfNode n2 : nodes) {
                        if (nb.equals(n2)) {
                            neighbour = n2;
                            break;
                        }
                    }
                }
                if (neighbour == null) {
                    neighbour = nb;  // Node was not found in cache
                }

                if (!openList.contains(neighbour) && !closedList.contains(neighbour)) {
                    neighbour.setTotalCost(nbCost + n.getTotalCost());
                    dist = Vector2d.euclideanDistance(neighbour.getPosition(), goal.getPosition());
                    neighbour.setEstimatedCost(dist);
                    neighbour.setParent(n);

                    openList.add(neighbour);
                    nodes.add(neighbour);

                } else if (nbCost + n.getTotalCost() < neighbour.getTotalCost()) {
                    neighbour.setTotalCost(nbCost + n.getTotalCost());
                    neighbour.setParent(n);

                    openList.remove(neighbour);
                    closedList.remove(neighbour);

                    openList.add(neighbour);
                }
            }

        }

        if (n == null || n.getX() != goal.getX() || n.getY() != goal.getY()) //not the goal
            return null;

        return calculatePath(n);
    }

    private Deque<PfNode> calculatePath(PfNode node) {
        Deque<PfNode> path = new ArrayDeque<>();
        while (node != null) {
            if (node.getParent() != null) //to avoid adding the start node.
            {
                path.push(node);
            }
            node = node.getParent();
        }
        return path;
    }

}
