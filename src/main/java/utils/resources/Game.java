package utils.resources;

/**
 * oppa google style
 */
public class Game implements Resource {
    private int FIELD_SIZE;
    private int NEED_PLAYERS;
    private int TURN_SAFE_TIME;
    private int POINTS_TO_WIN;
    private int OFFLINE_TIME;

    public int getFIELD_SIZE() {
        return FIELD_SIZE;
    }

    public int getNEED_PLAYERS() {
        return NEED_PLAYERS;
    }

    public int getTURN_SAFE_TIME() {
        return TURN_SAFE_TIME;
    }

    public int getPOINTS_TO_WIN() {
        return POINTS_TO_WIN;
    }

    public int getOFFLINE_TIME() {
        return OFFLINE_TIME;
    }
}
