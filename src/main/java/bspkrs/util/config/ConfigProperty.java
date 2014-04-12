package bspkrs.util.config;

import java.util.ArrayList;
import java.util.List;

import bspkrs.util.config.Property.Type;
import bspkrs.util.config.gui.IConfigProperty;

public class ConfigProperty implements IConfigProperty
{
    private Property prop;
    
    public ConfigProperty(Property prop)
    {
        this.prop = prop;
    }
    
    @Override
    public String getName()
    {
        return prop.getName();
    }
    
    @Override
    public Class getType()
    {
        if (prop.getType().equals(Type.BOOLEAN))
            return Boolean.class;
        else if (prop.getType().equals(Type.DOUBLE))
            return double.class;
        else if (prop.getType().equals(Type.INTEGER))
            return int.class;
        else
            return String.class;
    }
    
    @Override
    public List getToolTip()
    {
        List r = new ArrayList();
        r.add(prop.comment);
        return r;
    }
    
    @Override
    public boolean isDefault()
    {
        return prop.isDefault();
    }
    
    @Override
    public void setToDefault()
    {
        prop.setToDefault();
    }
    
    @Override
    public boolean getBoolean()
    {
        return prop.getBoolean();
    }
    
    @Override
    public int getInt()
    {
        return prop.getInt();
    }
    
    @Override
    public String getString()
    {
        return prop.getString();
    }
    
    @Override
    public double getDouble()
    {
        return prop.getDouble();
    }
    
    @Override
    public void set(boolean bol)
    {
        prop.set(bol);
    }
    
    @Override
    public void set(int i)
    {
        prop.set(i);
    }
    
    @Override
    public void set(String s)
    {
        prop.set(s);
    }
    
    @Override
    public void set(double d)
    {
        prop.set(d);
    }
    
    @Override
    public String[] getValidValues()
    {
        return prop.getValidValues();
    }
    
    @Override
    public String getLanguageKey()
    {
        return prop.getLanguageKey();
    }
    
    @Override
    public String getDefault()
    {
        return prop.getDefault();
    }
}
