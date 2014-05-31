package messageService;

import accountService.AccountService;
import accountService.AccountServiceError;
import accountService.message.MsgAuthUser;
import frontend.Frontend;
import message.MessageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;

/**
 * oppa google style
 */
public class MessageServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    MessageService messageService;
    utils.resources.Connection connection;
    AccountService accountService;
    AccountService spyAccountService;
    Frontend frontend;
    Frontend spyFrontend;
    String login;
    String password;
    String ssid;


    @Before
    public void before() {
        messageService = new MessageService();
        connection = mock(utils.resources.Connection.class);

        when(connection.getDbUrl()).thenReturn(null);
        when(connection.getDbUser()).thenReturn(null);
        when(connection.getDbPassword()).thenReturn(null);

        accountService = new AccountService(messageService, connection);
        spyAccountService = spy(accountService);
        frontend = new Frontend(messageService);
        spyFrontend = spy(frontend);

        login = "user";
        password = "pass";
        ssid = "ssid";
    }

    @Test
    public void sendMsgUserAuthTest() {
        AccountServiceError accountServiceError = new AccountServiceError();

        String throwMsg = "surprise!";

        when(spyAccountService.getUser(login, password)).thenReturn(accountServiceError);
        doThrow(new RuntimeException(throwMsg)).when(spyFrontend).msgUserError(accountServiceError, ssid);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(containsString(throwMsg));

        messageService.sendMessage(new MsgAuthUser(spyFrontend.getAddress(), spyAccountService.getAddress(),
                login, password, ssid));

        spyAccountService.execOneMessage();
        spyFrontend.execOneMessage();
    }

    @Test
    public void testConnectionAndExec() {
        MsgTest msg = new MsgTest(spyFrontend.getAddress(), spyAccountService.getAddress());
        messageService.sendMessage(msg);
        accountService.execOneMessage();
        Assert.assertTrue(msg.success);
    }
}
