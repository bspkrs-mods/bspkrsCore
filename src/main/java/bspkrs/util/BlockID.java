package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import bspkrs.helpers.world.WorldHelper;

public class BlockID
{
    public Block block;
    public int   metadata;
    
    public BlockID(Block block, int metadata)
    {
        this.block = block;
        this.metadata = metadata;
    }
    
    public BlockID(Block block)
    {
        this(block, -1);
    }
    
    public BlockID(World world, int x, int y, int z)
    {
        this(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    public BlockID(World world, int x, int y, int z, int metadata)
    {
        this(WorldHelper.getBlock(world, x, y, z), metadata);
    }
    
    @Override
    public BlockID clone()
    {
        return new BlockID(block, metadata);
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
            return block.equals(o.block);
        else
            return block.equals(o.block) && metadata == o.metadata;
    }
    
    @Override
    public int hashCode()
    {
        int result = 23;
        result = HashCodeUtil.hash(result, block);
        result = HashCodeUtil.hash(result, metadata);
        return result;
    }
    
    @Override
    public String toString()
    {
        return (metadata == -1 ? block + "" : block + ", " + metadata);
    }
}
