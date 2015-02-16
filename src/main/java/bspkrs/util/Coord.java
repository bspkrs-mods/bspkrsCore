package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class Coord extends BlockPos
{
    public int x;
    public int y;
    public int z;

    public Coord(int x, int y, int z)
    {
        super(0, 0, 0);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coord(Vec3i v)
    {
        this(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public int getZ()
    {
        return z;
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

        if (!(obj instanceof Vec3i))
            return false;

        Vec3i o = (Vec3i) obj;
        return (x == o.getX()) && (y == o.getY()) && (z == o.getZ());
    }

    @Override
    public int hashCode()
    {
        return (x + z) << (8 + y) << 16;
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

    public Coord getAdjacentCoord(EnumFacing fd)
    {
        return (Coord) offset(fd, 1);
    }

    public Coord getOffsetCoord(EnumFacing fd, int distance)
    {
        return (Coord) offset(fd, distance);
    }

    public Coord[] getDirectlyAdjacentCoords()
    {
        return getDirectlyAdjacentCoords(true);
    }

    public Coord[] getDirectlyAdjacentCoords(boolean includeBelow)
    {
        Coord[] adjacents;
        if (includeBelow)
        {
            adjacents = new Coord[6];
            adjacents[5] = getAdjacentCoord(EnumFacing.DOWN);
        }
        else
            adjacents = new Coord[5];

        adjacents[0] = getAdjacentCoord(EnumFacing.UP);
        adjacents[1] = getAdjacentCoord(EnumFacing.NORTH);
        adjacents[2] = getAdjacentCoord(EnumFacing.EAST);
        adjacents[3] = getAdjacentCoord(EnumFacing.SOUTH);
        adjacents[4] = getAdjacentCoord(EnumFacing.WEST);

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
                    if ((xl != 0) || (zl != 0) || (yl != 0))
                        adjacents[index++] = new Coord(x + xl, y + yl, z + zl);

        return adjacents;
    }

    public int get3DDistance(Coord pos)
    {
        return (int) Math.round(Math.sqrt(Math.pow(pos.x - x, 2) + Math.pow(pos.z - z, 2) + Math.pow(pos.y - y, 2)));
    }

    public int getCubicDistance(Coord pos)
    {
        return Math.abs(pos.x - x) + Math.abs(pos.y - y) + Math.abs(pos.z - z);
    }

    public int getSquaredDistance(Coord pos)
    {
        return Math.abs(pos.x - x) + Math.abs(pos.z - z);
    }

    public int getVerDistance(Coord pos)
    {
        return Math.abs(pos.y - y);
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
        return world.isAirBlock(this);
    }

    public boolean chunkExists(World world)
    {
        return world.getChunkProvider().chunkExists(x, z);
    }

    public boolean isBlockNormalCube(World world)
    {
        return world.isBlockNormalCube(this, false);
    }

    public boolean isBlockOpaqueCube(World world)
    {
        return getBlock(world).isOpaqueCube();
    }

    public boolean isWood(World world)
    {
        return getBlock(world).isWood(world, this);
    }

    public boolean isLeaves(World world)
    {
        return getBlock(world).isLeaves(world, this);
    }

    public Block getBlock(World world)
    {
        return getBlockState(world).getBlock();
    }

    public IBlockState getBlockState(World world)
    {
        return world.getBlockState(this);
    }

    public int getBlockMetadata(World world)
    {
        IBlockState state = getBlockState(world);
        return state.getBlock().getMetaFromState(state);
    }

    public BiomeGenBase getBiomeGenBase(World world)
    {
        return world.getBiomeGenForCoords(this);
    }

    public static boolean moveBlock(World world, Coord src, Coord tgt, boolean allowBlockReplacement)
    {
        return moveBlock(world, src, tgt, allowBlockReplacement, BlockNotifyType.ALL);
    }

    public static boolean moveBlock(World world, Coord src, Coord tgt, boolean allowBlockReplacement, int notifyFlag)
    {
        if (!world.isRemote && !src.isAirBlock(world) && (tgt.isAirBlock(world) || allowBlockReplacement))
        {
            Block block = src.getBlock(world);
            int metadata = src.getBlockMetadata(world);
            IBlockState state = block.getStateFromMeta(metadata);

            world.setBlockState(tgt, state, notifyFlag);

            TileEntity te = world.getTileEntity(src);
            if (te != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                te.writeToNBT(nbt);

                nbt.setInteger("x", tgt.x);
                nbt.setInteger("y", tgt.y);
                nbt.setInteger("z", tgt.z);

                te = world.getTileEntity(tgt);
                if (te != null)
                    te.readFromNBT(nbt);

                world.removeTileEntity(src);
            }

            world.setBlockToAir(src);
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
