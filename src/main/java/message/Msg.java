package message;

import message.Abonent;
import message.Address;

/**
 * oppa google style
 */
public abstract class Msg {
    private final Address from;
    private final Address to;

    public Msg(Address from, Address to){
        this.from = from;
        this.to = to;
    }

    protected Address getFrom(){
        return from;
    }

    protected Address getTo(){
        return to;
    }

    protected abstract void exec(Abonent abonent);
}
