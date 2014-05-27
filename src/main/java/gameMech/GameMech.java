package gameMech;

import accountService.AccountServiceError;
import accountService.MsgAddUser;
import accountService.MsgAuthUser;
import frontend.PageGenerator;
import frontend.UserSession;
import message.Abonent;
import message.Address;
import message.MessageService;
import utils.resources.Resources;
import utils.resources.Template;
import utils.resources.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * oppa google style
 */

public class GameMech extends HttpServlet implements Abonent, Runnable{
    private final MessageService messageService;
    private final Address address;
    private Map<Integer, GameSession> sessionIdToGameSession = new HashMap<>();

    public GameMech(MessageService messageService){
        this.messageService = messageService;
        this.address = new Address();
        this.messageService.addService(this);
        this.messageService.getAddressService().setGameMechAddress(address);
    }

    public Address getAddress(){
        return address;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public GameSession initGameSession(ArrayList<String> usersId) {
        GameSession gameSession = new GameSession(usersId);
        sessionIdToGameSession.put(gameSession.getSessionId(), gameSession);
        return gameSession;
    }

    public GameSession getGameSession(int gameId) {
        return sessionIdToGameSession.get(gameId);
    }

    public GameSession userClick(String userId, int gameId, int x, int y) {
        GameSession gameSession = sessionIdToGameSession.get(gameId);
        gameSession.userClick(userId, x, y);
        return gameSession;
    }

    public GameSession kickTurnPlayer(int gameId, String askedUserId) {
        GameSession gameSession = sessionIdToGameSession.get(gameId);
        if (gameSession.kickTurnPlayer(askedUserId))
            return gameSession;
        return null;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            messageService.execForAbonent(this);
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
