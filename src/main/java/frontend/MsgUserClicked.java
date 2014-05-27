package frontend;

import message.Address;

import java.util.LinkedList;

/**
 * oppa google style
 */
public class MsgUserClicked extends MsgToFrontend {
    private LinkedList<String> userId;
    private int result;
    private int x;
    private int y;
    private String turnUserId;

    public MsgUserClicked(Address from, Address to, LinkedList<String> userId, int result, int x, int y, String turnUserId) {
        super(from, to);
        this.userId = userId;
        this.result = result;
        this.x = x;
        this.y = y;
        this.turnUserId = turnUserId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgUserClicked(userId, result, x, y, turnUserId);
    }
}
