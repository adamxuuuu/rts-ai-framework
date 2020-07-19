package utils;

import static core.Constants.SECOND_LONG;

public class FPSCounter {

    private double frameCounter = 0.0;
    private long previous = 0L;

    public FPSCounter() {
    }

    public void count() {
        frameCounter++;
    }

    public void printResult(long current) {
        if (current > previous + SECOND_LONG * 10) {
            previous = System.nanoTime();
            System.out.println("FPS: " + frameCounter / 10);
            frameCounter = 0;
        }
    }

}
