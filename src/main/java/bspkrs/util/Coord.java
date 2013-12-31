package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import bspkrs.helpers.tileentity.TileEntityHelper;
import bspkrs.helpers.world.WorldHelper;

public class Coord
{
    public int x;
    public int y;
    public int z;
    
    public Coord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Coord clone()
    {
        return new Coord(x, y, z);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof Coord))
            return false;
        
        Coord o = (Coord) obj;
        return x == o.x && y == o.y && z == o.z;
    }
    
    @Override
    public int hashCode()
    {
        return x + z << 8 + y << 16;
    }
    
    public Coord add(Coord pos)
    {
        return new Coord(x + pos.x, y + pos.y, z + pos.z);
    }
    
    public Coord add(int ai[])
    {
        return new Coord(x + ai[0], y + ai[1], z + ai[2]);
    }
    
    public Coord substract(Coord pos)
    {
        return new Coord(x - pos.x, y - pos.y, z - pos.z);
    }
    
    public Coord substract(int ai[])
    {
        return new Coord(x - ai[0], y - ai[1], z - ai[2]);
    }
    
    public Coord getAdjacentCoord(ForgeDirection fd)
    {
        return getOffsetCoord(fd, 1);
    }
    
    public Coord getOffsetCoord(ForgeDirection fd, int distance)
    {
        return new Coord(x + (fd.offsetX * distance), y + (fd.offsetY * distance), z + (fd.offsetZ * distance));
    }
    
    public Coord[] getDirectlyAdjacentCoords()
    {
        return getDirectlyAdjacentCoords(true);
    }
    
    public Coord[] getDirectlyAdjacentCoords(boolean includeBelow)
    {
        Coord[] adjacents;
        if (includeBelow)
            adjacents = new Coord[6];
        else
            adjacents = new Coord[5];
        
        adjacents[0] = getAdjacentCoord(ForgeDirection.UP);
        adjacents[1] = getAdjacentCoord(ForgeDirection.NORTH);
        adjacents[2] = getAdjacentCoord(ForgeDirection.EAST);
        adjacents[3] = getAdjacentCoord(ForgeDirection.SOUTH);
        adjacents[4] = getAdjacentCoord(ForgeDirection.WEST);
        
        if (includeBelow)
            adjacents[5] = getAdjacentCoord(ForgeDirection.DOWN);
        
        return adjacents;
    }
    
    public Coord[] getAdjacentCoords()
    {
        return getAdjacentCoords(true, true);
    }
    
    public Coord[] getAdjacentCoords(boolean includeBelow, boolean includeDiagonal)
    {
        if (!includeDiagonal)
            return getDirectlyAdjacentCoords(includeBelow);
        
        Coord[] adjacents = new Coord[(includeBelow ? 26 : 17)];
        
        int index = 0;
        
        for (int xl = -1; xl < 1; xl++)
            for (int zl = -1; zl < 1; zl++)
                for (int yl = (includeBelow ? -1 : 0); yl < 1; yl++)
                    if (xl != 0 || zl != 0 || yl != 0)
                        adjacents[index++] = new Coord(x + xl, y + yl, z + zl);
        
        return adjacents;
    }
    
    public boolean isAbove(Coord pos)
    {
        return pos != null ? y > pos.y : false;
    }
    
    public boolean isBelow(Coord pos)
    {
        return pos != null ? y < pos.y : false;
    }
    
    public boolean isNorthOf(Coord pos)
    {
        return pos != null ? z < pos.z : false;
    }
    
    public boolean isSouthOf(Coord pos)
    {
        return pos != null ? z > pos.z : false;
    }
    
    public boolean isEastOf(Coord pos)
    {
        return pos != null ? x > pos.x : false;
    }
    
    public boolean isWestOf(Coord pos)
    {
        return pos != null ? x < pos.x : false;
    }
    
    public boolean isXAligned(Coord pos)
    {
        return pos != null ? x == pos.x : false;
    }
    
    public boolean isYAligned(Coord pos)
    {
        return pos != null ? y == pos.y : false;
    }
    
    public boolean isZAligned(Coord pos)
    {
        return pos != null ? z == pos.z : false;
    }
    
    public boolean isAirBlock(World world)
    {
        return WorldHelper.isAirBlock(world, x, y, z);
    }
    
    public boolean isBlockNormalCube(World world)
    {
        return WorldHelper.isBlockNormalCube(world, x, y, z, false);
    }
    
    public boolean isBlockOpaqueCube(World world)
    {
        return WorldHelper.isBlockOpaqueCube(world, x, y, z);
    }
    
    public boolean isWood(World world)
    {
        return WorldHelper.getBlock(world, x, y, z).isWood(world, x, y, z);
    }
    
    public boolean isLeaves(World world)
    {
        return WorldHelper.getBlock(world, x, y, z).isLeaves(world, x, y, z);
    }
    
    public Block getBlock(World world)
    {
        return WorldHelper.getBlock(world, x, y, z);
    }
    
    public int getBlockMetadata(World world)
    {
        return world.getBlockMetadata(x, y, z);
    }
    
    public BiomeGenBase getBiomeGenBase(World world)
    {
        return world.getBiomeGenForCoords(x, z);
    }
    
    public static boolean moveBlock(World world, Coord src, Coord tgt, boolean allowBlockReplacement)
    {
        if (!world.isRemote && !src.isAirBlock(world) && (tgt.isAirBlock(world) || allowBlockReplacement))
        {
            Block blockID = src.getBlock(world);
            int metadata = src.getBlockMetadata(world);
            
            WorldHelper.setBlock(world, tgt.x, tgt.y, tgt.z, blockID, metadata, BlockNotifyType.ALL);
            
            TileEntity te = WorldHelper.getBlockTileEntity(world, src.x, src.y, src.z);
            if (te != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                TileEntityHelper.writeToNBT(te, nbt);
                
                nbt.setInteger("x", tgt.x);
                nbt.setInteger("y", tgt.y);
                nbt.setInteger("z", tgt.z);
                
                te = WorldHelper.getBlockTileEntity(world, tgt.x, tgt.y, tgt.z);
                if (te != null)
                    TileEntityHelper.readFromNBT(te, nbt);
            }
            
            WorldHelper.setBlockToAir(world, src.x, src.y, src.z);
            return true;
        }
        return false;
    }
    
    public boolean moveBlockToHereFrom(World world, Coord src, boolean allowBlockReplacement)
    {
        return moveBlock(world, src, this, allowBlockReplacement);
    }
    
    public boolean moveBlockFromHereTo(World world, Coord tgt, boolean allowBlockReplacement)
    {
        return moveBlock(world, this, tgt, allowBlockReplacement);
    }
    
    @Override
    public String toString()
    {
        return x + "," + y + "," + z;
    }
}
