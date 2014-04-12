package bspkrs.util.config.gui;

import java.util.List;

public interface IConfigProperty
{
    public String getName();
    
    public Class getType();
    
    public List getToolTip();
    
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
}
