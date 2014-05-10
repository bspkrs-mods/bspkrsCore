package bspkrs.util;

import net.minecraft.block.Block;

public class ModulusBlockID extends BlockID
{
    public final int metadataModulus;
    
    public ModulusBlockID(String id, int metadata, int metadataModulus)
    {
        super(id, metadata);
        this.metadataModulus = metadataModulus;
    }
    
    public ModulusBlockID(Block block, int metadata, int metadataModulus)
    {
        super(block, metadata);
        this.metadataModulus = metadataModulus;
    }
    
    @Override
    public BlockID clone()
    {
        return new ModulusBlockID(id, metadata, metadataModulus);
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
            return id.equals(o.id) && metadata % metadataModulus == o.metadata;
    }
    
    @Override
    public String toString()
    {
        return (metadata == -1 ? id + "" : id + ", " + metadata + " % " + metadataModulus);
    }
}
