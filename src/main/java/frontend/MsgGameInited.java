package frontend;

import message.Address;

import java.util.LinkedList;

/**
 * oppa google style
 */
public class MsgGameInited extends MsgToFrontend {
    private int gameSessionId;
    private LinkedList<String> usersId;

    public MsgGameInited(Address from, Address to, LinkedList<String> usersId, int gameSessionId) {
        super(from, to);
        this.usersId = usersId;
        this.gameSessionId = gameSessionId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgGameInited(usersId, gameSessionId, this.getFrom());
    }
}
