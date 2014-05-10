package bspkrs.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import bspkrs.helpers.item.ItemHelper;

public class ItemID
{
    // Unique ID of the item from the namespace registry
    public String id;
    public int    damage;
    
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
        this(ItemHelper.getUniqueID(item), damage);
    }
    
    public ItemID(Item item)
    {
        this(ItemHelper.getUniqueID(item), -1);
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
            return id.equals(o.id);
        else
            return id.equals(o.id) && damage == o.damage;
    }
    
    @Override
    public int hashCode()
    {
        int result = 31;
        result = HashCodeUtil.hash(result, id);
        return result;
    }
    
    @Override
    public String toString()
    {
        return (damage == -1 ? id + "" : id + ", " + damage);
    }
}
