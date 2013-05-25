package bspkrs.fml.util;

import java.util.HashMap;
import java.util.Map;

import bspkrs.util.Configuration;
import bspkrs.util.Property;

@Deprecated
public class Config
{
    public static void setFromOldCtgy(Configuration config, String name, String oldCtgy, String newCtgy)
    {
        if (config.getCategory(oldCtgy).containsKey(name))
            config.getCategory(newCtgy).put(name, config.getCategory(oldCtgy).get(name));
    }
    
    public static void renameProperty(Configuration config, String category, String oldName, String newName)
    {
        if (config.getCategory(category).containsKey(oldName))
        {
            config.get(category, newName, config.getCategory(category).get(oldName).getString());
            config.getCategory(category).remove(oldName);
        }
    }
    
    public static void renameCtgy(Configuration config, String oldCtgy, String newCtgy)
    {
        if (!oldCtgy.equalsIgnoreCase(newCtgy))
        {
            for (String prop : config.getCategory(oldCtgy).keySet())
                config.get(newCtgy, prop, config.getCategory(oldCtgy).get(prop).getString());
            
            Map<String, HashMap<String, String>> toAdd = new HashMap<String, HashMap<String, String>>();
            
            for (String key : config.getCategoryNames())
                if (key.contains(oldCtgy + "."))
                {
                    HashMap<String, String> props = new HashMap<String, String>();
                    for (String prop : config.getCategory(key).keySet())
                        props.put(prop, config.getCategory(key).get(prop).getString());
                    
                    toAdd.put(newCtgy + key.substring(key.indexOf(".")), props);
                }
            
            for (String newName : toAdd.keySet())
                for (String newProp : toAdd.get(newName).keySet())
                    config.get(newName, newProp, toAdd.get(newName).get(newProp));
            
            //config.categories.remove(oldCtgy);
        }
    }
    
    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new string property.
     */
    public static String getString(Configuration config, String name, String category, String defaultValue, String comment)
    {
        Property prop = config.get(category, name, defaultValue);
        prop.comment = comment + " [default: " + defaultValue + "]";
        return prop.getString();
    }
    
    /**
     * Creates a string list property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new string property.
     */
    public static String[] getStringList(Configuration config, String name, String category, String[] defaultValue, String comment)
    {
        Property prop = config.get(category, name, defaultValue);
        prop.comment = comment + " [default: " + defaultValue + "]";
        return prop.getStringList();
    }
    
    /**
     * Creates a boolean property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new boolean property.
     */
    public static boolean getBoolean(Configuration config, String name, String category, boolean defaultValue, String comment)
    {
        Property prop = config.get(category, name, defaultValue);
        prop.comment = comment + " [default: " + defaultValue + "]";
        return prop.getBoolean(defaultValue);
    }
    
    /**
     * Creates a integer property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param minValue Minimum value of the property.
     * @param maxValue Maximum value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new integer property.
     */
    public static int getInt(Configuration config, String name, String category, int defaultValue, int minValue, int maxValue, String comment)
    {
        Property prop = config.get(category, name, defaultValue);
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        return prop.getInt(defaultValue) < minValue ? minValue : (prop.getInt(defaultValue) > maxValue ? maxValue : prop.getInt(defaultValue));
    }
    
    /**
     * Creates a float property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param minValue Minimum value of the property.
     * @param maxValue Maximum value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new float property.
     */
    public static float getFloat(Configuration config, String name, String category, float defaultValue, float minValue, float maxValue, String comment)
    {
        Property prop = config.get(category, name, Float.toString(defaultValue));
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        try
        {
            return Float.parseFloat(prop.getString()) < minValue ? minValue : (Float.parseFloat(prop.getString()) > maxValue ? maxValue : Float.parseFloat(prop.getString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return defaultValue;
    }
    
    /**
     * Creates a block ID property.
     * 
     * @param name Name of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new block ID property.
     */
    public static int getBlockId(Configuration config, String name, int defaultValue, String comment)
    {
        Property prop = config.getBlock(name, defaultValue);
        prop.comment = comment + " [default: " + defaultValue + "]";
        return prop.getInt(defaultValue);
    }
}
