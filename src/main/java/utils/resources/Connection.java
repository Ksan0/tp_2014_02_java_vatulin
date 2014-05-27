package utils.resources;

/**
 * oppa google style
 */
public class Connection implements Resource {
    private int PORT;
    private int TEST_PORT;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private String TEST_DB;

    public String getTEST_DB() {
        return TEST_DB;
    }

    public String getDB_URL() {
        return DB_URL;
    }
    public String getDB_USER() {
        return DB_USER;
    }
    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public int getTEST_PORT() {
        return TEST_PORT;
    }

    public int getPORT() {
        return PORT;
    }


}
