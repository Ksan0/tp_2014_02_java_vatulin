package utils.resources;

/**
 * oppa google style
 */
public class Game implements Resource {
    private int FIELD_SIZE;
    private int NEED_PLAYERS;
    private int TURN_SAFE_TIME;

    public int getFIELD_SIZE() {
        return FIELD_SIZE;
    }

    public int getNEED_PLAYERS() {
        return NEED_PLAYERS;
    }

    public int getTURN_SAFE_TIME() {
        return TURN_SAFE_TIME;
    }
}
