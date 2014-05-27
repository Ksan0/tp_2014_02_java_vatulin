package utils.VFS;

import java.io.IOException;
import java.util.Iterator;

/**
 * oppa google style
 */
public interface VFSInterface {
    boolean isFile(String path);
    Iterator<String> getIterator(String startDir);
}
