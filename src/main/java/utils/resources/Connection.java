package utils.resources;

/**
 * oppa google style
 */
public class Connection implements Resource {
    private int port;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public int getPort() {
        return port;
    }
    public String getDbUrl() {
        return dbUrl;
    }
    public String getDbUser() {
        return dbUser;
    }
    public String getDbPassword() {
        return dbPassword;
    }

}
