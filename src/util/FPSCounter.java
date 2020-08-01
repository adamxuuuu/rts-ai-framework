package util;

import static core.Constants.SECOND_NANO;

public class FPSCounter {

    private double ticks = 0.0;
    private double frames = 0.0;
    private long lastTime = 0L;

    public FPSCounter() {
    }

    public void tick() {
        ticks++;
    }

    public void incFrame() {
        frames++;
    }

    public void printResult(long current, int interval) {
        if (current > lastTime + SECOND_NANO * interval) {
            lastTime = System.nanoTime();
            System.out.printf("Frames: %.2f  Ticks: %.2f\n", frames / interval, ticks / interval);
            ticks = 0;
            frames = 0;
        }
    }

}
