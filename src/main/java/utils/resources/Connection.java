package utils.resources;

/**
 * oppa google style
 */
public class Connection implements Resource {
    private int PORT;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    public int getPORT() {
        return PORT;
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

}
