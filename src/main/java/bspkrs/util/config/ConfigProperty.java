package bspkrs.util.config;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import bspkrs.util.config.gui.IConfigProperty;

public class ConfigProperty implements IConfigProperty
{
    private Property      prop;
    private Property.Type type;
    
    public ConfigProperty(Property prop)
    {
        this(prop, prop.getType());
    }
    
    public ConfigProperty(Property prop, Property.Type type)
    {
        this.prop = prop;
        this.type = type;
    }
    
    @Override
    public String getName()
    {
        return prop.getName();
    }
    
    @Override
    public Class getType()
    {
        if (type.equals(Property.Type.BOOLEAN))
            return boolean.class;
        else if (type.equals(Property.Type.DOUBLE))
            return double.class;
        else if (type.equals(Property.Type.INTEGER))
            return int.class;
        else if (type.equals(Property.Type.COLOR))
            return EnumChatFormatting.class;
        else if (type.equals(Property.Type.BLOCK_LIST))
            return Blocks.class;
        else if (type.equals(Property.Type.ITEMSTACK_LIST))
            return Items.class;
        else if (type.equals(Property.Type.ENTITY_LIST))
            return EntityList.class;
        else if (type.equals(Property.Type.BIOME_LIST))
            return BiomeGenBase.class;
        else if (type.equals(Property.Type.DIMENSION_LIST))
            return WorldProvider.class;
        else
            return String.class;
    }
    
    @Override
    public String getComment()
    {
        return prop.comment;
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
    
    @Override
    public int getMinIntValue()
    {
        return prop.getMinIntValue();
    }
    
    @Override
    public int getMaxIntValue()
    {
        return prop.getMaxIntValue();
    }
    
    @Override
    public double getMinDoubleValue()
    {
        return prop.getMinDoubleValue();
    }
    
    @Override
    public double getMaxDoubleValue()
    {
        return prop.getMaxDoubleValue();
    }
}
