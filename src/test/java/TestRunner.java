import accountService.AccountServiceTest;
import frontend.PageGeneratorTest;
import messageService.MessageServiceTest;
import resources.ResourcesTest;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/*
 * oppa google style
 */
public class TestRunner {
    public static void main(String[] args) throws Exception{
        JUnitCore core = new JUnitCore();
        core.addListener(new TestListener());
        if (core.run(PageGeneratorTest.class, AccountServiceTest.class,
                    ResourcesTest.class, MessageServiceTest.class).wasSuccessful()) {
            System.out.println("Tests: OK");
        } else {
            System.out.println("Tests: Error");
        }
        System.exit(0);
    }
}


class TestListener extends RunListener {
    @Override
    public void testStarted(Description desc) {
        System.out.println("Started:" + desc.getDisplayName());
    }

    @Override
    public void testFinished(Description desc) {
        System.out.println("OK:" + desc.getDisplayName());
    }

    @Override
    public void testFailure(Failure fail) {
        System.out.println("Failed:" + fail.getDescription().getDisplayName() + " [" + fail.getMessage() + "]");
    }
}