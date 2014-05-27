package utils;

/**
 * oppa google style
 */
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class SaxReader {
    private static final SAXParserFactory factory;
    private static final SAXParser saxParser;
    private static final SaxHandler handler;

    static {
        factory = SAXParserFactory.newInstance();
        saxParser = initSaxParser();
        handler = new SaxHandler();
    }

    private static SAXParser initSaxParser() {
        try {
            return factory.newSAXParser();
        } catch (Exception e) {
            return null;
        }
    }

    public static Object readXML(String xmlFile) {
        try {
            saxParser.parse(xmlFile, handler);
            return handler.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}