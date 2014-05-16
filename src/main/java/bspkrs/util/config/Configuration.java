/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */

package bspkrs.util.config;

import static bspkrs.util.config.Property.Type.BOOLEAN;
import static bspkrs.util.config.Property.Type.DOUBLE;
import static bspkrs.util.config.Property.Type.INTEGER;
import static bspkrs.util.config.Property.Type.STRING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bspkrs.util.config.gui.GuiPropertyList.IGuiConfigListEntry;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLInjectionData;

/**
 * This class offers advanced configurations capabilities, allowing to provide various categories for configuration variables.
 */
public class Configuration
{
    public static final String          CATEGORY_GENERAL      = "general";
    public static final String          ALLOWED_CHARS         = "._-";
    public static final String          DEFAULT_ENCODING      = "UTF-8";
    public static final String          CATEGORY_SPLITTER     = ".";
    public static final String          NEW_LINE;
    public static final String          COMMENT_SEPARATOR     = "##########################################################################################################";
    private static final String         CONFIG_VERSION_MARKER = "~CONFIG_VERSION";
    private static final Pattern        CONFIG_START          = Pattern.compile("START: \"([^\\\"]+)\"");
    private static final Pattern        CONFIG_END            = Pattern.compile("END: \"([^\\\"]+)\"");
    public static final CharMatcher     allowedProperties     = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf(ALLOWED_CHARS));
    private static Configuration        PARENT                = null;
    
    File                                file;
    
    private Map<String, ConfigCategory> categories            = new TreeMap<String, ConfigCategory>();
    private Map<String, Configuration>  children              = new TreeMap<String, Configuration>();
    
    private boolean                     caseSensitiveCustomCategories;
    public String                       defaultEncoding       = DEFAULT_ENCODING;
    private String                      fileName              = null;
    public boolean                      isChild               = false;
    private boolean                     changed               = false;
    private String                      definedConfigVersion  = null;
    private String                      loadedConfigVersion   = null;
    
    static
    {
        NEW_LINE = System.getProperty("line.separator");
    }
    
    public Configuration()
    {}
    
    /**
     * Create a configuration file for the file given in parameter.
     */
    public Configuration(File file)
    {
        this(file, null);
    }
    
    /**
     * Create a configuration file for the file given in parameter with the provided config version number.
     */
    public Configuration(File file, String configVersion)
    {
        this.file = file;
        this.definedConfigVersion = configVersion;
        String basePath = ((File) (FMLInjectionData.data()[6])).getAbsolutePath().replace(File.separatorChar, '/').replace("/.", "");
        String path = file.getAbsolutePath().replace(File.separatorChar, '/').replace("/./", "/").replace(basePath, "");
        if (PARENT != null)
        {
            PARENT.setChild(path, this);
            isChild = true;
        }
        else
        {
            fileName = path;
            load();
        }
    }
    
    public Configuration(File file, String configVersion, boolean caseSensitiveCustomCategories)
    {
        this(file, configVersion);
        this.caseSensitiveCustomCategories = caseSensitiveCustomCategories;
    }
    
    public Configuration(File file, boolean caseSensitiveCustomCategories)
    {
        this(file, null, caseSensitiveCustomCategories);
    }
    
    @Override
    public String toString()
    {
        return file.getAbsolutePath();
    }
    
    public String getDefinedConfigVersion()
    {
        return this.definedConfigVersion;
    }
    
    public String getLoadedConfigVersion()
    {
        return this.loadedConfigVersion;
    }
    
    /******************************************************************************************************************
     * 
     * BOOLEAN gets
     * 
     *****************************************************************************************************************/
    
    /**
     * Gets a boolean Property object without a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @return a boolean Property object without a comment with isHotLoadable = false
     */
    public Property get(String category, String key, boolean defaultValue)
    {
        return get(category, key, defaultValue, (String) null);
    }
    
    /**
     * Gets a boolean Property object with a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @return a boolean Property object without a comment with isHotLoadable = false
     */
    public Property get(String category, String key, boolean defaultValue, String comment)
    {
        return get(category, key, defaultValue, comment, false);
        
    }
    
    /**
     * Gets a boolean Property object with a comment using the defined isHotLoadable value.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return a boolean Property object with a comment using the defined isHotLoadable value
     */
    public Property get(String category, String key, boolean defaultValue, String comment, boolean isHotLoadable)
    {
        Property prop = get(category, key, Boolean.toString(defaultValue), comment, BOOLEAN);
        prop.setDefaultValue(Boolean.toString(defaultValue));
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isBooleanValue())
        {
            prop.set(defaultValue);
        }
        return prop;
    }
    
    /**
     * Gets a boolean array Property without a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @return a boolean array Property without a comment using these defaults: isListLengthFixed = false, maxListLength = -1, and
     *         isHotLoadable = false
     */
    public Property get(String category, String key, boolean[] defaultValues)
    {
        return get(category, key, defaultValues, (String) null);
    }
    
    /**
     * Gets a boolean array Property with a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @return a boolean array Property with a comment using these defaults: isListLengthFixed = false, maxListLength = -1, and
     *         isHotLoadable = false
     */
    public Property get(String category, String key, boolean[] defaultValues, String comment)
    {
        return get(category, key, defaultValues, comment, false, -1, false);
    }
    
    /**
     * Gets a boolean array Property with a comment and the defined isHotLoadable value using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return a boolean array Property with a comment and the defined isHotLoadable value using these defaults: isListLengthFixed = false,
     *         maxListLength = -1
     */
    public Property get(String category, String key, boolean[] defaultValues, String comment, boolean isHotLoadable)
    {
        return get(category, key, defaultValues, comment, false, -1, isHotLoadable);
    }
    
    /**
     * Gets a boolean array Property with all settings defined.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param isListLengthFixed boolean for whether this array is required to be a specific length (defined by the default value array
     *            length or maxListLength)
     * @param maxListLength the maximum length of this array, use -1 for no max length
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return a boolean array Property with all settings defined
     */
    public Property get(String category, String key, boolean[] defaultValues, String comment,
            boolean isListLengthFixed, int maxListLength, boolean isHotLoadable)
    {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++)
        {
            values[i] = Boolean.toString(defaultValues[i]);
        }
        
        Property prop = get(category, key, values, comment, BOOLEAN);
        prop.setDefaultValues(values);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isBooleanList())
        {
            prop.set(values);
        }
        
        return prop;
    }
    
    /******************************************************************************************************************
     * 
     * INTEGER gets
     * 
     *****************************************************************************************************************/
    
    /**
     * Gets an integer Property object without a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @return an integer Property object with default bounds of Integer.MIN_VALUE and Integer.MAX_VALUE and isHotLoadable = false
     */
    public Property get(String category, String key, int defaultValue)
    {
        return get(category, key, defaultValue, (String) null, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }
    
    /**
     * Gets an integer Property object with a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @return an integer Property object with default bounds of Integer.MIN_VALUE and Integer.MAX_VALUE and isHotLoadable = false
     */
    public Property get(String category, String key, int defaultValue, String comment)
    {
        return get(category, key, defaultValue, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }
    
    /**
     * Gets an integer Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return an integer Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value
     */
    public Property get(String category, String key, int defaultValue, String comment, int minValue, int maxValue, boolean isHotLoadable)
    {
        Property prop = get(category, key, Integer.toString(defaultValue), comment, INTEGER);
        prop.setDefaultValue(Integer.toString(defaultValue));
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isIntValue())
        {
            prop.set(defaultValue);
        }
        return prop;
    }
    
    /**
     * Gets an integer array Property object without a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @return an integer array Property object with default bounds of Integer.MIN_VALUE and Integer.MAX_VALUE, isListLengthFixed = false,
     *         maxListLength = -1, and isHotLoadable = false
     */
    public Property get(String category, String key, int[] defaultValues)
    {
        return get(category, key, defaultValues, (String) null);
    }
    
    /**
     * Gets an integer array Property object with a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @return an integer array Property object with default bounds of Integer.MIN_VALUE and Integer.MAX_VALUE, isListLengthFixed = false,
     *         maxListLength = -1, and isHotLoadable = false
     */
    public Property get(String category, String key, int[] defaultValues, String comment)
    {
        return get(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, false, -1, false);
    }
    
    /**
     * Gets an integer array Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return an integer array Property object with the defined comment, minimum and maximum bounds, isHotLoadable value, isListLengthFixed
     *         = false, maxListLength = -1
     */
    public Property get(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, boolean isHotLoadable)
    {
        return get(category, key, defaultValues, comment, minValue, maxValue, false, -1, isHotLoadable);
    }
    
    /**
     * Gets an integer array Property object with all settings defined.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isListLengthFixed boolean for whether this array is required to be a specific length (defined by the default value array
     *            length or maxListLength)
     * @param maxListLength the maximum length of this array, use -1 for no max length
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return an integer array Property object with all settings defined
     */
    public Property get(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue,
            boolean isListLengthFixed, int maxListLength, boolean isHotLoadable)
    {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++)
        {
            values[i] = Integer.toString(defaultValues[i]);
        }
        
        Property prop = get(category, key, values, comment, INTEGER);
        prop.setDefaultValues(values);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isIntList())
        {
            prop.set(values);
        }
        
        return prop;
    }
    
    /******************************************************************************************************************
     * 
     * DOUBLE gets
     * 
     *****************************************************************************************************************/
    
    /**
     * Gets a double Property object without a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @return a double Property object with default bounds of Double.MIN_VALUE and Double.MAX_VALUE and isHotLoadable = false
     */
    public Property get(String category, String key, double defaultValue)
    {
        return get(category, key, defaultValue, (String) null);
    }
    
    /**
     * Gets a double Property object with a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @return a double Property object with default bounds of Double.MIN_VALUE and Double.MAX_VALUE and isHotLoadable = false
     */
    public Property get(String category, String key, double defaultValue, String comment)
    {
        return get(category, key, defaultValue, comment, Double.MIN_VALUE, Double.MAX_VALUE, false);
    }
    
    /**
     * Gets a double Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return a double Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value
     */
    public Property get(String category, String key, double defaultValue, String comment, double minValue, double maxValue, boolean isHotLoadable)
    {
        Property prop = get(category, key, Double.toString(defaultValue), comment, DOUBLE);
        prop.setDefaultValue(Double.toString(defaultValue));
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isDoubleValue())
        {
            prop.set(defaultValue);
        }
        return prop;
    }
    
    /**
     * Gets a double array Property object without a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @return a double array Property object with default bounds of Double.MIN_VALUE and Double.MAX_VALUE, isListLengthFixed = false,
     *         maxListLength = -1, and isHotLoadable = false
     */
    public Property get(String category, String key, double[] defaultValues)
    {
        return get(category, key, defaultValues, null);
    }
    
    /**
     * Gets a double array Property object without a comment using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @return a double array Property object with default bounds of Double.MIN_VALUE and Double.MAX_VALUE, isListLengthFixed = false,
     *         maxListLength = -1, and isHotLoadable = false
     */
    public Property get(String category, String key, double[] defaultValues, String comment)
    {
        return get(category, key, defaultValues, comment, Double.MIN_VALUE, Double.MAX_VALUE, false, -1, false);
    }
    
    /**
     * Gets a double array Property object with the defined comment, minimum and maximum bounds, and isHotLoadable value.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not
     * @return a double array Property object with the defined comment, minimum and maximum bounds, isHotLoadable value, isListLengthFixed =
     *         false, maxListLength = -1
     */
    public Property get(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, boolean isHotLoadable)
    {
        return get(category, key, defaultValues, comment, minValue, maxValue, false, -1, isHotLoadable);
    }
    
    /**
     * Gets a double array Property object with all settings defined.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param minValue minimum boundary
     * @param maxValue maximum boundary
     * @param isListLengthFixed boolean for whether this array is required to be a specific length (defined by the default value array
     *            length or maxListLength)
     * @param maxListLength the maximum length of this array, use -1 for no max length
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not.
     * @return a double array Property object with all settings defined
     */
    public Property get(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue,
            boolean isListLengthFixed, int maxListLength, boolean isHotLoadable)
    {
        String[] values = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++)
        {
            values[i] = Double.toString(defaultValues[i]);
        }
        
        Property prop = get(category, key, values, comment, DOUBLE);
        prop.setDefaultValues(values);
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        prop.setIsHotLoadable(isHotLoadable);
        
        if (!prop.isDoubleList())
        {
            prop.set(values);
        }
        
        return prop;
    }
    
    /******************************************************************************************************************
     * 
     * STRING gets
     * 
     *****************************************************************************************************************/
    
    /**
     * Gets a string Property without a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @return a string Property with validationPattern = null, validValues = null, and isHotLoadable = false
     */
    public Property get(String category, String key, String defaultValue)
    {
        return get(category, key, defaultValue, (String) null);
    }
    
    /**
     * Gets a string Property with a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @return a string Property with validationPattern = null, validValues = null, and isHotLoadable = false
     */
    public Property get(String category, String key, String defaultValue, String comment)
    {
        return get(category, key, defaultValue, comment, STRING);
    }
    
    /**
     * Gets a string Property with a comment using the defined validationPattern and otherwise default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param validationPattern a Pattern object for input validation
     * @return a string Property with the defined validationPattern, validValues = null, and isHotLoadable = false
     */
    public Property get(String category, String key, String defaultValue, String comment, Pattern validationPattern)
    {
        Property prop = get(category, key, defaultValue, comment, STRING);
        prop.setValidStringPattern(validationPattern);
        return prop;
    }
    
    /**
     * Gets a string Property with a comment using the defined validValues array and otherwise default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param validValues an array of valid values that this Property can be set to. If an array is provided the Config GUI control will be
     *            a value cycle button.
     * @return a string Property with the defined validValues array, validationPattern = null, and isHotLoadable = false
     */
    public Property get(String category, String key, String defaultValue, String comment, String[] validValues)
    {
        Property prop = get(category, key, defaultValue, comment, STRING);
        prop.setValidValues(validValues);
        return prop;
    }
    
    /**
     * Gets a string Property with a comment using the defined validationPattern, isHotLoadable value, and validvalues = null.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param validationPattern a Pattern object for input validation
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not.
     * @return a string Property with a comment with the defined validationPattern, isHotLoadable value, and validvalues = null
     */
    public Property get(String category, String key, String defaultValue, String comment, Pattern validationPattern, boolean isHotLoadable)
    {
        Property prop = get(category, key, defaultValue, comment, STRING);
        prop.setValidStringPattern(validationPattern);
        prop.setIsHotLoadable(isHotLoadable);
        return prop;
    }
    
    /**
     * Gets a string Property with a comment using the defined validValues array, isHotLoadable value, and validationPattern = null.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param validValues an array of valid values that this Property can be set to. If an array is provided the Config GUI control will be
     *            a value cycle button.
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not.
     * @return a string Property with a comment using the defined validValues array, isHotLoadable value, and validationPattern = null
     */
    public Property get(String category, String key, String defaultValue, String comment, String[] validValues, boolean isHotLoadable)
    {
        Property prop = get(category, key, defaultValue, comment, STRING);
        prop.setValidValues(validValues);
        prop.setIsHotLoadable(isHotLoadable);
        return prop;
    }
    
    /**
     * Gets a string array Property without a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @return a string array Property with validationPattern = null, isListLengthFixed = false, maxListLength = -1, and isHotLoadable =
     *         false
     */
    public Property get(String category, String key, String[] defaultValues)
    {
        return get(category, key, defaultValues, (String) null, false, -1, (Pattern) null, false);
    }
    
    /**
     * Gets a string array Property with a comment using the default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @return a string array Property with validationPattern = null, isListLengthFixed = false, maxListLength = -1, and isHotLoadable =
     *         false
     */
    public Property get(String category, String key, String[] defaultValues, String comment)
    {
        return get(category, key, defaultValues, comment, false, -1, (Pattern) null, false);
    }
    
    /**
     * Gets a string array Property with a comment using the defined validationPattern and otherwise default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param validationPattern a Pattern object for input validation
     * @return a string array Property with the defined validationPattern, isListLengthFixed = false, maxListLength = -1, and isHotLoadable
     *         = false
     */
    public Property get(String category, String key, String[] defaultValues, String comment, Pattern validationPattern)
    {
        return get(category, key, defaultValues, comment, false, -1, validationPattern, false);
    }
    
    /**
     * Gets a string array Property with a comment using the defined validationPattern, isHotLoadable value, and otherwise default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param validationPattern a Pattern object for input validation
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not.
     * @return a string array Property with the defined validationPattern, isHotLoadable value, isListLengthFixed = false, and maxListLength
     *         = -1
     */
    public Property get(String category, String key, String[] defaultValues, String comment, Pattern validationPattern, boolean isHotLoadable)
    {
        return get(category, key, defaultValues, comment, false, -1, validationPattern, isHotLoadable);
    }
    
    /**
     * Gets a string array Property with a comment with all settings defined.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param isListLengthFixed boolean for whether this array is required to be a specific length (defined by the default value array
     *            length or maxListLength)
     * @param maxListLength the maximum length of this array, use -1 for no max length
     * @param validationPattern a Pattern object for input validation
     * @param isHotLoadable boolean for whether this Property can be edited while a world is running or not.
     * @return a string array Property with a comment with all settings defined
     */
    public Property get(String category, String key, String[] defaultValues, String comment,
            boolean isListLengthFixed, int maxListLength, Pattern validationPattern, boolean isHotLoadable)
    {
        Property prop = get(category, key, defaultValues, comment, STRING);
        prop.setIsListLengthFixed(isListLengthFixed);
        prop.setMaxListLength(maxListLength);
        prop.setValidStringPattern(validationPattern);
        prop.setIsHotLoadable(isHotLoadable);
        return prop;
    }
    
    /******************************************************************************************************************
     * 
     * GENERIC gets
     * 
     *****************************************************************************************************************/
    
    /**
     * Gets a Property object of the specified type using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValue the default value
     * @param comment a String comment
     * @param type a Property.Type enum value
     * @return a Property object of the specified type using default settings
     */
    public Property get(String category, String key, String defaultValue, String comment, Property.Type type)
    {
        if (!caseSensitiveCustomCategories)
        {
            category = category.toLowerCase(Locale.ENGLISH);
        }
        
        ConfigCategory cat = getCategory(category);
        
        if (cat.containsKey(key))
        {
            Property prop = cat.get(key);
            
            if (prop.getType() == null)
            {
                prop = new Property(prop.getName(), prop.getString(), type);
                cat.put(key, prop);
            }
            
            prop.setDefaultValue(defaultValue);
            prop.comment = comment;
            return prop;
        }
        else if (defaultValue != null)
        {
            Property prop = new Property(key, defaultValue, type);
            prop.set(defaultValue); //Set and mark as dirty to signify it should save 
            cat.put(key, prop);
            prop.setDefaultValue(defaultValue);
            prop.comment = comment;
            return prop;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Gets a list (array) Property object of the specified type using default settings.
     * 
     * @param category the config category
     * @param key the Property key value
     * @param defaultValues an array containing the default values
     * @param comment a String comment
     * @param type a Property.Type enum value
     * @return a list (array) Property object of the specified type using default settings
     */
    public Property get(String category, String key, String[] defaultValues, String comment, Property.Type type)
    {
        if (!caseSensitiveCustomCategories)
        {
            category = category.toLowerCase(Locale.ENGLISH);
        }
        
        ConfigCategory cat = getCategory(category);
        
        if (cat.containsKey(key))
        {
            Property prop = cat.get(key);
            
            if (prop.getType() == null)
            {
                prop = new Property(prop.getName(), prop.getString(), type);
                cat.put(key, prop);
            }
            
            prop.setDefaultValues(defaultValues);
            prop.comment = comment;
            
            return prop;
        }
        else if (defaultValues != null)
        {
            Property prop = new Property(key, defaultValues, type);
            prop.setDefaultValues(defaultValues);
            prop.comment = comment;
            cat.put(key, prop);
            return prop;
        }
        else
        {
            return null;
        }
    }
    
    /* ****************************************************************************************************************
     * 
     * Other methods
     * 
     *************************************************************************************************************** */
    
    public boolean hasCategory(String category)
    {
        return categories.get(category) != null;
    }
    
    public boolean hasKey(String category, String key)
    {
        ConfigCategory cat = categories.get(category);
        return cat != null && cat.containsKey(key);
    }
    
    public void load()
    {
        if (PARENT != null && PARENT != this)
        {
            return;
        }
        
        BufferedReader buffer = null;
        UnicodeInputStreamReader input = null;
        try
        {
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }
            
            if (!file.exists() && !file.createNewFile())
            {
                return;
            }
            
            if (file.canRead())
            {
                input = new UnicodeInputStreamReader(new FileInputStream(file), defaultEncoding);
                defaultEncoding = input.getEncoding();
                buffer = new BufferedReader(input);
                
                String line;
                ConfigCategory currentCat = null;
                Property.Type type = null;
                ArrayList<String> tmpList = null;
                int lineNum = 0;
                String name = null;
                loadedConfigVersion = null;
                
                while (true)
                {
                    lineNum++;
                    line = buffer.readLine();
                    
                    if (line == null)
                    {
                        if (lineNum == 1)
                            loadedConfigVersion = definedConfigVersion;
                        break;
                    }
                    
                    Matcher start = CONFIG_START.matcher(line);
                    Matcher end = CONFIG_END.matcher(line);
                    
                    if (start.matches())
                    {
                        fileName = start.group(1);
                        categories = new TreeMap<String, ConfigCategory>();
                        continue;
                    }
                    else if (end.matches())
                    {
                        fileName = end.group(1);
                        Configuration child = new Configuration();
                        child.categories = categories;
                        this.children.put(fileName, child);
                        continue;
                    }
                    
                    int nameStart = -1, nameEnd = -1;
                    boolean skip = false;
                    boolean quoted = false;
                    
                    for (int i = 0; i < line.length() && !skip; ++i)
                    {
                        if (Character.isLetterOrDigit(line.charAt(i)) || ALLOWED_CHARS.indexOf(line.charAt(i)) != -1 || (quoted && line.charAt(i) != '"'))
                        {
                            if (nameStart == -1)
                            {
                                nameStart = i;
                            }
                            
                            nameEnd = i;
                        }
                        else if (Character.isWhitespace(line.charAt(i)))
                        {
                            // ignore space charaters
                        }
                        else
                        {
                            switch (line.charAt(i))
                            {
                                case '#':
                                    if (tmpList != null)
                                        break;
                                    skip = true;
                                    continue;
                                    
                                case '"':
                                    if (tmpList != null)
                                        break;
                                    if (quoted)
                                    {
                                        quoted = false;
                                    }
                                    if (!quoted && nameStart == -1)
                                    {
                                        quoted = true;
                                    }
                                    break;
                                
                                case '{':
                                    if (tmpList != null)
                                        break;
                                    name = line.substring(nameStart, nameEnd + 1);
                                    String qualifiedName = ConfigCategory.getQualifiedName(name, currentCat);
                                    
                                    ConfigCategory cat = categories.get(qualifiedName);
                                    if (cat == null)
                                    {
                                        currentCat = new ConfigCategory(name, currentCat);
                                        categories.put(qualifiedName, currentCat);
                                    }
                                    else
                                    {
                                        currentCat = cat;
                                    }
                                    name = null;
                                    
                                    break;
                                
                                case '}':
                                    if (tmpList != null)
                                        break;
                                    if (currentCat == null)
                                    {
                                        throw new RuntimeException(String.format("Config file corrupt, attepted to close to many categories '%s:%d'", fileName, lineNum));
                                    }
                                    currentCat = currentCat.parent;
                                    break;
                                
                                case '=':
                                    if (tmpList != null)
                                        break;
                                    name = line.substring(nameStart, nameEnd + 1);
                                    
                                    if (currentCat == null)
                                    {
                                        throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, fileName, lineNum));
                                    }
                                    
                                    Property prop = new Property(name, line.substring(i + 1), type, true);
                                    i = line.length();
                                    
                                    currentCat.put(name, prop);
                                    
                                    break;
                                
                                case ':':
                                    if (tmpList != null)
                                        break;
                                    type = Property.Type.tryParse(line.substring(nameStart, nameEnd + 1).charAt(0));
                                    nameStart = nameEnd = -1;
                                    break;
                                
                                case '<':
                                    if (tmpList != null)
                                    {
                                        throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", fileName, lineNum));
                                    }
                                    
                                    name = line.substring(nameStart, nameEnd + 1);
                                    
                                    if (currentCat == null)
                                    {
                                        throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, fileName, lineNum));
                                    }
                                    
                                    tmpList = new ArrayList<String>();
                                    
                                    skip = true;
                                    
                                    break;
                                
                                case '>':
                                    if (tmpList == null)
                                    {
                                        throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", fileName, lineNum));
                                    }
                                    
                                    currentCat.put(name, new Property(name, tmpList.toArray(new String[tmpList.size()]), type));
                                    name = null;
                                    tmpList = null;
                                    type = null;
                                    break;
                                
                                case '~':
                                    if (tmpList != null)
                                        break;
                                    
                                    if (line.startsWith(CONFIG_VERSION_MARKER))
                                    {
                                        int colon = line.indexOf(':');
                                        if (colon != -1)
                                            loadedConfigVersion = line.substring(colon + 1).trim();
                                    }
                                    
                                    skip = true;
                                    
                                    break;
                                
                                default:
                                    if (tmpList != null)
                                        break;
                                    throw new RuntimeException(String.format("Unknown character '%s' in '%s:%d'", line.charAt(i), fileName, lineNum));
                            }
                        }
                    }
                    
                    if (quoted)
                    {
                        throw new RuntimeException(String.format("Unmatched quote in '%s:%d'", fileName, lineNum));
                    }
                    else if (tmpList != null && !skip)
                    {
                        tmpList.add(line.trim());
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (buffer != null)
            {
                try
                {
                    buffer.close();
                }
                catch (IOException e)
                {}
            }
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {}
            }
        }
        
        resetChangedState();
    }
    
    public void save()
    {
        if (PARENT != null && PARENT != this)
        {
            PARENT.save();
            return;
        }
        
        try
        {
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }
            
            if (!file.exists() && !file.createNewFile())
            {
                return;
            }
            
            if (file.canWrite())
            {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, defaultEncoding));
                
                buffer.write("# Configuration file" + NEW_LINE + NEW_LINE);
                
                if (this.definedConfigVersion != null)
                    buffer.write(CONFIG_VERSION_MARKER + ": " + this.definedConfigVersion + NEW_LINE + NEW_LINE);
                
                if (children.isEmpty())
                {
                    save(buffer);
                }
                else
                {
                    for (Map.Entry<String, Configuration> entry : children.entrySet())
                    {
                        buffer.write("START: \"" + entry.getKey() + "\"" + NEW_LINE);
                        entry.getValue().save(buffer);
                        buffer.write("END: \"" + entry.getKey() + "\"" + NEW_LINE + NEW_LINE);
                    }
                }
                
                buffer.close();
                fos.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void save(BufferedWriter out) throws IOException
    {
        for (ConfigCategory cat : categories.values())
        {
            if (!cat.isChild())
            {
                cat.write(out, 0);
                out.newLine();
            }
        }
    }
    
    public ConfigCategory getCategory(String category)
    {
        ConfigCategory ret = categories.get(category);
        
        if (ret == null)
        {
            if (category.contains(CATEGORY_SPLITTER))
            {
                String[] hierarchy = category.split("\\" + CATEGORY_SPLITTER);
                ConfigCategory parent = categories.get(hierarchy[0]);
                
                if (parent == null)
                {
                    parent = new ConfigCategory(hierarchy[0]);
                    categories.put(parent.getQualifiedName(), parent);
                    changed = true;
                }
                
                for (int i = 1; i < hierarchy.length; i++)
                {
                    String name = ConfigCategory.getQualifiedName(hierarchy[i], parent);
                    ConfigCategory child = categories.get(name);
                    
                    if (child == null)
                    {
                        child = new ConfigCategory(hierarchy[i], parent);
                        categories.put(name, child);
                        changed = true;
                    }
                    
                    ret = child;
                    parent = child;
                }
            }
            else
            {
                ret = new ConfigCategory(category);
                categories.put(category, ret);
                changed = true;
            }
        }
        
        return ret;
    }
    
    /**
     * 
     * @param category the config category
     */
    public void removeCategory(ConfigCategory category)
    {
        for (ConfigCategory child : category.getChildren())
        {
            removeCategory(child);
        }
        
        if (categories.containsKey(category.getQualifiedName()))
        {
            categories.remove(category.getQualifiedName());
            if (category.parent != null)
            {
                category.parent.removeChild(category);
            }
            changed = true;
        }
    }
    
    /**
     * Adds a comment to the specified ConfigCategory object
     * 
     * @param category the config category
     * @param comment a String comment
     */
    public Configuration addCustomCategoryComment(String category, String comment)
    {
        if (!caseSensitiveCustomCategories)
            category = category.toLowerCase(Locale.ENGLISH);
        getCategory(category).setComment(comment);
        return this;
    }
    
    /**
     * Adds a language key to the specified ConfigCategory object
     * 
     * @param category the config category
     * @param langKey a language key string such as configcategory.general
     */
    public Configuration addCustomCategoryLanguageKey(String category, String langKey)
    {
        if (!caseSensitiveCustomCategories)
            category = category.toLowerCase(Locale.ENGLISH);
        getCategory(category).setLanguageKey(langKey);
        return this;
    }
    
    /**
     * Sets the custom IGuiConfigListEntry class that should be used in place of the standard entry class (which is just a button that
     * navigates into the category). This class MUST provide a constructor with the following parameter types: {@code GuiConfig} (the parent
     * GuiConfig screen will be provided), {@code GuiPropertyList} (the parent GuiPropertyList will be provided), {@code IConfigProperty}
     * (the IConfigProperty for this Property will be provided).
     * 
     * @param category the config category
     * @param clazz a class that implements IGuiConfigListEntry
     */
    public Configuration setCategoryCustomIGuiConfigListEntryClass(String category, Class<? extends IGuiConfigListEntry> clazz)
    {
        
        if (!caseSensitiveCustomCategories)
            category = category.toLowerCase(Locale.ENGLISH);
        getCategory(category).setCustomIGuiConfigListEntryClass(clazz);
        return this;
    }
    
    /**
     * Sets the ConfigCategory.isHotLoadable flag to the specified value.
     * 
     * @param category
     * @param isHotLoadable
     */
    public Configuration setCategoryIsHotLoadable(String category, boolean isHotLoadable)
    {
        if (!caseSensitiveCustomCategories)
            category = category.toLowerCase(Locale.ENGLISH);
        getCategory(category).setIsHotLoadable(isHotLoadable);
        return this;
    }
    
    private void setChild(String name, Configuration child)
    {
        if (!children.containsKey(name))
        {
            children.put(name, child);
            changed = true;
        }
        else
        {
            Configuration old = children.get(name);
            child.categories = old.categories;
            child.fileName = old.fileName;
            old.changed = true;
        }
    }
    
    public static void enableGlobalConfig()
    {
        PARENT = new Configuration(new File(Loader.instance().getConfigDir(), "global.cfg"));
        PARENT.load();
    }
    
    public static class UnicodeInputStreamReader extends Reader
    {
        private final InputStreamReader input;
        private final String            defaultEnc;
        
        public UnicodeInputStreamReader(InputStream source, String encoding) throws IOException
        {
            defaultEnc = encoding;
            String enc = encoding;
            byte[] data = new byte[4];
            
            PushbackInputStream pbStream = new PushbackInputStream(source, data.length);
            int read = pbStream.read(data, 0, data.length);
            int size = 0;
            
            int bom16 = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
            int bom24 = bom16 << 8 | (data[2] & 0xFF);
            int bom32 = bom24 << 8 | (data[3] & 0xFF);
            
            if (bom24 == 0xEFBBBF)
            {
                enc = "UTF-8";
                size = 3;
            }
            else if (bom16 == 0xFEFF)
            {
                enc = "UTF-16BE";
                size = 2;
            }
            else if (bom16 == 0xFFFE)
            {
                enc = "UTF-16LE";
                size = 2;
            }
            else if (bom32 == 0x0000FEFF)
            {
                enc = "UTF-32BE";
                size = 4;
            }
            else if (bom32 == 0xFFFE0000) //This will never happen as it'll be caught by UTF-16LE,
            { //but if anyone ever runs across a 32LE file, i'd like to disect it.
                enc = "UTF-32LE";
                size = 4;
            }
            
            if (size < read)
            {
                pbStream.unread(data, size, read - size);
            }
            
            this.input = new InputStreamReader(pbStream, enc);
        }
        
        public String getEncoding()
        {
            return input.getEncoding();
        }
        
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException
        {
            return input.read(cbuf, off, len);
        }
        
        @Override
        public void close() throws IOException
        {
            input.close();
        }
    }
    
    public boolean hasChanged()
    {
        if (changed)
            return true;
        
        for (ConfigCategory cat : categories.values())
        {
            if (cat.hasChanged())
                return true;
        }
        
        for (Configuration child : children.values())
        {
            if (child.hasChanged())
                return true;
        }
        
        return false;
    }
    
    private void resetChangedState()
    {
        changed = false;
        for (ConfigCategory cat : categories.values())
        {
            cat.resetChangedState();
        }
        
        for (Configuration child : children.values())
        {
            child.resetChangedState();
        }
    }
    
    public Set<String> getCategoryNames()
    {
        return ImmutableSet.copyOf(categories.keySet());
    }
    
    /**
     * Renames a property in a given category.
     * 
     * @param category the category in which the property resides
     * @param oldPropName the existing property name
     * @param newPropName the new property name
     * @return true if the category and property exist, false otherwise
     */
    public boolean renameProperty(String category, String oldPropName, String newPropName)
    {
        if (hasCategory(category))
        {
            if (getCategory(category).containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName))
            {
                get(category, newPropName, getCategory(category).get(oldPropName).getString(), "");
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
    public boolean moveProperty(String oldCategory, String propName, String newCategory)
    {
        if (hasCategory(oldCategory))
        {
            if (getCategory(oldCategory).containsKey(propName))
            {
                getCategory(newCategory).put(propName, getCategory(oldCategory).remove(propName));
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
    public String getString(String name, String category, String defaultValue, String comment)
    {
        return getString(name, category, defaultValue, comment, name, null);
    }
    
    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new string property.
     */
    public String getString(String name, String category, String defaultValue, String comment, String langKey)
    {
        return getString(name, category, defaultValue, comment, langKey, null);
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
    public String getString(String name, String category, String defaultValue, String comment, Pattern pattern)
    {
        return getString(name, category, defaultValue, comment, name, pattern);
    }
    
    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new string property.
     */
    public String getString(String name, String category, String defaultValue, String comment, String langKey, Pattern pattern)
    {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setValidStringPattern(pattern);
        prop.comment = comment + " [default: " + defaultValue + "]";
        return prop.getString();
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
    public String getString(String name, String category, String defaultValue, String comment, String[] validValues)
    {
        return getString(name, category, defaultValue, comment, validValues, name);
    }
    
    /**
     * Creates a string property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @param validValues A list of valid values that this property can be set to.
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new string property.
     */
    public String getString(String name, String category, String defaultValue, String comment, String[] validValues, String langKey)
    {
        Property prop = this.get(category, name, defaultValue);
        prop.setValidValues(validValues);
        prop.setLanguageKey(langKey);
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
    public String[] getStringList(String name, String category, String[] defaultValues, String comment)
    {
        return getStringList(name, category, defaultValues, comment, (String[]) null, name);
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
    public String[] getStringList(String name, String category, String[] defaultValue, String comment, String[] validValues)
    {
        return getStringList(name, category, defaultValue, comment, validValues, name);
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
    public String[] getStringList(String name, String category, String[] defaultValue, String comment, String[] validValues, String langKey)
    {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setValidValues(validValues);
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
    public boolean getBoolean(String name, String category, boolean defaultValue, String comment)
    {
        return getBoolean(name, category, defaultValue, comment, name);
    }
    
    /**
     * Creates a boolean property.
     * 
     * @param name Name of the property.
     * @param category Category of the property.
     * @param defaultValue Default value of the property.
     * @param comment A brief description what the property does.
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new boolean property.
     */
    public boolean getBoolean(String name, String category, boolean defaultValue, String comment, String langKey)
    {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
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
    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment)
    {
        return getInt(name, category, defaultValue, minValue, maxValue, comment, name);
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
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new integer property.
     */
    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment, String langKey)
    {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
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
    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment)
    {
        return getFloat(name, category, defaultValue, minValue, maxValue, comment, name);
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
     * @param langKey A language key used for localization of GUIs
     * @return The value of the new float property.
     */
    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment, String langKey)
    {
        Property prop = this.get(category, name, Float.toString(defaultValue), name);
        prop.setLanguageKey(langKey);
        prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
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
    
    public File getConfigFile()
    {
        return file;
    }
}