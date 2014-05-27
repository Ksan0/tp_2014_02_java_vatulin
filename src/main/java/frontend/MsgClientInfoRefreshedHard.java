package frontend;

import accountService.AccountServiceError;
import message.Address;

import java.util.LinkedList;

/**
 * oppa google style
 */
public class MsgClientInfoRefreshedHard extends MsgToFrontend {
    private String userId;
    private LinkedList<String> usersId;
    private String turnUserId;
    private String winnerId;
    private String field;

    public MsgClientInfoRefreshedHard(Address from, Address to, String userId, LinkedList<String> usersId, String turnUserId, String winnerId, String field) {
        super(from, to);
        this.userId = userId;
        this.usersId = usersId;
        this.turnUserId = turnUserId;
        this.winnerId = winnerId;
        this.field = field;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.msgRefreshClientInfoHard(userId, usersId, turnUserId, winnerId, field);
    }
}
