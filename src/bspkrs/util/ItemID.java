package bspkrs.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemID extends BlockID
{
    public ItemID(int id, int damage)
    {
        super(id, damage);
    }
    
    /**
     * String must be one or two integer values delimited with the delimiter string. ex "17|0","|"
     * 
     * @param format
     * @param delimiter
     */
    public ItemID(String format, String delimiter)
    {
        super(format, delimiter);
    }
    
    /**
     * String must be one or two integer values delimited with a comma. ex "17,0"
     * 
     * @param format
     */
    public ItemID(String format)
    {
        super(format, ",");
    }
    
    public ItemID(Item item, int damage)
    {
        super(item.itemID, damage);
    }
    
    public ItemID(ItemStack itemStack, int damage)
    {
        super(itemStack.getItem().itemID, damage);
    }
    
    public ItemID(ItemStack itemStack)
    {
        super(itemStack.getItem().itemID, -1);
    }
    
    public ItemID(Item item)
    {
        super(item.itemID, -1);
    }
    
    public ItemID(int id)
    {
        super(id, -1);
    }
    
    @Override
    public ItemID clone()
    {
        return new ItemID(id, metadata);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof ItemID))
            return false;
        
        ItemID o = (ItemID) obj;
        if (o.metadata == -1 || metadata == -1)
            return id == o.id;
        else
            return id == o.id && metadata == o.metadata;
    }
    
    @Override
    public int hashCode()
    {
        int result = 31;
        result = HashCodeUtil.hash(result, id);
        result = HashCodeUtil.hash(result, metadata);
        return result;
    }
}
