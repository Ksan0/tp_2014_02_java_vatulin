package frontend;

import accountService.AccountServiceError;
import message.Address;

/**
 * oppa google style
 */
public class MsgUserError extends MsgToFrontend {
    private AccountServiceError error;
    private String sessionId;

    public MsgUserError(Address from, Address to, AccountServiceError error, String sessionId) {
        super(from, to);
        this.error = error;
        this.sessionId = sessionId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgUserError(error, sessionId);
    }
}
