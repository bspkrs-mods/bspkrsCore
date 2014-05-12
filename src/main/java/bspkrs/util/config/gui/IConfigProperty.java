package bspkrs.util.config.gui;

import java.util.List;
import java.util.regex.Pattern;

import bspkrs.util.config.gui.GuiPropertyList.IGuiConfigListEntry;

public interface IConfigProperty
{
    /**
     * [Property, Category, Custom] Is this object a property object?
     * 
     * @return true if this is a property object, false if the object is a category object or a custom object
     */
    public boolean isProperty();
    
    /**
     * [Property, Category, Custom] Does this object have a custom entry? If true is returned {@code getCustomEntry()} MUST return an object
     * that implements {@code IGuiConfigListEntry}.
     * 
     * @return true if this object will use a custom IGuiConfigListEntry class, false otherwise.
     */
    public boolean hasCustomIGuiConfigListEntry();
    
    /**
     * If {@code isCustomEntry()} returns true, this method MUST return a class that implements {@code IGuiConfigListEntry}. This class MUST
     * provide a constructor with the following parameter types: {@code GuiConfig}, {@code GuiPropertyList}, {@code IConfigProperty}
     * 
     * @return a custom implementation of {@code IGuiConfigListEntry}
     */
    public Class<? extends IGuiConfigListEntry> getCustomIGuiConfigListEntryClass();
    
    /**
     * [Property, Category] Gets the name of this object.
     * 
     * @return the name of this object.
     */
    public String getName();
    
    /**
     * [Category] Gets the qualified name of this object. This is typically only used for category objects.
     * 
     * @return the qualified name of this category object.
     */
    public String getQualifiedName();
    
    /**
     * [Property, Category] Gets a language key for localization of config GUI entry names. If the same key is specified with .tooltip
     * appended to the end, that key will return a localized tooltip when the mouse hovers over the property label/category button.
     * 
     * @return A language key for this property/category.
     */
    public String getLanguageKey();
    
    /**
     * [Property, Category] Gets the comment for this object. Used for the tooltip if getLanguageKey() + ".tooltip" is not defined in the
     * .lang file.
     * 
     * @return the comment for this object.
     */
    public String getComment();
    
    /**
     * [Category] Gets this category's child categories/properties. For best results this method should return a List with the child
     * categories ordered before the child properties.
     * 
     * @return This category's child categories/properties.
     */
    public List<IConfigProperty> getConfigPropertiesList();
    
    /**
     * [Property, Category] Gets the ConfigGuiType value corresponding to the type of this property object, or CONFIG_CATEGORY if this is a
     * category object.
     * 
     * @return the ConfigGuiType value corresponding to the type of this property object, or CONFIG_CATEGORY if this is a category object.
     */
    public ConfigGuiType getType();
    
    /**
     * [Property] Is this property object a list?
     * 
     * @return true if this property object is a list, false otherwise.
     */
    public boolean isList();
    
    /**
     * [Property] Does this list property have to remain a fixed length?
     * 
     * @return true if the list length must remain constant, false if the list length can vary
     */
    public boolean isListLengthFixed();
    
    /**
     * [Property] Gets the max length of this list property.
     * 
     * @return the maximum length of this list property, or -1 if the length is unlimited.
     */
    public int getMaxListLength();
    
    /**
     * [Property] Is this property value equal to the default value?
     * 
     * @return true if the property value is equal to the default value, false otherwise.
     */
    public boolean isDefault();
    
    /**
     * [Property] Gets the String representation of this property's default value.
     * 
     * @return The String representation of this property's default value.
     */
    public String getDefault();
    
    /**
     * [Property] Gets the String[] representation of this property's default value.
     * 
     * @return The String representation of this property's default value.
     */
    public String[] getDefaults();
    
    /**
     * [Property] Sets this property's value to the default value.
     */
    public void setToDefault();
    
    /**
     * [Property, Category] Whether or not this property is safe to modify while a world is running.
     * 
     * @return true if this property can be modified while a world is running, false otherwise. For Categories return true if ALL properties
     *         in the category are modifiable while a world is running, false if any are not hot loadable.
     */
    public boolean isHotLoadable();
    
    /**
     * [Property] Gets this property value as a boolean. Generally you should be sure of the type before calling this.
     * 
     * @return This property value as a boolean.
     */
    public boolean getBoolean();
    
    /**
     * [Property] Gets this property value as a int. Generally you should be sure of the type before calling this.
     * 
     * @return This property value as a int.
     */
    public int getInt();
    
    /**
     * [Property] Gets this property value as a String.
     * 
     * @return This property value as a String.
     */
    public String getString();
    
    /**
     * [Property] Gets this property value as a double. Generally you should be sure of the type before calling this.
     * 
     * @return This property value as a double.
     */
    public double getDouble();
    
    /**
     * [Property] Gets this property value as a boolean list. Generally you should be sure of the type and whether the property is a list
     * before calling this.
     * 
     * @return This property value as a boolean list.
     */
    public boolean[] getBooleanList();
    
    /**
     * [Property] Gets this property value as a int list. Generally you should be sure of the type and whether the property is a list before
     * calling this.
     * 
     * @return This property value as a int list.
     */
    public int[] getIntList();
    
    /**
     * [Property] Gets this property value as a String list. Generally you should be sure the property is a list before calling this.
     * 
     * @return This property value as a String list.
     */
    public String[] getStringList();
    
    /**
     * [Property] Gets this property value as a double list. Generally you should be sure of the type and whether the property is a list
     * before calling this.
     * 
     * @return This property value as a double list.
     */
    public double[] getDoubleList();
    
    /**
     * [Property] Sets this property's value to the specified boolean.
     * 
     * @param bol
     */
    public void set(boolean bol);
    
    /**
     * [Property] Sets this property's value to the specified int.
     * 
     * @param i
     */
    public void set(int i);
    
    /**
     * [Property] Sets this property's value to the specified String.
     * 
     * @param s
     */
    public void set(String s);
    
    /**
     * [Property] Sets this property's value to the specified double.
     * 
     * @param d
     */
    public void set(double d);
    
    /**
     * [Property] Sets this property's value to the specified boolean array.
     * 
     * @param abol
     */
    public void set(boolean[] abol);
    
    /**
     * [Property] Sets this property's value to the specified int array.
     * 
     * @param ai
     */
    public void set(int[] ai);
    
    /**
     * [Property] Sets this property's value to the specified String array.
     * 
     * @param as
     */
    public void set(String[] as);
    
    /**
     * [Property] Sets this property's value to the specified double array.
     * 
     * @param ad
     */
    public void set(double[] ad);
    
    /**
     * [Property] Gets a String array of valid values for this property. This is generally used for String properties to allow the user to
     * select a value from a list of valid values.
     * 
     * @return A String array of valid values for this property.
     */
    public String[] getValidValues();
    
    /**
     * [Property] Gets this property's minimum value as an int.
     * 
     * @return This property's minimum value as an int.
     */
    public int getMinIntValue();
    
    /**
     * [Property] Gets this property's maximum value as an int.
     * 
     * @return This property's maximum value as an int.
     */
    public int getMaxIntValue();
    
    /**
     * [Property] Gets this property's minimum value as a double.
     * 
     * @return This property's minimum value as a double.
     */
    public double getMinDoubleValue();
    
    /**
     * [Property] Gets this property's maximum value as a double.
     * 
     * @return This property's maximum value as a double.
     */
    public double getMaxDoubleValue();
    
    /**
     * [Property] Gets a Pattern object used in String property input validation.
     * 
     * @return A Pattern object used in String property input validation.
     */
    public Pattern getValidStringPattern();
    
}
