package bspkrs.util;

import java.io.*;
import net.minecraftforge.common.config.*;

@Deprecated
public class BSConfiguration extends Configuration
{
    private final File fileRef;

    public BSConfiguration(final File file)
    {
        super(file);
        this.fileRef = file;
    }

    public File getConfigFile()
    {
        return this.fileRef;
    }

    public boolean renameProperty(final String category, final String oldPropName, final String newPropName)
    {
        if(this.hasCategory(category) && this.getCategory(category).containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName))
        {
            this.get(category, newPropName, this.getCategory(category).get(oldPropName).getString());
            this.getCategory(category).remove((Object)oldPropName);
            return true;
        }
        return false;
    }

    public boolean moveProperty(final String oldCategory, final String propName, final String newCategory)
    {
        if(this.hasCategory(oldCategory) && this.getCategory(oldCategory).containsKey(propName))
        {
            this.getCategory(newCategory).put(propName, this.getCategory(oldCategory).get(propName));
            return true;
        }
        return false;
    }

    public String getString(final String name, final String category, final String defaultValue, final String comment)
    {
        return this.getString(name, category, defaultValue, comment, new String[0]);
    }

    public String getString(final String name, final String category, final String defaultValue, final String comment, final String[] validValues)
    {
        final Property prop = this.get(category, name, defaultValue);
        prop.setComment(comment + " [default: " + defaultValue + "]");
        return prop.getString();
    }

    public String[] getStringList(final String name, final String category, final String[] defaultValues, final String comment)
    {
        return this.getStringList(name, category, defaultValues, comment, new String[0]);
    }

    public String[] getStringList(final String name, final String category, final String[] defaultValue, final String comment, final String[] validValues)
    {
        final Property prop = this.get(category, name, defaultValue);
        prop.setComment(comment + " [default: " + defaultValue + "]");
        return prop.getStringList();
    }

    public boolean getBoolean(final String name, final String category, final boolean defaultValue, final String comment)
    {
        final Property prop = this.get(category, name, defaultValue);
        prop.setComment(comment + " [default: " + defaultValue + "]");
        return prop.getBoolean(defaultValue);
    }

    public int getInt(final String name, final String category, final int defaultValue, final int minValue, final int maxValue, final String comment)
    {
        final Property prop = this.get(category, name, defaultValue);
        prop.setComment(comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
        return (prop.getInt(defaultValue) < minValue) ? minValue : ((prop.getInt(defaultValue) > maxValue) ? maxValue : prop.getInt(defaultValue));
    }

    public float getFloat(final String name, final String category, final float defaultValue, final float minValue, final float maxValue, final String comment)
    {
        final Property prop = this.get(category, name, Float.toString(defaultValue));
        prop.setComment(comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
        try
        {
            return (Float.parseFloat(prop.getString()) < minValue) ? minValue : ((Float.parseFloat(prop.getString()) > maxValue) ? maxValue : Float.parseFloat(prop.getString()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
