package resources;

import utils.resources.Resource;

/**
 * oppa google style
 */
public class TestFile implements Resource {
    String firstParam;
    int secondParam;

    String getFirstParam() {
        return firstParam;
    }

    int getSecondParam() {
        return secondParam;
    }
}
