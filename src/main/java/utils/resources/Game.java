package utils.resources;

/**
 * oppa google style
 */
public class Game implements Resource {
    private int fieldSize;
    private int needPlayers;
    private int turnSafeTime;
    private int pointsToWin;
    private int offlineTime;

    public int getFieldSize() {
        return fieldSize;
    }

    public int getNeedPlayers() {
        return needPlayers;
    }

    public int getTurnSafeTime() {
        return turnSafeTime;
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public int getOfflineTime() {
        return offlineTime;
    }
}
