package gameMech;

import frontend.MsgGameInited;
import frontend.MsgPlayerKicked;
import message.Address;
import message.MessageService;

import java.util.LinkedList;

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
