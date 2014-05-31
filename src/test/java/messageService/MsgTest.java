package messageService;

import accountService.AccountService;
import accountService.message.MsgToAS;
import message.Address;

/**
 * oppa google style
 */
public class MsgTest extends MsgToAS {
    public boolean success = false;

    public MsgTest(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(AccountService accountService) {
        success = true;
    }
}
