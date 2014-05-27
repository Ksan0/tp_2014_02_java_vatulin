package utils;

/**
 * oppa google style
 */
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {
    private static final String CLASSNAME = "class";
    private String element = null;
    private Object object = null;

    public void startDocument() throws SAXException {
        System.out.println(">>>");
    }

    public void endDocument() throws SAXException {
        System.out.println("<<<");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(!qName.equals(CLASSNAME)){
            element = qName;
        }
        else{
            String className = attributes.getValue(0);
            System.out.println("Class name: " + className);
            object = ReflectionHelper.createInstance(className);
            if (object == null) {
                System.out.println("[Error] No such class");
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        element = null;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if(element != null && object != null){
            String value = new String(ch, start, length);
            System.out.println(element + " = " + value);
            ReflectionHelper.setFieldValue(object, element, value);
        }
    }

    public Object getObject(){
        return object;
    }
}