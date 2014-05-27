package gameMech;

import frontend.MsgGameInited;
import message.Address;
import message.MessageService;

/**
 * oppa google style
 */
public class MsgInitGame extends MsgToGM {
    private String usersId[];

    public MsgInitGame(Address from, Address to, String usersId[]) {
        super(from, to);
        this.usersId = usersId;
    }

    @Override
    void exec(GameMech gameMech) {
        GameSession gameSession = gameMech.initGameSession(usersId);
        MessageService ms = gameMech.getMessageService();
        ms.sendMessage(new MsgGameInited(this.getTo(), this.getFrom(), gameSession.getUsersId(), gameSession.getSessionId()));
    }
}
