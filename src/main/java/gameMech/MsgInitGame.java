package gameMech;

import frontend.MsgGameInited;
import message.Address;
import message.MessageService;

import java.util.LinkedList;

/**
 * oppa google style
 */
public class MsgInitGame extends MsgToGM {
    private LinkedList<String> usersId;

    public MsgInitGame(Address from, Address to, LinkedList<String> usersId) {
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
