package frontend.message;

import frontend.Frontend;
import message.Address;

import java.util.ArrayList;

/**
 * oppa google style
 */
public class MsgUserClicked extends MsgToFrontend {
    private ArrayList<String> userId;
    private int result;
    private int x;
    private int y;
    private String turnUserId;
    private String winUser;

    public MsgUserClicked(Address from, Address to, ArrayList<String> userId, int result, int x, int y,
                          String turnUserId, String winUser) {
        super(from, to);
        this.userId = userId;
        this.result = result;
        this.x = x;
        this.y = y;
        this.turnUserId = turnUserId;
        this.winUser = winUser;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgUserClicked(userId, result, x, y, turnUserId, winUser);
    }
}
