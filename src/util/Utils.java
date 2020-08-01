package util;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static float nextFloatBetween(float min, float max) {
        return (ThreadLocalRandom.current().nextFloat() * (max - min)) + min;
    }

    public static int absMin(int a, int b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }

    public static Vector2d shortestPos(Vector2d start, LinkedList<Vector2d> ps) {
        double min = Double.MAX_VALUE;
        Vector2d res = null;
        for (Vector2d p : ps) {
            double tempDist = Vector2d.euclideanDistance(p, start);
            if (tempDist < min) {
                min = tempDist;
                res = p;
            }
        }
        return res;
    }

}
