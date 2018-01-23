package bspkrs.util;

import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class BlockID
{
    public final String id;
    public final int metadata;

    public BlockID(final String id, final int metadata)
    {
        this.id = id;
        this.metadata = metadata;
    }

    public BlockID(final String block)
    {
        this(block, -1);
    }

    @SuppressWarnings("deprecation")
    public BlockID(final Block block, final int metadata)
    {
        this(Block.REGISTRY.getNameForObject(block.getStateFromMeta(metadata).getBlock()).toString());
    }

    @SuppressWarnings("deprecation")
    public BlockID(final Block block)
    {
        this(Block.REGISTRY.getNameForObject(block.getStateFromMeta(-1).getBlock()).toString());
    }

    public BlockID(final Block block, final IBlockState state)
    {
        this(block, block.getMetaFromState(state));
    }

    public BlockID(final IBlockState state)
    {
        this(state.getBlock(), state);
    }

    public BlockID(final World world, final BlockPos pos)
    {
        this(world.getBlockState(pos));
    }

    public BlockID(final World world, final BlockPos pos, final int metadata)
    {
        this(world.getBlockState(pos).getBlock(), metadata);
    }

    public boolean isValid()
    {
        return this.getBlock() != null;
    }

    public Block getBlock()
    {
        return Block.REGISTRY.getObject(new ResourceLocation(this.id));
    }

    public static BlockID parse(String format)
    {
        int metadataModulus = 0;
        format = format.trim();
        final int comma = format.indexOf(",");
        int tilde = format.indexOf("~");
        if(tilde == -1)
        {
            tilde = format.indexOf("%");
        }
        if(comma == -1 && tilde != -1)
        {
            throw new RuntimeException(String.format("ModulusBlockID format error: a \"~\" or \"%1$s\" was found, but no \",\" in format \"%2$s\". Expected format is \"<blockidstring>, <integer metadata> %1$s <integer modulus>\". EG: \"minecraft:log, 0 %1$s 4\".", "%", format));
        }
        if(tilde != -1 && comma > tilde)
        {
            throw new RuntimeException(String.format("ModulusBlockID format error: a \"~\" or \"%1$s\" was found before a \",\" in format \"%2$s\". Expected format is \"<blockidstring>, <integer metadata> %1$s <integer modulus>\". EG: \"minecraft:log, 0 %1$s 4\".", "%", format));
        }
        if(tilde == -1)
        {
            tilde = format.length();
        }
        String id;
        if(comma != -1)
        {
            id = format.substring(0, comma).trim();
        }
        else
        {
            id = format.trim();
        }
        final int metadata = CommonUtils.parseInt(format.substring(comma + 1, tilde).trim(), -1);
        if(tilde != format.length())
        {
            metadataModulus = CommonUtils.parseInt(format.substring(tilde + 1, format.length()).trim(), 0);
        }
        if(metadata != -1 && metadataModulus > 0)
        {
            return new ModulusBlockID(id, metadata, metadataModulus);
        }
        return new BlockID(id, metadata);
    }

    public BlockID clone()
    {
        return new BlockID(this.id, this.metadata);
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
            return this.metadata % o.metadataModulus == o.metadata % o.metadataModulus;
        }
        final BlockID o2 = (BlockID)obj;
        return o2.metadata == -1 || this.metadata == -1 || this.metadata == o2.metadata;
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode() * 37;
    }

    @Override
    public String toString()
    {
        return (this.metadata == -1) ? this.id : (this.id + ", " + this.metadata);
    }
}
