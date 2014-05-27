package utils.resources;

/**
 * oppa google style
 */
public class InfoMessages implements Resource {
    private String BAD_LOGIN_OR_PASSWORD;
    private String USER_ALREADY_EXISTS;
    private String BAD_USER_FORM;
    private String DB_NO_CONNECTION;
    private String WAIT_FOR_REG;

    public String getDB_NO_CONNECTION() {
        return DB_NO_CONNECTION;
    }

    public String getBAD_USER_FORM() {
        return BAD_USER_FORM;
    }

    public String getUSER_ALREADY_EXISTS() {
        return USER_ALREADY_EXISTS;
    }

    public String getBAD_LOGIN_OR_PASSWORD() {
        return BAD_LOGIN_OR_PASSWORD;
    }

    public String getWAIT_FOR_REG() {
        return WAIT_FOR_REG;
    }
}
