package gameMech.message;

import frontend.message.MsgPlayerKicked;
import gameMech.GameMech;
import gameMech.GameSession;
import message.Address;
import message.MessageService;

/**
 * oppa google style
 */
public class MsgKickTurnPlayer extends MsgToGM {
    private String askedUserId;
    private int gameId;

    public MsgKickTurnPlayer(Address from, Address to, int gameId, String askedUserId) {
        super(from, to);
        this.askedUserId = askedUserId;
        this.gameId = gameId;
    }

    @Override
    void exec(GameMech gameMech) {
        GameSession gameSession = gameMech.kickTurnPlayer(gameId, askedUserId);
        if (gameSession != null) {
            MessageService ms = gameMech.getMessageService();
            ms.sendMessage(new MsgPlayerKicked(this.getTo(), this.getFrom(),
                            gameSession.getUsersId(), gameSession.getLastKickedUserId(),
                            gameSession.getTurnUserId()));
        }
    }
}
