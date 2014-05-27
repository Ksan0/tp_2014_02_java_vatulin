package frontend;

import message.Address;

import java.util.LinkedList;

/**
 * oppa google style
 */
public class MsgPlayerKicked extends MsgToFrontend {
    private LinkedList<String> usersId;
    private String kickedUserId;
    private String turnUserId;

    public MsgPlayerKicked(Address from, Address to, LinkedList<String> usersId, String kickedUserId, String turnUserId) {
        super(from, to);
        this.usersId = usersId;
        this.kickedUserId = kickedUserId;
        this.turnUserId = turnUserId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgPlayerKicked(usersId, kickedUserId, turnUserId);
    }
}
