package be.helha.aemt.test.utils;

import java.lang.reflect.Field;

public class Explorateur {
	public static Object getField(final Object obj, final String name) {
        Object value = null;
        final Class<?> class1 = obj.getClass();
        try {
            final Field declaredField;
            (declaredField = class1.getDeclaredField(name)).setAccessible(true);
            value = declaredField.get(obj);
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex2) {
            ex2.printStackTrace();
        }
        catch (IllegalAccessException ex3) {
            ex3.printStackTrace();
        }
        return value;
    }
	
	public static Object getFieldSuperClasse(final Object obj, final String name) {
        Object value = null;
        final Class<?> class1 = obj.getClass().getSuperclass();
        try {
            final Field declaredField;
            (declaredField = class1.getDeclaredField(name)).setAccessible(true);
            value = declaredField.get(obj);
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex2) {
            ex2.printStackTrace();
        }
        catch (IllegalAccessException ex3) {
            ex3.printStackTrace();
        }
        return value;
    }
	
	public static Object getFieldSuperSuperClasse(final Object obj, final String name) {
        Object value = null;
        final Class<?> class1 = obj.getClass().getSuperclass().getSuperclass();
        try {
            final Field declaredField;
            (declaredField = class1.getDeclaredField(name)).setAccessible(true);
            value = declaredField.get(obj);
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex2) {
            ex2.printStackTrace();
        }
        catch (IllegalAccessException ex3) {
            ex3.printStackTrace();
        }
        return value;
    }
}
