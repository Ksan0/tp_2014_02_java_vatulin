package gameMech;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * oop google style
 */
public class GameSession {
    private static final utils.resources.Game gameRes = (utils.resources.Game)utils.resources.Resources.getInstance().getResource("data/game.xml");
    private static AtomicInteger sessionIdCreator = new AtomicInteger();
    private int sessionId = sessionIdCreator.getAndIncrement();
    private ArrayList<String> usersId = new ArrayList<>();
    private String turnUserId;
    private Date turnStartDate;
    private String lastKickedUserId;
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
            if (tmpX < 0 || tmpX >= gameRes.getFieldSize() || tmpY < 0 || tmpY >= gameRes.getFieldSize() ||
                    gameField[tmpX][tmpY] != index) {
                break;
            }
            count++;
        }
        return count;
    }

    private void nextTurn() {
        int index = usersId.indexOf(turnUserId);
        for (int i = 0; i < usersId.size(); ++i) {
            if (++index == usersId.size())
                index = 0;

            if (usersId.get(index) != null)
                break;
        }
        turnUserId = usersId.get(index);
        turnStartDate = new Date();
    }

    public GameSession(ArrayList<String> usersId) {
        this.usersId = new ArrayList<>(usersId);
        this.turnUserId = this.usersId.get(new Random().nextInt(this.usersId.size()));
        this.turnStartDate = new Date();
        this.gameField = new byte[gameRes.getFieldSize()][gameRes.getFieldSize()];
        for (int i = 0; i < gameRes.getFieldSize(); ++i)
            for (int j = 0; j < gameRes.getFieldSize(); ++j) {
                this.gameField[i][j] = -1;
            }
    }

    public void userClick(String userId, int x, int y) {
        if (!userId.equals(turnUserId) ||
            x < 0 || x >= gameRes.getFieldSize() ||
            y < 0 || y > gameRes.getFieldSize() ||
            gameField[x][y] != -1)
        {
            lastResult = -1;
            return;
        }

        int index = usersId.indexOf(turnUserId);

        lastResult = index;
        gameField[x][y] = (byte)index;

        for (int dirX = -1; dirX <= 1; ++dirX) {
            for (int dirY = 0; dirY <= 1; ++dirY) {
                if ((dirX == 0 && dirY == 0) || (dirX == 1 && dirY == 0)) {
                    continue;
                }

                int count = calculateFilledPointsInDir(x, y, dirX, dirY, index) + 1;
                count += calculateFilledPointsInDir(x, y, -dirX, -dirY, index);
                if (count >= gameRes.getPointsToWin()) {
                    this.winUser = this.turnUserId;
                    return;
                }
            }
        }

        nextTurn();
    }

    public boolean kickTurnPlayer(String askedUserId) {
        if (askedUserId.equals(turnUserId) || winUser != null ||
            (new Date().getTime() - turnStartDate.getTime()) / 1000 <= gameRes.getTurnSafeTime()) {
            return false;
        }

        lastKickedUserId = turnUserId;
        nextTurn();
        int index = usersId.indexOf(lastKickedUserId);
        usersId.set(index, null);
        return true;
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
    public String getLastKickedUserId() {
        return lastKickedUserId;
    }
    public ArrayList<String> getUsersId() {
        ArrayList<String> list = new ArrayList<>();
        for(String userId: this.usersId) {
            if (userId != null) {
                list.add(userId);
            }
        }
        return list;
    }
    public String getTurnUserId() {
        return turnUserId;
    }
    public String getField() {
        String result = "";
        for (int j = 0; j < gameRes.getFieldSize(); ++j)
            for (int i = 0; i < gameRes.getFieldSize(); ++i) {
                if (this.gameField[i][j] == -1)
                    result += 'n';
                else
                    result += Integer.toString(gameField[i][j]);
            }
        return result;
    }
}
