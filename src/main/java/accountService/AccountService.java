package accountService;

import database.UserDataSet;
import database.UserDataSetDAO;
import message.Abonent;
import message.Address;
import message.MessageService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * oppa google style
 */
public class AccountService implements Runnable, Abonent{
    private final utils.resources.Connection DB_CONNECTION_PARAMS;
    private final Address address;
    private final MessageService messageService;
    private Connection dbconn = null;

    private void openConnection() {
        try {
            if (this.dbconn != null && !this.dbconn.isClosed())
                this.dbconn.close();

            this.dbconn = DriverManager.getConnection(  DB_CONNECTION_PARAMS.getDB_URL(),
                                                        DB_CONNECTION_PARAMS.getDB_USER(),
                                                        DB_CONNECTION_PARAMS.getDB_PASSWORD()
                                                     );
        } catch (SQLException e) {
            e.printStackTrace();
            this.dbconn = null;
        }
    }
    private boolean isConnectionClosed() {
        try {
            if (this.dbconn == null || this.dbconn.isClosed())
                return true;
        } catch (SQLException e) {
            return true;
        }

        return false;
    }

    public AccountService(MessageService messageService, utils.resources.Connection connParams){
        DB_CONNECTION_PARAMS = connParams;
        address = new Address();
        this.messageService = messageService;
        this.messageService.addService(this);
        this.messageService.getAddressService().setAccountServiceAddress(address);
        openConnection();
    }

    public Address getAddress(){
        return address;
    }

    public AccountServiceError addUser(String login, String password) {
        if (isConnectionClosed())
            openConnection();

        if (isConnectionClosed())
            return new AccountServiceError(AccountServiceError.Type.DB_noConnection);

        UserDataSetDAO dao = new UserDataSetDAO(dbconn);

        UserDataSet user = null;
        try {
            user = dao.find("login", login);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if (user != null)
            return new AccountServiceError(AccountServiceError.Type.Sigiup_userAlreadyExists);

        user = new UserDataSet(login, password);
        boolean result = dao.save(user);
        if (result)
            return new AccountServiceError(user);
        return new AccountServiceError(AccountServiceError.Type.Signup_badUserForm);
    }
    public AccountServiceError getUser(String login, String password) {
        if (isConnectionClosed())
            openConnection();

        if (isConnectionClosed())
            return new AccountServiceError(AccountServiceError.Type.DB_noConnection);

        UserDataSetDAO dao = new UserDataSetDAO(dbconn);
        UserDataSet user;
        try {
            user = dao.find("login", login);
        } catch (SQLException e) {
            return new AccountServiceError(AccountServiceError.Type.Signin_badLoginOrPassword);
        }

        if (user == null || !password.equals(user.getPassword()))
            return new AccountServiceError(AccountServiceError.Type.Signin_badLoginOrPassword);
        return new AccountServiceError(user);
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

    public MessageService getMessageSystem(){
        return messageService;
    }
}
