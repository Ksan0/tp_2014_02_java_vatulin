package accountService.message;

import accountService.AccountService;
import accountService.AccountServiceError;
import message.Address;
import message.MessageService;
import frontend.message.MsgUserError;

/**
 * oppa google style
 */
public class MsgAddUser extends MsgToAS {
    private final String login;
    private final String pass;
    private final String sessionId;

    public MsgAddUser(Address from, Address to, String login, String pass, String sessionId) {
        super(from, to);
        this.login = login;
        this.pass = pass;
        this.sessionId = sessionId;
    }

    @Override
    void exec(AccountService accountService) {
        MessageService ms = accountService.getMessageSystem();
        AccountServiceError error = accountService.addUser(login, pass);
        ms.sendMessage(new MsgUserError(this.getTo(), this.getFrom(), error, sessionId));
    }
}
