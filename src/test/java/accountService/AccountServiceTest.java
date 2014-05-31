package accountService;

import accountService.AccountService;
import accountService.AccountServiceError;
import database.UserDataSetDAO;
import message.AddressService;
import message.MessageService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * oppa google style
 */
public class AccountServiceTest {
    @Test
    public void testAccountService() {
        MessageService messageService = mock(MessageService.class);
        AddressService addressService = mock(AddressService.class);
        when(messageService.getAddressService()).thenReturn(addressService);

        AccountService accountService = spy(new AccountService(messageService, "jdbc:mysql://localhost:3306/test_jproj_db", "root", ""));

        AccountServiceError err = accountService.addUser("admin", "qwe123");
        Assert.assertFalse(err.isError());
    }

    @Test
    public void testAccountServiceFail() {
        MessageService messageService = mock(MessageService.class);
        AddressService addressService = mock(AddressService.class);
        when(messageService.getAddressService()).thenReturn(addressService);

        AccountService accountService = spy(new AccountService(messageService, "jdbc:mysql://localhost:3306/test_jproj_db", "root", ""));

        when(accountService.isConnectionClosed()).thenReturn(true);
        AccountServiceError err = accountService.addUser("user", "123qwe");
        Assert.assertTrue(err.isError());
    }

    @After
    public void after() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_jproj_db", "root", "");
            try (Statement stmt = conn.createStatement()) {
                String query = "DELETE FROM users";
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
