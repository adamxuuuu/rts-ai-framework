package util;

import core.game.Grid;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static float nextFloatBetween(float min, float max) {
        return (ThreadLocalRandom.current().nextFloat() * (max - min)) + min;
    }

    public static int absMin(int a, int b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }

    public static BufferedImage scaleDown(BufferedImage before, double scale) {
        if (scale >= 1) {
            return before;
        }
        int w = before.getWidth();
        int h = before.getHeight();
        // Create a new image of the proper size
        int w2 = (int) (w * scale);
        int h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp
                = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);

        scaleOp.filter(before, after);
        return after;
    }

    public static Vector2d shortestPos(Vector2d start, LinkedList<Vector2d> ps, Grid grid) {
        double min = Double.MAX_VALUE;
        Vector2d res = null;
        for (Vector2d p : ps) {
            double tempDist = Vector2d.euclideanDistance(p, start);
            if (tempDist < min && grid.accessible(p.x, p.y)) {
                min = tempDist;
                res = p;
            }
        }
        return res;
    }

}
