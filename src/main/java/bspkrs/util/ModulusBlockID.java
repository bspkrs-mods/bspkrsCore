package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ModulusBlockID extends BlockID
{
    public final int metadataModulus;

    public ModulusBlockID(String id, int metadata, int metadataModulus)
    {
        super(id, Math.max(metadata, 0) % Math.max(metadataModulus, 1));
        this.metadataModulus = Math.max(metadataModulus, 1);
    }

    public ModulusBlockID(Block block, int metadata, int metadataModulus)
    {
        super(block, Math.max(metadata, 0) % Math.max(metadataModulus, 1));
        this.metadataModulus = Math.max(metadataModulus, 1);
    }

    public ModulusBlockID(World world, int x, int y, int z, int metadataModulus)
    {
        this(world, x, y, z, world.getBlockMetadata(x, y, z), metadataModulus);
    }

    public ModulusBlockID(World world, int x, int y, int z, int metadata, int metadataModulus)
    {
        this(world.getBlock(x, y, z), Math.max(metadata, 0), metadataModulus);
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

        if (((BlockID) obj).id != null && !((BlockID) obj).id.equals(this.id))
            return false;
        else if (((BlockID) obj).id == null && this.id != null)
            return false;

        if (obj instanceof ModulusBlockID)
        {
            ModulusBlockID o = (ModulusBlockID) obj;
            return metadata % metadataModulus == o.metadata % o.metadataModulus;
        }
        else
        {
            BlockID o = (BlockID) obj;
            if (o.metadata == -1)
                return true;
            else
                return metadata % metadataModulus == o.metadata % metadataModulus;
        }
    }

    @Override
    public String toString()
    {
        return id + ", " + metadata + " % " + metadataModulus;
    }
}
