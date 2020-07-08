package utils;

import static utils.Constants.SECOND_LONG;

public class FPSCounter {

    private int frameCounter = 0;
    private long previous = 0L;

    public FPSCounter() {
    }

    public void count() {
        frameCounter++;
    }

    public void printResult(long current) {
        if (current > previous + SECOND_LONG) {
            previous = System.nanoTime();
            System.out.println("FPS: " + frameCounter);
            frameCounter = 0;
        }
    }

}
