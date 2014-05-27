package gameMech;

import accountService.AccountServiceError;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * oop google style
 */
public class GameSession {
    private static final utils.resources.Game gameRes = (utils.resources.Game)utils.resources.Resources.getInstance().getResource("data/game.xml");
    private static AtomicInteger sessionIdCreator = new AtomicInteger();
    private int sessionId = sessionIdCreator.getAndIncrement();
    private LinkedList<String> usersId = new LinkedList<>();
    private String turnUserId;
    private String winUser = null;
    private int lastResult = -1;
    private byte gameField[][]; // -1 - empty, 0 - 0-player, 1 - 1-player etc

    private int calculateFilledPointsInDir(int startX, int startY, int dirX, int dirY, int index) {
        int count = 0;
        int tmpX = startX;
        int tmpY = startY;
        for (int i = 1; i < 5; ++i) {
            tmpX += dirX;
            tmpY += dirY;
            if (tmpX < 0 || tmpX >= gameRes.getFIELD_SIZE() || tmpY < 0 || tmpY >= gameRes.getFIELD_SIZE() ||
                    gameField[tmpX][tmpY] != index) {
                break;
            }
            count++;
        }
        return count;
    }

    public GameSession(String usersId[]) {
        Collections.addAll(this.usersId, usersId);
        this.turnUserId = this.usersId.get(0);
        this.gameField = new byte[gameRes.getFIELD_SIZE()][gameRes.getFIELD_SIZE()];
        for (int i = 0; i < gameRes.getFIELD_SIZE(); ++i)
            for (int j = 0; j < gameRes.getFIELD_SIZE(); ++j) {
                this.gameField[i][j] = -1;
            }
    }

    public void userClick(String userId, int x, int y) {
        if (userId != turnUserId ||
            x < 0 || x >= gameRes.getFIELD_SIZE() ||
            y < 0 || y > gameRes.getFIELD_SIZE() ||
            gameField[x][y] != -1)
        {
            lastResult = -1;
            return;
        }

        byte index = 0;
        for ( ; index < usersId.size(); ++index) {
            if (userId == usersId.get(index))
                break;
        }

        lastResult = index;
        gameField[x][y] = index;

        for (int dirX = -1; dirX <= 1; ++dirX) {
            for (int dirY = 0; dirY <= 1; ++dirY) {
                if ((dirX == 0 && dirY == 0) || (dirX == 1 && dirY == 0)) {
                    continue;
                }

                int count = calculateFilledPointsInDir(x, y, dirX, dirY, index) + 1;
                count += calculateFilledPointsInDir(x, y, -dirX, -dirY, index);
                if (count >= 5) {
                    this.winUser = this.turnUserId;
                    return;
                }
            }
        }

        if (++index == usersId.size())
            index = 0;
        turnUserId = usersId.get(index);
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getWinUser() {
        return winUser;
    }
    public int getLastResult() {
        return lastResult;
    }
    public LinkedList<String> getUsersId() {
        return (LinkedList)usersId.clone();
    }
    public String getTurnUserId() {
        return turnUserId;
    }
    public String getField() {
        String result = "";
        for (int j = 0; j < gameRes.getFIELD_SIZE(); ++j)
            for (int i = 0; i < gameRes.getFIELD_SIZE(); ++i) {
                if (this.gameField[i][j] == -1)
                    result += 'n';
                else
                    result += new Integer(gameField[i][j]).toString();
            }
        return result;
    }
}
