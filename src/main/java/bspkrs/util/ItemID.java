package bspkrs.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameData;

public class ItemID
{
    /**
     * Unique ID of the item from the namespace registry
     */
    public final String id;
    public final int    damage;
    
    public ItemID(String id, int damage)
    {
        this.id = id;
        this.damage = damage;
    }
    
    public ItemID(String id)
    {
        this(id, -1);
    }
    
    public ItemID(ItemStack itemStack, int damage)
    {
        this(itemStack.getItem(), damage);
    }
    
    public ItemID(ItemStack itemStack)
    {
        this(itemStack.getItem(), -1);
    }
    
    public ItemID(Item item, int damage)
    {
        this(GameData.getItemRegistry().getNameForObject(item), damage);
    }
    
    public ItemID(Item item)
    {
        this(GameData.getItemRegistry().getNameForObject(item), -1);
    }
    
    public ItemID(String format, String delimiter)
    {
        String[] parts = format.split(delimiter);
        
        if (parts.length > 1)
        {
            id = parts[0].trim();
            damage = CommonUtils.parseInt(parts[1], -1);
        }
        else
        {
            id = parts[0].trim();
            damage = -1;
        }
    }
    
    @Override
    public ItemID clone()
    {
        return new ItemID(id, damage);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof ItemID))
            return false;
        
        ItemID o = (ItemID) obj;
        if (o.damage == -1 || damage == -1)
            return id != null ? id.equals(o.id) : o.id == null;
        else
            return id != null ? id.equals(o.id) && damage == o.damage : o.id == null && damage == o.damage;
    }
    
    @Override
    public int hashCode()
    {
        return id.hashCode() * 31;
    }
    
    @Override
    public String toString()
    {
        return (damage == -1 ? id + "" : id + ", " + damage);
    }
}
