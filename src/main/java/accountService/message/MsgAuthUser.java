package accountService.message;

import accountService.AccountService;
import accountService.AccountServiceError;
import message.Address;
import message.MessageService;
import frontend.message.MsgUserError;

/**
 * oppa google style
 */
public class MsgAuthUser extends MsgToAS {
    private String login;
    private String password;
    private String sessionId;

    public MsgAuthUser(Address from, Address to, String login, String password, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(AccountService accountService) {
        MessageService ms = accountService.getMessageSystem();
        AccountServiceError error = accountService.getUser(login, password);
        ms.sendMessage(new MsgUserError(this.getTo(), this.getFrom(), error, sessionId));
    }
}
