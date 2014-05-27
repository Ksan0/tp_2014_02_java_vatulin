package gameMech.message;

import gameMech.GameMech;
import message.Abonent;
import message.Address;
import message.Msg;

/**
 * oppa google style
 */
public abstract class MsgToGM extends Msg {
    public MsgToGM(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(Abonent abonent) {
        if(abonent instanceof GameMech){
            exec((GameMech)abonent);
        }
    }

    abstract void exec(GameMech gameMech);
}
