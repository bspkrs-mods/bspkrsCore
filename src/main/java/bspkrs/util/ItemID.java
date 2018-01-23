package bspkrs.util;

import net.minecraft.item.*;

public class ItemID
{
    public final String id;
    public final int damage;

    public ItemID(final String id, final int damage)
    {
        this.id = id;
        this.damage = damage;
    }

    public ItemID(final String id)
    {
        this(id, -1);
    }

    public ItemID(final ItemStack itemStack, final int damage)
    {
        this(itemStack.getItem(), damage);
    }

    public ItemID(final ItemStack itemStack)
    {
        this(itemStack.getItem(), -1);
    }

    public ItemID(final Item item, final int damage)
    {
        this(Item.REGISTRY.getNameForObject(item).toString(), damage);
    }

    public ItemID(final Item item)
    {
        this(Item.REGISTRY.getNameForObject(item).toString(), -1);
    }

    public ItemID(final String format, final String delimiter)
    {
        final String[] parts = format.split(delimiter);
        if(parts.length > 1)
        {
            this.id = parts[0].trim();
            this.damage = CommonUtils.parseInt(parts[1], -1);
        }
        else
        {
            this.id = parts[0].trim();
            this.damage = -1;
        }
    }

    public ItemID clone()
    {
        return new ItemID(this.id, this.damage);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof ItemID))
        {
            return false;
        }
        final ItemID o = (ItemID)obj;
        if(o.damage == -1 || this.damage == -1)
        {
            return (this.id != null) ? this.id.equals(o.id) : (o.id == null);
        }
        return (this.id != null) ? (this.id.equals(o.id) && this.damage == o.damage) : (o.id == null && this.damage == o.damage);
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode() * 31;
    }

    @Override
    public String toString()
    {
        return (this.damage == -1) ? (this.id + "") : (this.id + ", " + this.damage);
    }
}
