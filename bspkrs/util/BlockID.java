package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockID
{
    public int id, metadata;
    
    public BlockID(int id, int metadata)
    {
        this.id = id;
        this.metadata = metadata;
    }
    
    public BlockID(net.minecraft.block.Block block, int metadata)
    {
        this(block.blockID, metadata);
    }
    
    public BlockID(Block block)
    {
        this(block.blockID, -1);
    }
    
    public BlockID(int id)
    {
        this(id, -1);
    }
    
    public BlockID(World world, int x, int y, int z)
    {
        this(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    public BlockID(World world, int x, int y, int z, int metadata)
    {
        this(world.getBlockId(x, y, z), metadata);
    }
    
    @Override
    public BlockID clone()
    {
        return new BlockID(this.id, this.metadata);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof BlockID))
            return false;
        
        BlockID o = (BlockID) obj;
        if (o.metadata == -1 || this.metadata == -1)
            return this.id == o.id;
        else
            return this.id == o.id && this.metadata == o.metadata;
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
        return id + ", " + metadata;
    }
}
