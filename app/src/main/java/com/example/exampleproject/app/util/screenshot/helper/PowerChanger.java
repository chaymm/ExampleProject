package com.example.exampleproject.app.util.screenshot.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class PowerChanger {

    private PowerChanger() {
    }

    public static void changeFinalString(Class klass, String fieldName, String newValue) {
        try {
            String value = (String) klass.getDeclaredField(fieldName).get(null);
            changeFinalField(klass, fieldName, newValue);
            changeString(value, newValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void changeFinalField(Class klass, String fieldName, Object newValue) {
        try {
            Field field = klass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, newValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void changeString(String string, String newString) {
        try {
            Field fields[] = String.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.set(string, field.get(newString));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
