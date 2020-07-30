package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static float nextFloatBetween(float min, float max) {
        return (ThreadLocalRandom.current().nextFloat() * (max - min)) + min;
    }

}
