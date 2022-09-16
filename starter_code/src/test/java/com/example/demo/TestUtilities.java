package com.example.demo;

import java.lang.reflect.Field;

public class TestUtilities {

    public static void injectObject(Object target,String fieldName,Object toInject){
        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                wasPrivate = true;
                field.setAccessible(true);
            }
            field.set(target,toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
