package utils;

import java.lang.reflect.Field;

/**
 * oppa google style
 */

public class ReflectionHelper {
    public static Object createInstance(String className){
        try {
            return Class.forName(className).newInstance();
        } catch (IllegalArgumentException | SecurityException | InstantiationException |
                IllegalAccessException | ClassNotFoundException e) {
            return null;
        }
    }

    public static void setFieldValue(Object object,
                                     String fieldName,
                                     String value){

        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if(field.getType().equals(String.class)){
                field.set(object, value);
            } else if (field.getType().equals(int.class)){
                field.set(object, new Integer(value));
            } else if (field.getType().equals(double.class)){
                field.set(object, new Double(value));
            }

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

