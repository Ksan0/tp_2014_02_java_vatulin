package frontend.message;

import frontend.Frontend;
import message.Abonent;
import message.Address;
import message.Msg;

/**
 * oppa google style
 */
public abstract class MsgToFrontend extends Msg {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(Abonent abonent) {
        if(abonent instanceof Frontend){
            exec((Frontend)abonent);
        }
    }

    abstract void exec(Frontend frontend);
}
