package bspkrs.util;

import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;

public class ModulusBlockID extends BlockID
{
    public final int metadataModulus;

    public ModulusBlockID(final String id, final int metadata, final int metadataModulus)
    {
        super(id, Math.max(metadata, 0) % Math.max(metadataModulus, 1));
        this.metadataModulus = Math.max(metadataModulus, 1);
    }

    public ModulusBlockID(final Block block, final int metadata, final int metadataModulus)
    {
        super(block, Math.max(metadata, 0) % Math.max(metadataModulus, 1));
        this.metadataModulus = Math.max(metadataModulus, 1);
    }

    public ModulusBlockID(final Block block, final IBlockState state, final int metadataModulus)
    {
        this(block, block.getMetaFromState(state), metadataModulus);
    }

    public ModulusBlockID(final IBlockState state, final int metadataModulus)
    {
        this(state.getBlock(), state, metadataModulus);
    }

    public ModulusBlockID(final World world, final BlockPos pos, final int metadataModulus)
    {
        this(world.getBlockState(pos), metadataModulus);
    }

    public ModulusBlockID(final World world, final BlockPos pos, final int metadata, final int metadataModulus)
    {
        this(world.getBlockState(pos).getBlock(), Math.max(metadata, 0), metadataModulus);
    }

    @Override
    public BlockID clone()
    {
        return new ModulusBlockID(this.id, this.metadata, this.metadataModulus);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof BlockID))
        {
            return false;
        }
        if(((BlockID)obj).id != null && !((BlockID)obj).id.equals(this.id))
        {
            return false;
        }
        if(((BlockID)obj).id == null && this.id != null)
        {
            return false;
        }
        if(obj instanceof ModulusBlockID)
        {
            final ModulusBlockID o = (ModulusBlockID)obj;
            return this.metadata % this.metadataModulus == o.metadata % o.metadataModulus;
        }
        final BlockID o2 = (BlockID)obj;
        return o2.metadata == -1 || this.metadata % this.metadataModulus == o2.metadata % this.metadataModulus;
    }

    @Override
    public String toString()
    {
        return this.id + ", " + this.metadata + " % " + this.metadataModulus;
    }
}
