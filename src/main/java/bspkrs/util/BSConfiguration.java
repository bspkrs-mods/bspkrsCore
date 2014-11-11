package bspkrs.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * This class offers advanced configurations capabilities, allowing to provide various categories for configuration variables.
 */
@Deprecated
public class BSConfiguration extends Configuration
{
    private final File fileRef;

    public BSConfiguration(File file)
    {
        super(file);
        fileRef = file;
    }

    @Override
    public File getConfigFile()
    {
        return fileRef;
    }

    /**
     * Renames a property in a given category.
     * 
     * @param category the category in which the property resides
     * @param oldPropName the existing property name
     * @param newPropName the
     * @return true if the category and property exist, false otherwise
     */
    @Override
    public boolean renameProperty(String category, String oldPropName, String newPropName)
    {
        if (hasCategory(category))
        {
            if (getCategory(category).containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName))
            {
                get(category, newPropName, getCategory(category).get(oldPropName).getString());
                getCategory(category).remove(oldPropName);
                return true;
            }
        }
        return false;
    }

    /**
     * Moves a property from one category to another.
     * 
     * @param oldCategory the category the property currently resides in
     * @param propName the name of the property to move
     * @param newCategory the category the property should be moved to
     * @return true if the old category and property exist, false otherwise
     */
    @Override
    public boolean moveProperty(String oldCategory, String propName, String newCategory)
    {
        if (hasCategory(oldCategory))
        {
            if (getCategory(oldCategory).containsKey(propName))
            {
                getCategory(newCategory).put(propName, getCategory(oldCategory).get(propName));
                return true;
            }
        }
        return false;
    }

    //    public static void renameCtgy(Configuration config, String oldCtgy, String newCtgy)
    //    {
    //        if (!oldCtgy.equalsIgnoreCase(newCtgy))
    //        {
    //            for (String prop : config.getCategory(oldCtgy).keySet())
    //                config.get(newCtgy, prop, config.getCategory(oldCtgy).get(prop).getString());
    //            
    //            Map<String, HashMap<String, String>> toAdd = new HashMap<String, HashMap<String, String>>();
    //            
    //            for (String key : config.getCategoryNames())
    //                if (key.contains(oldCtgy + "."))
    //                {
    //                    HashMap<String, String> props = new HashMap<String, String>();
    //                    for (String prop : config.getCategory(key).keySet())
    //                        props.put(prop, config.getCategory(key).get(prop).getString());
    //                    
    //                    toAdd.put(newCtgy + key.substring(key.indexOf(".")), props);
    //                }
    //            
    //            for (String newName : toAdd.keySet())
    //                for (String newProp : toAdd.get(newName).keySet())
    //                    config.get(newName, newProp, toAdd.get(newName).get(newProp));
    //            
    //            //config.categories.remove(oldCtgy);
    //        }
    //    }

    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @return The value of the new string property.
     */
    @Override
    public String getString(String name, String category, String defaultValue, String comment)
    {
        return getString(name, category, defaultValue, comment, new String[0]);
    }

    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @param validValues A list of valid values that this property can be set to.
     * @return The value of the new string property.
     */
    @Override
    public String getString(String name, String category, String defaultValue, String comment, String[] validValues)
    {
        Property prop = this.get(category, name, defaultValue);
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
    @Override
    public String[] getStringList(String name, String category, String[] defaultValues, String comment)
    {
        return getStringList(name, category, defaultValues, comment, new String[0]);
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
    @Override
    public String[] getStringList(String name, String category, String[] defaultValue, String comment, String[] validValues)
    {
        Property prop = this.get(category, name, defaultValue);
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
    @Override
    public boolean getBoolean(String name, String category, boolean defaultValue, String comment)
    {
        Property prop = this.get(category, name, defaultValue);
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
    @Override
    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment)
    {
        Property prop = this.get(category, name, defaultValue);
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
    @Override
    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment)
    {
        Property prop = this.get(category, name, Float.toString(defaultValue));
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
}
