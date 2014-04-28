package bspkrs.util.config.gui;

import java.util.regex.Pattern;

public interface IConfigProperty
{
    public boolean isProperty();
    
    public String getName();
    
    public String getQualifiedName();
    
    public ConfigGuiType getType();
    
    public boolean isList();
    
    public String getComment();
    
    public boolean isDefault();
    
    public String getDefault();
    
    public void setToDefault();
    
    public boolean getBoolean();
    
    public int getInt();
    
    public String getString();
    
    public double getDouble();
    
    public boolean[] getBooleanList();
    
    public int[] getIntList();
    
    public String[] getStringList();
    
    public double[] getDoubleList();
    
    public void set(boolean bol);
    
    public void set(int i);
    
    public void set(String s);
    
    public void set(double d);
    
    public void set(boolean[] bol);
    
    public void set(int[] i);
    
    public void set(String[] s);
    
    public void set(double[] d);
    
    public String[] getValidValues();
    
    public String getLanguageKey();
    
    public int getMinIntValue();
    
    public int getMaxIntValue();
    
    public double getMinDoubleValue();
    
    public double getMaxDoubleValue();
    
    public IConfigProperty[] getConfigProperties();
    
    public Pattern getValidStringPattern();
    
}
