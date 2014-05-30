package messageService;

import accountService.AccountService;
import accountService.AccountServiceError;
import accountService.message.MsgAuthUser;
import frontend.Frontend;
import message.MessageService;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * oppa google style
 */
public class MessageServiceTest {
    @Test
    public void sendMsgUserAuthTest() {
        MessageService messageService = new MessageService();
        AccountService accountService = new AccountService(messageService, null);
        AccountService spyAccountService = spy(accountService);
        Frontend frontend = new Frontend(messageService);
        Frontend spyFrontend = spy(frontend);
        AccountServiceError accountServiceError = new AccountServiceError();

        String login = "user";
        String password = "pass";
        String ssid = "ssid";
        String throwMsg = "surprise!";

        when(spyAccountService.getUser(login, password)).thenReturn(accountServiceError);
        doThrow(new RuntimeException(throwMsg)).when(spyFrontend).msgUserError(accountServiceError, ssid);

        messageService.sendMessage(new MsgAuthUser(spyFrontend.getAddress(), spyAccountService.getAddress(),
                login, password, ssid));

        spyAccountService.execOneMessage();
        try {
            spyFrontend.execOneMessage();
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals(throwMsg));
        }
    }
}
