package gameMech;

import frontend.MsgUserClicked;
import message.Address;
import message.MessageService;

/**
 * oppa google style
 */
public class MsgUserClick extends MsgToGM {
    private String userId;
    private int gameId;
    private int x;
    private int y;

    public MsgUserClick(Address from, Address to, String userId, int gameId, int x, int y) {
        super(from, to);
        this.userId = userId;
        this.gameId = gameId;
        this.x = x;
        this.y = y;
    }

    @Override
    void exec(GameMech gameMech) {
        GameSession gameSession = gameMech.userClick(userId, gameId, x, y);
        MessageService ms = gameMech.getMessageService();
        int result = gameSession.getLastResult();
        String winnerId = gameSession.getWinUser();
        if (result != -1) {
            ms.sendMessage(new MsgUserClicked(this.getTo(), this.getFrom(),
                            gameSession.getUsersId(), result, x, y,
                            gameSession.getTurnUserId(), winnerId));
        }
    }
}
