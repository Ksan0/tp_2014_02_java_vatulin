package utils.resources;

/**
 * oppa google style
 */
public class InfoMessages implements Resource {
    private String badLoginOrPassword;
    private String userAlreadyExists;
    private String badUserForm;
    private String dbNoConnection;
    private String waitForReg;

    public String getDbNoConnection() {
        return dbNoConnection;
    }

    public String getBadUserForm() {
        return badUserForm;
    }

    public String getUserAlreadyExists() {
        return userAlreadyExists;
    }

    public String getBadLoginOrPassword() {
        return badLoginOrPassword;
    }

    public String getWaitForReg() {
        return waitForReg;
    }
}
