package accountService.message;

import accountService.AccountService;
import message.Abonent;
import message.Address;
import message.Msg;

/**
 * oppa google style
 */
public abstract class MsgToAS extends Msg {
    public MsgToAS(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(Abonent abonent) {
        if(abonent instanceof AccountService){
            exec((AccountService) abonent);
        }
    }

    abstract void exec(AccountService accountService);
}
