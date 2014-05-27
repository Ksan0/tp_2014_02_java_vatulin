package gameMech;

import frontend.MsgClientInfoRefreshedHard;
import message.Address;
import message.MessageService;

/**
 * oppa google style
 */
public class MsgClientInfoRefreshHard extends MsgToGM {
    private int gameId;
    private String userId;

    public MsgClientInfoRefreshHard(Address from, Address to, int gameId, String userId) {
        super(from, to);
        this.gameId = gameId;
        this.userId = userId;
    }

    @Override
    void exec(GameMech gameMech) {
        GameSession gameSession = gameMech.getGameSession(gameId);
        MessageService ms = gameMech.getMessageService();
        ms.sendMessage(new MsgClientInfoRefreshedHard(this.getTo(), this.getFrom(),
                        userId, gameSession.getUsersId(), gameSession.getTurnUserId(), gameSession.getWinUser(),
                        gameSession.getField()
                        )
                      );
    }
}
