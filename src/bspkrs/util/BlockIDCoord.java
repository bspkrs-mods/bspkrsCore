package bspkrs.util;

import net.minecraft.tileentity.TileEntity;

public class BlockIDCoord
{
    public BlockID    blockID;
    public Coord      pos;
    public TileEntity tileEntity;
    
    public BlockIDCoord(BlockID blockID, Coord pos)
    {
        this(blockID, pos, null);
    }
    
    public BlockIDCoord(BlockID blockID, Coord pos, TileEntity tileEntity)
    {
        this.blockID = blockID;
        this.pos = pos;
        this.tileEntity = tileEntity;
    }
}
