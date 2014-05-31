package accountService;

import utils.resources.InfoMessages;
import utils.resources.Resources;

/**
 * oppa google style
 */
public class AccountServiceError {
    private Type type = Type.NoError;

    public enum Type {
        NoError,
        DB_noConnection,
        Signin_badLoginOrPassword,
        Sigiup_userAlreadyExists,
        Signup_badUserForm
    }


    public AccountServiceError() {
    }
    public AccountServiceError(Type type) {
        this.type = type;
    }

    public String getMsg() {
        InfoMessages im = (InfoMessages) Resources.getInstance().getResource("data/info_messages.xml");

        switch (type) {
            case DB_noConnection:
                return im.getDbNoConnection();
            case Signin_badLoginOrPassword:
                return im.getBadLoginOrPassword();
            case Sigiup_userAlreadyExists:
                return im.getUserAlreadyExists();
            case Signup_badUserForm:
                return im.getBadUserForm();
        }

        return null;
    }

    public boolean isError() {
        return type != Type.NoError;
    }
}
