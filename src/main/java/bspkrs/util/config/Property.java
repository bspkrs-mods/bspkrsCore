/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */

package bspkrs.util.config;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Property
{
    public enum Type
    {
        STRING,
        INTEGER,
        BOOLEAN,
        DOUBLE,
        COLOR,
        ENTITY_LIST('M'),
        BLOCK_LIST('T'),
        ITEMSTACK_LIST('O'),
        DIMENSION_LIST('W'),
        BIOME_LIST('R');
        
        private char id;
        
        private Type()
        {
            this.id = name().charAt(0);
        }
        
        private Type(char id)
        {
            this.id = id;
        }
        
        private static Type[] values = { STRING, INTEGER, BOOLEAN, DOUBLE, COLOR, ENTITY_LIST, BLOCK_LIST, ITEMSTACK_LIST, DIMENSION_LIST, BIOME_LIST };
        
        public static Type tryParse(char id)
        {
            for (int x = 0; x < values.length; x++)
            {
                if (values[x].getID() == id)
                {
                    return values[x];
                }
            }
            
            return STRING;
        }
        
        public char getID()
        {
            return id;
        }
    }
    
    private String        name;
    private String        value;
    private String        defaultValue;
    public String         comment;
    private String[]      values;
    private String[]      defaultValues;
    private String[]      validValues;
    private String        langKey;
    private String        minValue;
    private String        maxValue;
    
    private Pattern       stringPattern;
    
    private final boolean wasRead;
    private final boolean isList;
    private final Type    type;
    private boolean       changed = false;
    
    public Property(String name, String value, Type type)
    {
        this(name, value, type, false, new String[0], name);
    }
    
    public Property(String name, String value, Type type, boolean read)
    {
        this(name, value, type, read, new String[0], name);
    }
    
    public Property(String name, String value, Type type, String[] validValues)
    {
        this(name, value, type, false, validValues, name);
    }
    
    public Property(String name, String value, Type type, String langKey)
    {
        this(name, value, type, false, new String[0], langKey);
    }
    
    public Property(String name, String value, Type type, boolean read, String langKey)
    {
        this(name, value, type, read, new String[0], langKey);
    }
    
    public Property(String name, String value, Type type, String[] validValues, String langKey)
    {
        this(name, value, type, false, validValues, langKey);
    }
    
    Property(String name, String value, Type type, boolean read, String[] validValues, String langKey)
    {
        setName(name);
        this.value = value;
        this.values = new String[0];
        this.defaultValue = value;
        this.defaultValues = new String[0];
        this.validValues = validValues;
        this.type = type;
        wasRead = read;
        isList = false;
        this.minValue = String.valueOf(Integer.MIN_VALUE);
        this.maxValue = String.valueOf(Integer.MAX_VALUE);
        this.langKey = langKey;
        this.comment = "";
    }
    
    public Property(String name, String[] values, Type type)
    {
        this(name, values, type, false);
    }
    
    Property(String name, String[] values, Type type, boolean read)
    {
        this(name, values, type, read, new String[0], name);
    }
    
    public Property(String name, String[] values, Type type, String langKey)
    {
        this(name, values, type, false, langKey);
    }
    
    Property(String name, String[] values, Type type, boolean read, String langKey)
    {
        this(name, values, type, read, new String[0], langKey);
    }
    
    Property(String name, String[] values, Type type, boolean read, String[] validValues, String langKey)
    {
        setName(name);
        this.type = type;
        this.value = "";
        this.values = values;
        this.defaultValue = "";
        this.defaultValues = values;
        this.validValues = validValues;
        wasRead = read;
        isList = true;
        this.minValue = String.valueOf(Integer.MIN_VALUE);
        this.maxValue = String.valueOf(Integer.MAX_VALUE);
        this.langKey = langKey;
        this.comment = "";
    }
    
    public boolean isDefault()
    {
        if (this.isBooleanList())
        {
            if (values.length == defaultValues.length)
            {
                for (int i = 0; i < values.length; i++)
                    if (Boolean.parseBoolean(values[i]) != Boolean.parseBoolean(defaultValues[i]))
                        return false;
                
                return true;
            }
            else
                return false;
        }
        
        if (this.isIntList())
        {
            if (values.length == defaultValues.length)
            {
                for (int i = 0; i < values.length; i++)
                    if (Integer.parseInt(values[i]) != Integer.parseInt(defaultValues[i]))
                        return false;
                
                return true;
            }
            else
                return false;
        }
        
        if (this.isDoubleList())
        {
            if (values.length == defaultValues.length)
            {
                for (int i = 0; i < values.length; i++)
                    if (Double.parseDouble(values[i]) != Double.parseDouble(defaultValues[i]))
                        return false;
                
                return true;
            }
            else
                return false;
        }
        
        if (this.isList())
        {
            if (values.length == defaultValues.length)
            {
                for (int i = 0; i < values.length; i++)
                    if (!values[i].equals(defaultValues[i]))
                        return false;
                
                return true;
            }
            else
                return false;
        }
        
        if (this.type.equals(Type.BOOLEAN) && this.isBooleanValue())
            return Boolean.parseBoolean(value) == Boolean.parseBoolean(defaultValue);
        
        if (this.type.equals(Type.INTEGER) && this.isIntValue())
            return Integer.parseInt(value) == Integer.parseInt(defaultValue);
        
        if (this.type.equals(Type.DOUBLE) && this.isDoubleValue())
            return Double.parseDouble(value) == Double.parseDouble(defaultValue);
        
        return value.equals(defaultValue);
    }
    
    public void setToDefault()
    {
        this.value = this.defaultValue;
        this.values = this.defaultValues.clone();
    }
    
    public String getDefault()
    {
        return defaultValue;
    }
    
    public String[] getDefaults()
    {
        return defaultValues;
    }
    
    public void setValidStringPattern(Pattern pattern)
    {
        this.stringPattern = pattern;
    }
    
    public Pattern getValidStringPattern()
    {
        return this.stringPattern;
    }
    
    public void setLanguageKey(String value)
    {
        this.langKey = value;
    }
    
    public String getLanguageKey()
    {
        return this.langKey;
    }
    
    protected void setDefaultValue(String value)
    {
        this.defaultValue = value;
    }
    
    protected void setDefaultValue(String[] values)
    {
        this.defaultValues = values;
    }
    
    protected void setMinValue(String minValue)
    {
        this.minValue = minValue;
    }
    
    protected void setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
    }
    
    public int getMinIntValue()
    {
        return Integer.parseInt(minValue);
    }
    
    public int getMaxIntValue()
    {
        return Integer.parseInt(maxValue);
    }
    
    public double getMinDoubleValue()
    {
        return Double.parseDouble(minValue);
    }
    
    public double getMaxDoubleValue()
    {
        return Double.parseDouble(maxValue);
    }
    
    /**
     * Returns the value in this property as it's raw string.
     * 
     * @return current value
     */
    public String getString()
    {
        return value;
    }
    
    protected void setValidValues(String[] validValues)
    {
        this.validValues = validValues;
    }
    
    public String[] getValidValues()
    {
        return this.validValues;
    }
    
    /**
     * Returns the value in this property as an integer, if the value is not a valid integer, it will return the initially provided default.
     * 
     * @return The value
     */
    public int getInt()
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return Integer.parseInt(defaultValue);
        }
    }
    
    /**
     * Returns the value in this property as an integer, if the value is not a valid integer, it will return the provided default.
     * 
     * @param _default The default to provide if the current value is not a valid integer
     * @return The value
     */
    public int getInt(int _default)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return _default;
        }
    }
    
    /**
     * Checks if the current value stored in this property can be converted to an integer.
     * 
     * @return True if the type of the Property is an Integer
     */
    public boolean isIntValue()
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    
    /**
     * Returns the value in this property as a boolean, if the value is not a valid boolean, it will return the provided default.
     * 
     * @param _default The default to provide
     * @return The value as a boolean, or the default
     */
    public boolean getBoolean(boolean _default)
    {
        if (isBooleanValue())
        {
            return Boolean.parseBoolean(value);
        }
        else
        {
            return _default;
        }
    }
    
    /**
     * Returns the value in this property as a boolean, if the value is not a valid boolean, it will return the provided default.
     * 
     * @return The value as a boolean, or the default
     */
    public boolean getBoolean()
    {
        if (isBooleanValue())
        {
            return Boolean.parseBoolean(value);
        }
        else
        {
            return Boolean.parseBoolean(defaultValue);
        }
    }
    
    /**
     * Checks if the current value held by this property is a valid boolean value.
     * 
     * @return True if it is a boolean value
     */
    public boolean isBooleanValue()
    {
        return ("true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase()));
    }
    
    /**
     * Checks if the current value held by this property is a valid double value.
     * 
     * @return True if the value can be converted to an double
     */
    public boolean isDoubleValue()
    {
        try
        {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    
    /**
     * Returns the value in this property as a double, if the value is not a valid double, it will return the provided default.
     * 
     * @param _default The default to provide if the current value is not a valid double
     * @return The value
     */
    public double getDouble(double _default)
    {
        try
        {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e)
        {
            return _default;
        }
    }
    
    /**
     * Returns the value in this property as a double, if the value is not a valid double, it will return the provided default.
     * 
     * @param _default The default to provide if the current value is not a valid double
     * @return The value
     */
    public double getDouble()
    {
        try
        {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e)
        {
            return Double.parseDouble(defaultValue);
        }
    }
    
    public String[] getStringList()
    {
        return values;
    }
    
    /**
     * Returns the integer value of all values that can be parsed in the list.
     * 
     * @return Array of length 0 if none of the values could be parsed.
     */
    public int[] getIntList()
    {
        ArrayList<Integer> nums = new ArrayList<Integer>();
        
        for (String value : values)
        {
            try
            {
                nums.add(Integer.parseInt(value));
            }
            catch (NumberFormatException e)
            {}
        }
        
        int[] primitives = new int[nums.size()];
        
        for (int i = 0; i < nums.size(); i++)
        {
            primitives[i] = nums.get(i);
        }
        
        return primitives;
    }
    
    /**
     * Checks if all of the current values stored in this property can be converted to an integer.
     * 
     * @return True if the type of the Property is an Integer List
     */
    public boolean isIntList()
    {
        if (isList && type.equals(Type.INTEGER))
            for (String value : values)
            {
                try
                {
                    Integer.parseInt(value);
                }
                catch (NumberFormatException e)
                {
                    return false;
                }
            }
        return isList;
    }
    
    /**
     * Returns the boolean value of all values that can be parsed in the list.
     * 
     * @return Array of length 0 if none of the values could be parsed.
     */
    public boolean[] getBooleanList()
    {
        ArrayList<Boolean> tmp = new ArrayList<Boolean>();
        for (String value : values)
        {
            try
            {
                tmp.add(Boolean.parseBoolean(value));
            }
            catch (NumberFormatException e)
            {}
        }
        
        boolean[] primitives = new boolean[tmp.size()];
        
        for (int i = 0; i < tmp.size(); i++)
        {
            primitives[i] = tmp.get(i);
        }
        
        return primitives;
    }
    
    /**
     * Checks if all of current values stored in this property can be converted to a boolean.
     * 
     * @return True if it is a boolean value
     */
    public boolean isBooleanList()
    {
        if (isList && type.equals(Type.BOOLEAN))
            for (String value : values)
            {
                if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value))
                {
                    return false;
                }
            }
        
        return isList;
    }
    
    /**
     * Returns the double value of all values that can be parsed in the list.
     * 
     * @return Array of length 0 if none of the values could be parsed.
     */
    public double[] getDoubleList()
    {
        ArrayList<Double> tmp = new ArrayList<Double>();
        for (String value : values)
        {
            try
            {
                tmp.add(Double.parseDouble(value));
            }
            catch (NumberFormatException e)
            {}
        }
        
        double[] primitives = new double[tmp.size()];
        
        for (int i = 0; i < tmp.size(); i++)
        {
            primitives[i] = tmp.get(i);
        }
        
        return primitives;
    }
    
    /**
     * Checks if all of the current values stored in this property can be converted to a double.
     * 
     * @return True if the type of the Property is a double List
     */
    public boolean isDoubleList()
    {
        if (isList && type.equals(Type.DOUBLE))
            for (String value : values)
            {
                try
                {
                    Double.parseDouble(value);
                }
                catch (NumberFormatException e)
                {
                    return false;
                }
            }
        
        return isList;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Determines if this config value was just created, or if it was read from the config file. This is useful for mods who auto-assign
     * there blocks to determine if the ID returned is a configured one, or a automatically generated one.
     * 
     * @return True if this property was loaded from the config file with a value
     */
    public boolean wasRead()
    {
        return wasRead;
    }
    
    public Type getType()
    {
        return type;
    }
    
    public boolean isList()
    {
        return isList;
    }
    
    public boolean hasChanged()
    {
        return changed;
    }
    
    void resetChangedState()
    {
        changed = false;
    }
    
    public void set(String value)
    {
        this.value = value;
        changed = true;
    }
    
    public void set(String[] values)
    {
        this.values = values;
        changed = true;
    }
    
    public void set(int value)
    {
        set(Integer.toString(value));
    }
    
    public void set(boolean value)
    {
        set(Boolean.toString(value));
    }
    
    public void set(double value)
    {
        set(Double.toString(value));
    }
    
    public void set(boolean[] values)
    {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++)
            this.values[i] = String.valueOf(values[i]);
        changed = true;
    }
    
    public void set(int[] values)
    {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++)
            this.values[i] = String.valueOf(values[i]);
        changed = true;
    }
    
    public void set(double[] values)
    {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++)
            this.values[i] = String.valueOf(values[i]);
        changed = true;
    }
}