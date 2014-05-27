package utils.resources;

import java.util.HashMap;
import java.util.Map;

import static utils.SaxReader.readXML;

/**
 * oppa google style
 */
public class Resources {
    private static Resources instance;
    private Map<String, Resource> resources = new HashMap<>();

    private Resources(){
    }

    public static Resources getInstance(){
        if (instance == null){
            instance = new Resources();
        }
        return instance;
    }

    public Resource getResource(String path) {
        if(resources.containsKey(path))
            return resources.get(path);
        return null;
    }

    public void addResource(String path){
        instance.resources.put(path, (Resource)readXML(path));
    }
}
