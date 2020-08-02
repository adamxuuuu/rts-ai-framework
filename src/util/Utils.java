package util;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static float nextFloatBetween(float min, float max) {
        return (ThreadLocalRandom.current().nextFloat() * (max - min)) + min;
    }

    public static int absMin(int a, int b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }


}
