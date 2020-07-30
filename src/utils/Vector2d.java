package utils;

import java.util.LinkedList;

public class Vector2d {

    /**
     * X-coordinate of the vector.
     */
    public int x;

    /**
     * Y-coordinate of the vector.
     */
    public int y;

    /**
     * Default constructor.
     */
    public Vector2d() {
        this(0, 0);
    }

    /**
     * Checks if a vector and this are the same.
     *
     * @param o the other vector to check
     * @return true if their coordinates are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector2d) {
            Vector2d v = (Vector2d) o;
            return x == v.x && y == v.y;
        } else {
            return false;
        }
    }

    public boolean equalsPlusError(Object o, double error) {
        if (o instanceof Vector2d) {
            Vector2d v = (Vector2d) o;
            return x >= v.x - error && x <= v.x + error &&
                    y >= v.y - error && y <= v.y + error;
        } else {
            return false;
        }
    }

    /**
     * Builds a vector from its coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Builds a vector from another vector.
     *
     * @param v Vector to copy from.
     */
    public Vector2d(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Creates a copy of this vector
     *
     * @return a copy of this vector
     */
    public Vector2d copy() {
        return new Vector2d(x, y);
    }

    /**
     * Sets this vector's coordinates to the coordinates of another vector.
     *
     * @param v that other vector.
     */
    public void set(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Sets this vector's coordinates to the coordinates given.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the vector's coordinates to (0,0)
     */
    public void zero() {
        x = 0;
        y = 0;
    }

    /**
     * Returns a representative String of this vector.
     *
     * @return a representative String of this vector.
     */
    @Override
    public String toString() {
        return x + " : " + y;
    }

    /**
     * Adds another vector to this.
     *
     * @param v vector to add.
     * @return this, after the addition.
     */
    public Vector2d add(Vector2d v) {
        Vector2d newVec = new Vector2d(this.x, this.y);
        newVec.x += v.x;
        newVec.y += v.y;
        return newVec;
    }

    /**
     * Adds to this vector two coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return returns this, after the addition.
     */
    public Vector2d add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Adds to this vector another vector, scaled it by a factor..
     *
     * @param v Vector to add, to be scaled by w
     * @param w Scale of v.
     * @return this vector, after the addition.
     */
    public Vector2d add(Vector2d v, int w) {
        // weighted addition
        this.x += w * v.x;
        this.y += w * v.y;
        return this;
    }

    /**
     * Performs a wrap operation over this vector.
     *
     * @param w width
     * @param h height
     * @return This vector, after the wrap.
     */
    public Vector2d wrap(int w, int h) {
//        w = 2 * w;
//        h = 2 * h;
        x = (x + w) % w;
        y = (y + h) % h;
        return this;
    }

    /**
     * Subtracts another vector from this.
     *
     * @param v vector to subtract.
     * @return this, after the subtraction.
     */
    public Vector2d subtract(Vector2d v) {
        Vector2d newVec = new Vector2d(this.x, this.y);
        newVec.x -= v.x;
        newVec.y -= v.y;
        return newVec;
    }

    /**
     * Subtracts two coordinates to this vector.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return returns this, after the subtraction.
     */
    public Vector2d subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Multiplies this vector by a factor.
     *
     * @param fac factor to multiply this vector by.
     * @return This vector, after the operation.
     */
    public Vector2d mul(int fac) {
        x *= fac;
        y *= fac;
        return this;
    }

    /**
     * Rotates the vector an angle given, in radians.
     *
     * @param theta angle given, in radians
     */
    public void rotate(int theta) {
        // rotate this vector by the angle made to the horizontal by this line
        // theta is in radians
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        int nx = (int) (x * cosTheta - y * sinTheta);
        int ny = (int) (x * sinTheta + y * cosTheta);

        x = nx;
        y = ny;
    }

    /**
     * Calculates the scalar product of this vector and the one passed by parameter
     *
     * @param v vector to do the scalar product with.
     * @return the value of the scalar product.
     */
    public int scalarProduct(Vector2d v) {
        return x * v.x + y * v.y;
    }

    /**
     * Gets the square value of the parameter passed.
     *
     * @param x parameter
     * @return x * x
     */
    public static int sqr(int x) {
        return x * x;
    }

    /**
     * Returns the square distance from this vector to the one in the arguments.
     *
     * @param v the other vector, to calculate the distance to.
     * @return the square distance, in pixels, between this vector and v.
     */
    public int sqDist(Vector2d v) {
        return sqr(x - v.x) + sqr(y - v.y);
    }

    /**
     * Gets the magnitude of the vector.
     *
     * @return the magnitude of the vector (Math.sqrt(sqr(x) + sqr(y)))
     */
    public double mag() {
        return Math.sqrt(sqr(x) + sqr(y));
    }

    /**
     * Returns the distance from this vector to the one in the arguments.
     *
     * @param v the other vector, to calculate the distance to.
     * @return the distance, in pixels, between this vector and v.
     */
    public double dist(Vector2d v) {
        return Math.sqrt(sqDist(v));
    }

    /**
     * Returns the distance from this vector to a pair of coordinates.
     *
     * @param xx x coordinate
     * @param yy y coordinate
     * @return the distance, in pixels, between this vector and the pair of coordinates.
     */
    public double dist(int xx, int yy) {
        return Math.sqrt(sqr(x - xx) + sqr(y - yy));
    }

    public double custom_dist(int xx, int yy) {
        return Math.max(Math.abs(x - xx), Math.abs(y - yy));
    }

    public double custom_dist(Vector2d other) {
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }


    /**
     * Returns the atan2 of this vector.
     *
     * @return the atan2 of this vector.
     */
    public double theta() {
        return Math.atan2(y, x);
    }

    /**
     * Normalises this vector.
     */
    public void normalise() {
        double mag = mag();
        if (mag == 0) {
            x = y = 0;
        } else {
            x /= mag;
            y /= mag;
        }
    }

    /**
     * Calculates the dot product between this vector and the one passed by parameter.
     *
     * @param v the other vector.
     * @return the dot product between these two vectors.
     */
    public int dot(Vector2d v) {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2d unitVector() {
        double l = this.mag();
        if (l > 0) {
            return new Vector2d((int) (this.x / l), (int) (this.y / l));
        } else return new Vector2d(1, 0);
    }

    public boolean adjacentTo(Vector2d v) {
        return Math.abs(v.x - x) <= 1 && Math.abs(v.y - y) <= 1;
    }

    @Override
    public int hashCode() {
        return x * 20 + y;
    }

    /**
     * Returns a list a neighbouring vectors from target for a given radius. This vector's x,y is
     * excluded from the neighbours.
     *
     * @param radius   the size of the neighborhood (radius = 1, gives a 3x3 neighborhood ).
     * @param min      the minimum value to keep it in bounds (inclusive).
     * @param max      the maximum value to keep it in bounds (exclusive).
     * @param diagonal 8-way connectivity
     * @return A list of neighbors.
     */
    public LinkedList<Vector2d> neighborhood(int radius, int min, int max, boolean diagonal) {
        LinkedList<Vector2d> vectors = new LinkedList<>();

        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                // 4 way connectivity
                if (!diagonal && Math.abs(i - x) + Math.abs(j - y) > 1) {
                    continue;
                }
                //Not x,y and within established bounds
                if ((i != x || j != y) && (i >= min && j >= min && i < max && j < max)) {
                    vectors.add(new Vector2d(i, j));
                }
            }
        }
        return vectors;
    }

    public static double manhattanDistance(Vector2d p1, Vector2d p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public static double chebychevDistance(Vector2d p1, Vector2d p2) {
        return Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }

    public static double euclideanDistance(Vector2d p1, Vector2d p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    public Vector2d unify() {
        int dx = 0, dy = 0;
        if (x != 0) {
            dx = x > 0 ? 1 : -1;
        }
        if (y != 0) {
            dy = y > 0 ? 1 : -1;
        }
        return new Vector2d(dx, dy);
    }

    public boolean greater(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public boolean less(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

}
