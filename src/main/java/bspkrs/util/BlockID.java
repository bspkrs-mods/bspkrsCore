package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import bspkrs.helpers.block.BlockHelper;
import bspkrs.helpers.world.WorldHelper;

public class BlockID
{
    public final String id;
    public final int    metadata;
    
    public BlockID(String id, int metadata)
    {
        this.id = id;
        this.metadata = metadata;
    }
    
    public BlockID(String block)
    {
        this(block, -1);
    }
    
    public BlockID(Block block, int metadata)
    {
        this(BlockHelper.getUniqueID(block), metadata);
    }
    
    public BlockID(Block block)
    {
        this(BlockHelper.getUniqueID(block), -1);
    }
    
    public BlockID(String format, String delimiter)
    {
        String[] parts = format.split(delimiter);
        
        if (parts.length > 1)
        {
            id = parts[0].trim();
            metadata = CommonUtils.parseInt(parts[1], -1);
        }
        else
        {
            id = parts[0].trim();
            metadata = -1;
        }
    }
    
    public BlockID(World world, int x, int y, int z)
    {
        this(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    public BlockID(World world, int x, int y, int z, int metadata)
    {
        this(WorldHelper.getBlock(world, x, y, z), metadata);
    }
    
    public boolean isValid()
    {
        return getBlock() != null;
    }
    
    public Block getBlock()
    {
        return BlockHelper.getBlock(id);
    }
    
    @Override
    public BlockID clone()
    {
        return new BlockID(id, metadata);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof BlockID))
            return false;
        
        BlockID o = (BlockID) obj;
        if (o.metadata == -1 || metadata == -1)
            return id.equals(o.id);
        else
            return id.equals(o.id) && metadata == o.metadata;
    }
    
    @Override
    public int hashCode()
    {
        int result = 23;
        result = HashCodeUtil.hash(result, id);
        result = HashCodeUtil.hash(result, metadata);
        return result;
    }
    
    @Override
    public String toString()
    {
        return (metadata == -1 ? id + "" : id + ", " + metadata);
    }
}
