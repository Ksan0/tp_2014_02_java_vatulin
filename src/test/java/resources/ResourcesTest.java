package resources;

import org.junit.Assert;
import org.junit.Test;
import utils.resources.Resources;

/**
 * oppa google style
 */
public class ResourcesTest {
    @Test
    public void testResources() {
        Resources resources = Resources.getInstance();
        resources.addResource("data/test_file.xml");
        TestFile testFile = (TestFile)resources.getResource("data/test_file.xml");
        Assert.assertTrue(testFile.getFirstParam().equals("i am first") && testFile.getSecondParam() == 2);
    }
}
