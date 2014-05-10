package bspkrs.util;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionHelper
{
    public static List getListObject(Class clazz, String srgFieldName, String mcpFieldName, Object instance)
    {
        try
        {
            Field privateField;
            if (CommonUtils.isObfuscatedEnv())
                privateField = clazz.getDeclaredField(srgFieldName);
            else
                privateField = clazz.getDeclaredField(mcpFieldName);
            
            privateField.setAccessible(true);
            return (List) privateField.get(instance);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setBooleanValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, boolean newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if (CommonUtils.isObfuscatedEnv())
            privateField = clazz.getDeclaredField(srgFieldName);
        else
            privateField = clazz.getDeclaredField(mcpFieldName);
        
        privateField.setAccessible(true);
        privateField.setBoolean(instance, newValue);
    }
    
    public static boolean getBooleanValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, boolean defaultValue)
    {
        try
        {
            Field privateField;
            if (CommonUtils.isObfuscatedEnv())
                privateField = clazz.getDeclaredField(srgFieldName);
            else
                privateField = clazz.getDeclaredField(mcpFieldName);
            
            privateField.setAccessible(true);
            return privateField.getBoolean(instance);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }
    
    public static void setIntValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, int newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if (CommonUtils.isObfuscatedEnv())
            privateField = clazz.getDeclaredField(srgFieldName);
        else
            privateField = clazz.getDeclaredField(mcpFieldName);
        
        privateField.setAccessible(true);
        privateField.setInt(instance, newValue);
    }
    
    public static int getIntValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, int defaultValue)
    {
        try
        {
            Field privateField;
            if (CommonUtils.isObfuscatedEnv())
                privateField = clazz.getDeclaredField(srgFieldName);
            else
                privateField = clazz.getDeclaredField(mcpFieldName);
            
            privateField.setAccessible(true);
            return privateField.getInt(instance);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }
    
    public static void setFloatValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, float newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if (CommonUtils.isObfuscatedEnv())
            privateField = clazz.getDeclaredField(srgFieldName);
        else
            privateField = clazz.getDeclaredField(mcpFieldName);
        
        privateField.setAccessible(true);
        privateField.setFloat(instance, newValue);
    }
    
    public static float getFloatValue(Class clazz, String srgFieldName, String mcpFieldName, Object instance, float defaultValue)
    {
        try
        {
            Field privateField;
            if (CommonUtils.isObfuscatedEnv())
                privateField = clazz.getDeclaredField(srgFieldName);
            else
                privateField = clazz.getDeclaredField(mcpFieldName);
            
            privateField.setAccessible(true);
            return privateField.getFloat(instance);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
