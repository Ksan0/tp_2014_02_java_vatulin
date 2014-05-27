package database;

import java.io.Serializable;

/*
 * oppa google style
 */

public class UserDataSet implements Serializable{
    private static final long UNDEFINED_ID = -1L;
    private long id = UNDEFINED_ID;
    private String login;
    private String password;

    public UserDataSet(String login, String password){
        setId(UNDEFINED_ID);
        setLogin(login);
        setPassword(password);
    }
    public UserDataSet(Long id, String login, String password){
        setId(id);
        setLogin(login);
        setPassword(password);
    }

    public boolean isIdDefined() {
        return !(getId() == UNDEFINED_ID);
    }

    public String getLogin() {
        return login;
    }
    public String getPassword(){
        return password;
    }
    public long getId() {return id;
    }

    public void setId(long id){
        this.id = id;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public void setPassword(String password){
        this.password = password;
    }


}
