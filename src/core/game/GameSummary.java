package core.game;

import player.Agent;

public class GameSummary {
    public int totalTick;
    public Agent winner;

    void conclude(int ticks, Agent winner) {
        totalTick = ticks;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameSummary{" +
                "totalTick=" + totalTick +
                ", winner=" + winner +
                '}';
    }
}
