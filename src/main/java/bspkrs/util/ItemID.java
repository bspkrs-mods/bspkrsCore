package bspkrs.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemID
{
    public Item item;
    public int  damage;
    
    public ItemID(Item item, int damage)
    {
        this.item = item;
        this.damage = damage;
    }
    
    public ItemID(ItemStack itemStack, int damage)
    {
        this(itemStack.getItem(), damage);
    }
    
    public ItemID(ItemStack itemStack)
    {
        this(itemStack.getItem(), -1);
    }
    
    public ItemID(Item item)
    {
        this(item, -1);
    }
    
    @Override
    public ItemID clone()
    {
        return new ItemID(item, damage);
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
            return item.equals(o.item);
        else
            return item.equals(o.item) && damage == o.damage;
    }
    
    @Override
    public int hashCode()
    {
        int result = 31;
        result = HashCodeUtil.hash(result, item);
        result = HashCodeUtil.hash(result, damage);
        return result;
    }
}
