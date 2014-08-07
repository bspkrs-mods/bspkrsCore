package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;

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
        this(GameData.getBlockRegistry().getNameForObject(block), metadata);
    }
    
    public BlockID(Block block)
    {
        this(GameData.getBlockRegistry().getNameForObject(block), -1);
    }
    
    @Deprecated
    public BlockID(String format, String delimiter)
    {
        int comma = format.indexOf(",");
        
        if (comma != -1)
            this.id = format.substring(0, comma).trim();
        else
            this.id = format.trim();
        
        this.metadata = CommonUtils.parseInt(format.substring(comma + 1, format.length()).trim(), -1);
    }
    
    public BlockID(World world, int x, int y, int z)
    {
        this(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    public BlockID(World world, int x, int y, int z, int metadata)
    {
        this(world.getBlock(x, y, z), metadata);
    }
    
    public boolean isValid()
    {
        return getBlock() != null;
    }
    
    public Block getBlock()
    {
        return GameData.getBlockRegistry().getObject(id);
    }
    
    public static BlockID parse(String format)
    {
        String id;
        int metadata;
        int metadataModulus = 0;
        format = format.trim();
        int comma = format.indexOf(",");
        int tilde = format.indexOf("~");
        if (tilde == -1)
            tilde = format.indexOf("%");
        
        if (comma == -1 && tilde != -1)
            throw new RuntimeException(String.format("ModulusBlockID format error: a \"~\" or \"%1$s\" was found, but no \",\" in format \"%2$s\". " +
                    "Expected format is \"<blockidstring>, <integer metadata> %1$s <integer modulus>\". EG: \"minecraft:log, 0 %1$s 4\".", "%", format));
        
        if (tilde != -1 && comma > tilde)
            throw new RuntimeException(String.format("ModulusBlockID format error: a \"~\" or \"%1$s\" was found before a \",\" in format \"%2$s\". " +
                    "Expected format is \"<blockidstring>, <integer metadata> %1$s <integer modulus>\". EG: \"minecraft:log, 0 %1$s 4\".", "%", format));
        
        if (tilde == -1)
            tilde = format.length();
        
        if (comma != -1)
            id = format.substring(0, comma).trim();
        else
            id = format.trim();
        
        metadata = CommonUtils.parseInt(format.substring(comma + 1, tilde).trim(), -1);
        if (tilde != format.length())
            metadataModulus = CommonUtils.parseInt(format.substring(tilde + 1, format.length()).trim(), 0);
        
        if (metadata != -1 && metadataModulus > 0)
            return new ModulusBlockID(id, metadata, metadataModulus);
        else
            return new BlockID(id, metadata);
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
        
        if (((BlockID) obj).id != null && !((BlockID) obj).id.equals(this.id))
            return false;
        else if (((BlockID) obj).id == null && this.id != null)
            return false;
        
        if (obj instanceof ModulusBlockID)
        {
            ModulusBlockID o = (ModulusBlockID) obj;
            return metadata % o.metadataModulus == o.metadata % o.metadataModulus;
        }
        else
        {
            BlockID o = (BlockID) obj;
            if (o.metadata == -1 || metadata == -1)
                return true;
            else
                return metadata == o.metadata;
        }
    }
    
    @Override
    public int hashCode()
    {
        return id.hashCode() * 37;
    }
    
    @Override
    public String toString()
    {
        return (metadata == -1 ? id + "" : id + ", " + metadata);
    }
}
