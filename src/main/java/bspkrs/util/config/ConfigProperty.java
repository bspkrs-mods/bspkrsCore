package bspkrs.util.config;

import java.util.Iterator;
import java.util.regex.Pattern;

import bspkrs.util.config.gui.ConfigGuiType;
import bspkrs.util.config.gui.IConfigProperty;

public class ConfigProperty implements IConfigProperty
{
    private Property       prop;
    private Property.Type  type;
    private boolean        isProperty;
    private ConfigCategory ctgy;
    
    public ConfigProperty(ConfigCategory ctgy)
    {
        this.ctgy = ctgy;
        isProperty = false;
    }
    
    public ConfigProperty(Property prop)
    {
        this(prop, prop.getType());
    }
    
    public ConfigProperty(Property prop, Property.Type type)
    {
        this.prop = prop;
        this.type = type;
        this.isProperty = true;
    }
    
    @Override
    public IConfigProperty[] getConfigProperties()
    {
        if (!isProperty)
        {
            IConfigProperty[] props = new ConfigProperty[ctgy.getValues().size() + ctgy.getChildren().size()];
            Iterator<ConfigCategory> ccI = ctgy.getChildren().iterator();
            Iterator<Property> pI = ctgy.getValues().values().iterator();
            int index = 0;
            while (ccI.hasNext())
                props[index++] = new ConfigProperty(ccI.next());
            while (pI.hasNext())
                props[index++] = new ConfigProperty(pI.next());
            return props;
        }
        return new IConfigProperty[] {};
    }
    
    @Override
    public String getName()
    {
        if (isProperty)
            return prop.getName();
        else
            return ctgy.getName();
    }
    
    @Override
    public boolean isProperty()
    {
        return isProperty;
    }
    
    @Override
    public String getQualifiedName()
    {
        if (isProperty)
            return prop.getName();
        else
            return ctgy.getQualifiedName();
    }
    
    @Override
    public ConfigGuiType getType()
    {
        if (isProperty)
        {
            if (type.equals(Property.Type.BOOLEAN))
                return ConfigGuiType.BOOLEAN;
            else if (type.equals(Property.Type.DOUBLE))
                return ConfigGuiType.DOUBLE;
            else if (type.equals(Property.Type.INTEGER))
                return ConfigGuiType.INTEGER;
            else if (type.equals(Property.Type.COLOR))
                return ConfigGuiType.COLOR;
            else if (type.equals(Property.Type.BLOCK_LIST))
                return ConfigGuiType.BLOCK_LIST;
            else if (type.equals(Property.Type.ITEMSTACK_LIST))
                return ConfigGuiType.ITEMSTACK_LIST;
            else if (type.equals(Property.Type.ENTITY_LIST))
                return ConfigGuiType.ENTITY_LIST;
            else if (type.equals(Property.Type.BIOME_LIST))
                return ConfigGuiType.BIOME_LIST;
            else if (type.equals(Property.Type.DIMENSION_LIST))
                return ConfigGuiType.DIMENSION_LIST;
            else
                return ConfigGuiType.STRING;
        }
        else
            return ConfigGuiType.CONFIG_CATEGORY;
    }
    
    @Override
    public String getComment()
    {
        if (isProperty)
            return prop.comment;
        else
            return ctgy.getComment();
    }
    
    @Override
    public boolean isDefault()
    {
        if (isProperty)
            return prop.isDefault();
        else
            return true;
    }
    
    @Override
    public void setToDefault()
    {
        if (isProperty)
            prop.setToDefault();
    }
    
    @Override
    public boolean getBoolean()
    {
        if (isProperty)
            return prop.getBoolean();
        else
            return false;
    }
    
    @Override
    public int getInt()
    {
        if (isProperty)
            return prop.getInt();
        else
            return 0;
    }
    
    @Override
    public String getString()
    {
        if (isProperty)
            return prop.getString();
        else
            return ctgy.getQualifiedName();
    }
    
    @Override
    public double getDouble()
    {
        if (isProperty)
            return prop.getDouble();
        else
            return 0.0F;
    }
    
    @Override
    public void set(boolean bol)
    {
        if (isProperty)
            prop.set(bol);
    }
    
    @Override
    public void set(int i)
    {
        if (isProperty)
            prop.set(i);
    }
    
    @Override
    public void set(String s)
    {
        if (isProperty)
            prop.set(s);
    }
    
    @Override
    public void set(double d)
    {
        if (isProperty)
            prop.set(d);
    }
    
    @Override
    public String[] getValidValues()
    {
        if (isProperty)
            return prop.getValidValues();
        else
            return new String[] {};
    }
    
    @Override
    public String getLanguageKey()
    {
        if (isProperty)
            return prop.getLanguageKey();
        else
            return ctgy.getLanguagekey();
    }
    
    @Override
    public String getDefault()
    {
        if (isProperty)
            return prop.getDefault();
        else
            return "";
    }
    
    @Override
    public int getMinIntValue()
    {
        if (isProperty)
            return prop.getMinIntValue();
        else
            return 0;
    }
    
    @Override
    public int getMaxIntValue()
    {
        if (isProperty)
            return prop.getMaxIntValue();
        else
            return 0;
    }
    
    @Override
    public double getMinDoubleValue()
    {
        if (isProperty)
            return prop.getMinDoubleValue();
        else
            return 0.0F;
    }
    
    @Override
    public double getMaxDoubleValue()
    {
        if (isProperty)
            return prop.getMaxDoubleValue();
        else
            return 0.0F;
    }
    
    @Override
    public Pattern getValidStringPattern()
    {
        return prop.getValidStringPattern();
    }
}
