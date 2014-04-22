package bspkrs.util.config.gui;


public interface IConfigProperty
{
    public boolean isProperty();
    
    public String getName();
    
    public String getQualifiedName();
    
    public Class getType();
    
    public String getComment();
    
    public boolean isDefault();
    
    public String getDefault();
    
    public void setToDefault();
    
    public boolean getBoolean();
    
    public int getInt();
    
    public String getString();
    
    public double getDouble();
    
    public void set(boolean bol);
    
    public void set(int i);
    
    public void set(String s);
    
    public void set(double d);
    
    public String[] getValidValues();
    
    public String getLanguageKey();
    
    public int getMinIntValue();
    
    public int getMaxIntValue();
    
    public double getMinDoubleValue();
    
    public double getMaxDoubleValue();
    
    public IConfigProperty[] getConfigProperties();
    
}
