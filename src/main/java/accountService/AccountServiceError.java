package accountService;

import org.mockito.exceptions.verification.NeverWantedButInvoked;
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
                return im.getDB_NO_CONNECTION();
            case Signin_badLoginOrPassword:
                return im.getBAD_LOGIN_OR_PASSWORD();
            case Sigiup_userAlreadyExists:
                return im.getUSER_ALREADY_EXISTS();
            case Signup_badUserForm:
                return im.getBAD_USER_FORM();
        }

        return null;
    }

    public boolean isError() {
        return type != Type.NoError;
    }
}
