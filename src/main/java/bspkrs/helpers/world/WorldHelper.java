/**
 * Copyright (C) 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.world;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldHelper
{
    public static boolean isAirBlock(World world, int x, int y, int z)
    {
        return world.func_147437_c(x, y, z);
    }
    
    public static boolean isBlockNormalCube(World world, int x, int y, int z, boolean defaultVal)
    {
        return world.func_147445_c(x, y, z, defaultVal);
    }
    
    public static boolean isBlockOpaqueCube(World world, int x, int y, int z)
    {
        return getBlock(world, x, y, z).func_149662_c();
    }
    
    public static Block getBlock(World world, int x, int y, int z)
    {
        return world.func_147439_a(x, y, z);
    }
    
    public static TileEntity getBlockTileEntity(World world, int x, int y, int z)
    {
        return world.func_147438_o(x, y, z);
    }
    
    public static void removeBlockTileEntity(World world, int x, int y, int z)
    {
        world.func_147475_p(x, y, z);
    }
    
    public static boolean setBlock(World world, int x, int y, int z, Block block, int metadata, int notifyFlag)
    {
        return world.func_147465_d(x, y, z, block, metadata, notifyFlag);
    }
    
    public static boolean setBlockToAir(World world, int x, int y, int z)
    {
        return world.func_147468_f(x, y, z);
    }
}
