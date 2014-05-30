package accountService;

import accountService.AccountService;
import accountService.AccountServiceError;
import org.junit.Assert;
import org.junit.Test;

/**
 * oppa google style
 */
public class AccountServiceTest {
    @Test
    public void testConnectionAndGetUser() {
        AccountService accountService = new AccountService(null, "jdbc:mysql://localhost:3306/jproj_db", "root", "");
        AccountServiceError err = accountService.getUser("admin", "qwe123");
        Assert.assertTrue(!err.isError());
    }
}
