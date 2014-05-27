package frontend;

import accountService.AccountServiceError;
import accountService.MsgAddUser;
import accountService.MsgAuthUser;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import gameMech.MsgClientInfoRefreshHard;
import gameMech.MsgInitGame;
import gameMech.MsgUserClick;
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
                case waitingPlayer:
                    break;
                case playing:
                    break;
            }
        }
    }

    public void msgGameInited(LinkedList<String> usersId, int gameSessionId, Address gameMechAddress) {
        for (String userId: usersId) {
            UserSession userSession = sessionIdToUserSession.get(userId);
            userSession.setGameMechAddress(gameMechAddress);
            userSession.setGameSessionId(gameSessionId);
            userSession.setStatus(UserSession.Status.playing);
        }
    }

    public void msgRefreshClientInfoHard(String userId, LinkedList<String> usersId, String turnUserId, String field) {
        utils.resources.Game gameRes = (utils.resources.Game)utils.resources.Resources.getInstance().getResource("data/game.xml");

        String tmpl = "refreshHard %d %d " + field;
        String send = String.format(tmpl, gameRes.getFIELD_SIZE(), gameRes.getFIELD_SIZE());

        UserSession userSession = sessionIdToUserSession.get(userId);
        if (userSession.isHardRefreshProcessing()) {
            userSession.setInfoForSend(userId == turnUserId ? (send + " you") : send);
            userSession.setHardRefreshReady();
        }
    }

    public void msgUserClicked(LinkedList<String> usersId, int result, int x, int y, String turnUserId) {
        for (String userId: usersId) {
            UserSession userSession = sessionIdToUserSession.get(userId);
            String tmpl = "clicked %d %d %d";
            if (userId.equals(turnUserId))
                tmpl += " you";
            userSession.setInfoForSend(String.format(tmpl, x, y, result));
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
                    case waitingPlayer:
                        msg = "waiting players";
                        String usersId[] = {userSession.getSessionId()};
                        messageService.sendMessage( new MsgInitGame(
                                                        getAddress(),
                                                        messageService.getAddressService().getGameMechAddress(),
                                                        usersId
                                                    )
                                                  );
                        userSession.setStatus(UserSession.Status.waitingInit);
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
                    response.getWriter().print(userSession.getInfoForSend());
                }
            }
            if (action.equals("game_refresh")) {
                response.getWriter().print(userSession.getInfoForSend());
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
            sessionIdToUserSession.put(ssid, new UserSession(ssid, login, UserSession.Status.waitingLogin));
        }

        response.sendRedirect(urlResource.getTIMER_PAGE());
    }

    public void sendPage(@NotNull HttpServletResponse response, @NotNull String tmpl,
                          @Nullable String errorMsg) throws IOException{
        Map <String, Object> responseData = new HashMap<>();
        responseData.put("error", errorMsg);
        response.getWriter().println(PageGenerator.getPage(tmpl, responseData));
    }

    public void sendPage(@NotNull HttpServletResponse response, @NotNull String tmpl,
                          @NotNull Map<String, Object> responseData) throws IOException{
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
