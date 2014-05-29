package frontend;

import accountService.AccountServiceError;
import accountService.message.MsgAddUser;
import accountService.message.MsgAuthUser;
import gameMech.message.MsgClientInfoRefreshHard;
import gameMech.message.MsgInitGame;
import gameMech.message.MsgKickTurnPlayer;
import gameMech.message.MsgUserClick;
import message.Abonent;
import message.Address;
import message.MessageService;
import utils.resources.Resources;
import utils.resources.Template;
import utils.resources.URL;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
 * oppa google style
 */

public class Frontend extends HttpServlet implements Abonent, Runnable{
    private static final DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");
    private final MessageService messageService;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();

    private ArrayList<String> findUsersForGame(String selfId) {
        utils.resources.Game gameResource = (utils.resources.Game)Resources.getInstance().getResource("data/game.xml");
        ArrayList<String> playersId = new ArrayList<>();
        playersId.add(selfId);

        for (String userId: sessionIdToUserSession.keySet()) {
            if (!userId.equals(selfId)) {
                UserSession userSession = sessionIdToUserSession.get(userId);
                if (userSession.isUserOnline() && userSession.getStatus() == UserSession.Status.waitingPlayer) {
                    playersId.add(userId);
                    if (playersId.size() >= gameResource.getNEED_PLAYERS())
                        break;
                }
            }
        }

        if (playersId.size() < gameResource.getNEED_PLAYERS())
            return null;
        return playersId;
    }

    public Frontend(MessageService messageService){
        this.messageService = messageService;
        address = new Address();
        this.messageService.addService(this);
    }

    public Address getAddress(){
        return address;
    }

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

    public void msgUserError(AccountServiceError error, String sessionId) {
        UserSession session = sessionIdToUserSession.get(sessionId);
        if (error.isError()) {
            session.setError(error);
        } else {
            switch(session.getStatus()) {
                case waitingReg:
                case waitingLogin:
                    session.setStatus(UserSession.Status.waitingPlayer);
                    break;
            }
        }
    }

    public void msgGameInited(ArrayList<String> usersId, int gameSessionId, Address gameMechAddress) {
        for (String userId: usersId) {
            UserSession userSession = sessionIdToUserSession.get(userId);
            userSession.setGameMechAddress(gameMechAddress);
            userSession.setGameSessionId(gameSessionId);
            userSession.setStatus(UserSession.Status.playing);
        }
    }

    public void msgRefreshClientInfoHard(String userId, ArrayList<String> usersId, String turnUserId, String winnerId, String field) {
        UserSession userSession = sessionIdToUserSession.get(userId);
        if (userSession.isHardRefreshProcessing()) {
            utils.resources.Game gameRes = (utils.resources.Game)utils.resources.Resources.getInstance().getResource("data/game.xml");

            String tmpl = "refreshHard %d %d " + field + " %s %d";
            String id = winnerId != null ? winnerId : turnUserId;
            String send = String.format(tmpl, gameRes.getFIELD_SIZE(), gameRes.getFIELD_SIZE(),
                                        userId, usersId.size());
            Integer count = 0;
            for (String uId: usersId) {
                UserSession uSs = sessionIdToUserSession.get(uId);
                send += " " + uSs.getLogin();
                send += " " + uId;
                send += " " + (count++).toString();
            }
            send += " " + id + (winnerId != null ? " win" : "");

            userSession.setInfoForSend(send);
            userSession.setHardRefreshReady();
        }
    }

    public void msgUserClicked(ArrayList<String> usersId, int result, int x, int y, String turnUserId, String winnerId) {
        String tmpl = "clicked %d %d %d %s"; // command x, y, result, {turnUserId|winUserId}, [win]
        if (winnerId != null)
            tmpl += " win";
        String id = winnerId != null ? winnerId : turnUserId;

        for (String userId: usersId) {
            UserSession userSession = sessionIdToUserSession.get(userId);
            if (winnerId != null) {
                userSession.setStatus(UserSession.Status.playingEnd);
            }
            userSession.setInfoForSend(String.format(tmpl, x, y, result, id));
        }
    }

