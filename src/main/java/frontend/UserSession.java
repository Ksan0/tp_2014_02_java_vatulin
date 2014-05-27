package frontend;

import accountService.AccountServiceError;
import message.Address;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * oop google style
 */
public class UserSession {
    private String login;
    private String sessionId;
    private Status status = null;
    private AccountServiceError error = new AccountServiceError();
    private ConcurrentLinkedQueue<String> infoForSend = new ConcurrentLinkedQueue<>();
    private AtomicInteger hardRefreshCalculating = new AtomicInteger(-1);

    private Address gameMechAddress = null;
    private int gameSessionId = -1;
    private Date gameStartDate = null;
    private Date gameEndDate = null;

    public enum Status {
        waitingReg,
        waitingLogin,
        waitingPlayer,
        waitingInit,
        playing,
        playingEnd
    }

    public UserSession(String sessionId, String login, Status status) {
        this.sessionId = sessionId;
        this.login = login;
        this.status = status;
    }

    public String getLogin(){
        return login;
    }
    public String getSessionId() {
        return sessionId;
    }
    public Status getStatus() {
        return status;
    }
    public AccountServiceError getError() {
        return error;
    }
    public String getInfoForSend() {
        if(infoForSend.isEmpty())
            return "";
        return infoForSend.poll();
    }
    public Address getGameMechAddress() {
        return gameMechAddress;
    }
    public int getGameSessionId() {
        return gameSessionId;
    }
    public String getGameTimeString() {
        Date endDate = (gameEndDate != null ? gameEndDate : new Date());
        long diff = endDate.getTime() - gameStartDate.getTime();
        long sec = diff / 1000;
        long min = sec / 60;
        sec %= 60;
        return String.format("%d:%d", min, sec);
    }

    public boolean isHardRefreshOff() {
        return hardRefreshCalculating.get() == -1;
    }
    public boolean isHardRefreshProcessing() {
        return hardRefreshCalculating.get() == 0;
    }
    public boolean isHardRefreshReady() {
        return hardRefreshCalculating.get() == 1;
    }
    public void setHardRefreshOff() {
        hardRefreshCalculating.set(-1);
    }
    public void setHardRefreshProcessing() {
        hardRefreshCalculating.set(0);
    }
    public void setHardRefreshReady() {
        hardRefreshCalculating.set(1);
    }

    public void setStatus(Status status) {
        switch (status) {
            case playing:
                gameStartDate = new Date();
                break;
            case playingEnd:
                gameEndDate = new Date();
                break;
            case waitingPlayer:
                gameStartDate = null;
                gameEndDate = null;
                break;
        }

        this.status = status;
    }
    public void setError(AccountServiceError error) {
        this.error = error;
    }
    public void setInfoForSend(String info) {
        this.infoForSend.add(info);
    }
    public void setGameMechAddress(Address address) {
        gameMechAddress = address;
    }
    public void setGameSessionId(int id) {
        gameSessionId = id;
    }
}
