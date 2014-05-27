package frontend;

import freemarker.template.*;
import java.util.*;
import java.io.*;

/*
 * oppa google style
 */
public class PageGenerator{
    private static final String TEMPLATE_DIR = "tml";
    private static final Configuration FREEMAKER_CONFIG;

    static {
        FREEMAKER_CONFIG = new Configuration();
        try{
            FREEMAKER_CONFIG.setDirectoryForTemplateLoading(new File(TEMPLATE_DIR));
        }catch(IOException e){
            e.printStackTrace();
        }
        FREEMAKER_CONFIG.setDefaultEncoding("UTF-8");
    }

    public static String getPage(String templateName, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = FREEMAKER_CONFIG.getTemplate(templateName);
            template.process(data, stream);
        } catch (IOException | TemplateException | NullPointerException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

}