    public void msgPlayerKicked(ArrayList<String> usersId, String kickedUserId, String turnUserId) {
        String tmpl = "kicked %s %s";
        UserSession kickedUserSession = sessionIdToUserSession.get(kickedUserId);
        kickedUserSession.setInfoForSend(String.format(tmpl, kickedUserId, turnUserId));
        kickedUserSession.setGameSessionId(-1);
        kickedUserSession.setGameMechAddress(null);
        kickedUserSession.setStatus(UserSession.Status.playingEnd);

        for (String userId: usersId) {
            UserSession userSession = sessionIdToUserSession.get(userId);
            userSession.setInfoForSend(String.format(tmpl, kickedUserId, turnUserId));
        }
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        final String path = request.getPathInfo();
        String sessionID = request.getSession().getId();

        URL urlResource = (URL) Resources.getInstance().getResource("data/url.xml");
        Template templateResource = (Template) Resources.getInstance().getResource("data/template.xml");

        if (path.isEmpty()) {
            sendPage(response, templateResource.getINDEX(), "");
            return;
        }
        if (path.equals(urlResource.getREGISTRATION_FORM())) {
            sendPage(response, templateResource.getREGISTRATION(), "");
            return;
        }
        if (path.equals(urlResource.getTIMER_PAGE())) {
            UserSession userSession = sessionIdToUserSession.get(sessionID);
            if (userSession == null){
                response.sendRedirect("/");
                return;
            }
            userSession.updateLastRequestDate();

            Map <String, Object> responseData = new HashMap<>();
            responseData.put("refreshPeriod", "1000");
            responseData.put("serverTime", Frontend.getTime());
            String msg = null;
            if (userSession.getError().isError()) {
                msg = userSession.getError().getMsg();
            } else {
                switch(userSession.getStatus()) {
                    case waitingReg:
                        msg = "waiting reg";
                        break;
                    case waitingLogin:
                        msg = "waiting login";
                        break;
                    case playingEnd:
                        userSession.setStatus(UserSession.Status.waitingPlayer);
                    case waitingPlayer:
                        msg = "waiting players";
                        ArrayList<String> usersId = findUsersForGame(userSession.getSessionId());
                        if (usersId != null) {
                            userSession.setStatus(UserSession.Status.waitingInit);
                            messageService.sendMessage( new MsgInitGame(
                                                            getAddress(),
                                                            messageService.getAddressService().getGameMechAddress(),
                                                            usersId
                                                        )
                                                      );
                        } else {
                            break;
                        }
                    case waitingInit:
                        msg = "waiting game init";
                        break;
                    case playing:
                        response.sendRedirect("/game");
                        return;
                }
            }
            responseData.put("userID", msg);

            sendPage(response, templateResource.getTIMER(), responseData);
            return;
        }
        if(path.equals(urlResource.getGAME())) {
            Map <String, Object> responseData = new HashMap<>();
            responseData.put("refreshPeriod", "1000");
            responseData.put("opponentLogin", "Mem");
            sendPage(response, templateResource.getGAME(), responseData);
            return;
        }
        if(path.equals(urlResource.getAJAX())) {
            String action = request.getParameter("action");
            UserSession userSession = sessionIdToUserSession.get(sessionID);
            if (userSession == null) {
                response.getWriter().print("invalid session");
                return;
            }

            if (action.equals("game_refresh_hard")) {
                if (userSession.getGameSessionId() == -1 || userSession.getGameMechAddress() == null) {
                    response.getWriter().print("invalid game");
                } else {
                    if (userSession.isHardRefreshOff()) {
                        userSession.setHardRefreshProcessing();
                        messageService.sendMessage(new MsgClientInfoRefreshHard(
                                getAddress(),
                                userSession.getGameMechAddress(),
                                userSession.getGameSessionId(),
                                userSession.getSessionId()
                        )
                        );
                    }
                    if (userSession.isHardRefreshReady()) {
                        userSession.setHardRefreshOff();
                        response.getWriter().print(userSession.getGameTimeString() + " " + userSession.getInfoForSend());
                    }
                }
            }
            if (action.equals("game_refresh")) {
                response.getWriter().print(userSession.getGameTimeString() + " " + userSession.getInfoForSend());
            }
            if (action.equals("user_click")) {
                int x = Integer.parseInt(request.getParameter("x"));
                int y = Integer.parseInt(request.getParameter("y"));
                messageService.sendMessage(new MsgUserClick(
                                                getAddress(),
                                                userSession.getGameMechAddress(),
                                                sessionID,
                                                userSession.getGameSessionId(),
                                                x, y
                                            )
                                          );
            }
            if (action.equals("kick_turn_player")) {
                messageService.sendMessage(new MsgKickTurnPlayer(getAddress(), userSession.getGameMechAddress(),
                                                                    userSession.getGameSessionId(), sessionID));
            }
            return;
        }

        response.sendRedirect("/");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        final String path = request.getPathInfo();
        String ssid = request.getSession().getId();
        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        URL urlResource = (URL) Resources.getInstance().getResource("data/url.xml");

        if (path.equals(urlResource.getREGISTER())) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            messageService.sendMessage( new MsgAddUser(
                                            this.getAddress(),
                                            messageService.getAddressService().getAccountServiceAddress(),
                                            login,
                                            password,
                                            ssid
                                            )
                                        );
            sessionIdToUserSession.put(ssid, new UserSession(ssid, login, UserSession.Status.waitingReg));
        }
        if (path.equals(urlResource.getLOGIN())) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            messageService.sendMessage( new MsgAuthUser(
                                            this.getAddress(),
                                            messageService.getAddressService().getAccountServiceAddress(),
                                            login,
                                            password,
                                            request.getSession().getId()
                                            )
                                        );

            for (String userSessionId: sessionIdToUserSession.keySet()) {
                UserSession userSession = sessionIdToUserSession.get(userSessionId);
                if (userSession.getLogin().equals(login)) {
                    sessionIdToUserSession.remove(userSessionId);
                }
            }
            sessionIdToUserSession.put(ssid, new UserSession(ssid, login, UserSession.Status.waitingLogin));
        }

        response.sendRedirect(urlResource.getTIMER_PAGE());
    }

    public void sendPage(HttpServletResponse response, String tmpl,
                          String errorMsg) throws IOException{
        Map <String, Object> responseData = new HashMap<>();
        responseData.put("error", errorMsg);
        response.getWriter().println(PageGenerator.getPage(tmpl, responseData));
    }

    public void sendPage(HttpServletResponse response, String tmpl,
                          Map<String, Object> responseData) throws IOException{
        response.getWriter().println(PageGenerator.getPage(tmpl, responseData));
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
