package utils;

import static core.Constants.SECOND_NANO;

public class FPSCounter {

    private double frameCounter = 0.0;
    private long previous = 0L;

    public FPSCounter() {
    }

    public void count() {
        frameCounter++;
    }

    public void printResult(long current) {
        if (current > previous + SECOND_NANO * 10) {
            previous = System.nanoTime();
            System.out.printf("FPS: %f%n", frameCounter / 10);
            frameCounter = 0;
        }
    }

}
