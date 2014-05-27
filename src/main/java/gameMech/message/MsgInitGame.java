package gameMech.message;

import frontend.message.MsgGameInited;
import gameMech.GameMech;
import gameMech.GameSession;
import message.Address;
import message.MessageService;

import java.util.ArrayList;

/**
 * oppa google style
 */
public class MsgInitGame extends MsgToGM {
    private ArrayList<String> usersId;

    public MsgInitGame(Address from, Address to, ArrayList<String> usersId) {
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
