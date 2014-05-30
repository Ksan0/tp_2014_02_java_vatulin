package frontend;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static frontend.PageGenerator.getPage;

/**
 * oppa google style
 */
public class PageGeneratorTest {
    @Test
    public void testGetPage() throws Exception {
        String tmplSrc = "index.tml";
        Assert.assertTrue(getPage(tmplSrc, new HashMap<String, Object>()).contains("Index"));
        tmplSrc = "registration.tml";
        Assert.assertTrue(getPage(tmplSrc, new HashMap<String, Object>()).contains("Registration"));
        tmplSrc = "timer.tml";
        Assert.assertTrue(getPage(tmplSrc, new HashMap<String, Object>()).contains("Timer"));
    }
}
