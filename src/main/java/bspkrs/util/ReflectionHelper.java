package bspkrs.util;

import java.util.*;
import java.lang.reflect.*;

public class ReflectionHelper
{
    public static List<?> getListObject(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance)
    {
        try
        {
            Field privateField;
            if(CommonUtils.isObfuscatedEnv())
            {
                privateField = clazz.getDeclaredField(srgFieldName);
            }
            else
            {
                privateField = clazz.getDeclaredField(mcpFieldName);
            }
            privateField.setAccessible(true);
            return (List<?>)privateField.get(instance);
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void setBooleanValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final boolean newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if(CommonUtils.isObfuscatedEnv())
        {
            privateField = clazz.getDeclaredField(srgFieldName);
        }
        else
        {
            privateField = clazz.getDeclaredField(mcpFieldName);
        }
        privateField.setAccessible(true);
        privateField.setBoolean(instance, newValue);
    }

    public static boolean getBooleanValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final boolean defaultValue)
    {
        try
        {
            Field privateField;
            if(CommonUtils.isObfuscatedEnv())
            {
                privateField = clazz.getDeclaredField(srgFieldName);
            }
            else
            {
                privateField = clazz.getDeclaredField(mcpFieldName);
            }
            privateField.setAccessible(true);
            return privateField.getBoolean(instance);
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static void setIntValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final int newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if(CommonUtils.isObfuscatedEnv())
        {
            privateField = clazz.getDeclaredField(srgFieldName);
        }
        else
        {
            privateField = clazz.getDeclaredField(mcpFieldName);
        }
        privateField.setAccessible(true);
        privateField.setInt(instance, newValue);
    }

    public static int getIntValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final int defaultValue)
    {
        try
        {
            Field privateField;
            if(CommonUtils.isObfuscatedEnv())
            {
                privateField = clazz.getDeclaredField(srgFieldName);
            }
            else
            {
                privateField = clazz.getDeclaredField(mcpFieldName);
            }
            privateField.setAccessible(true);
            return privateField.getInt(instance);
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static void setFloatValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final float newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Field privateField;
        if(CommonUtils.isObfuscatedEnv())
        {
            privateField = clazz.getDeclaredField(srgFieldName);
        }
        else
        {
            privateField = clazz.getDeclaredField(mcpFieldName);
        }
        privateField.setAccessible(true);
        privateField.setFloat(instance, newValue);
    }

    public static float getFloatValue(final Class<?> clazz, final String srgFieldName, final String mcpFieldName, final Object instance, final float defaultValue)
    {
        try
        {
            Field privateField;
            if(CommonUtils.isObfuscatedEnv())
            {
                privateField = clazz.getDeclaredField(srgFieldName);
            }
            else
            {
                privateField = clazz.getDeclaredField(mcpFieldName);
            }
            privateField.setAccessible(true);
            return privateField.getFloat(instance);
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
