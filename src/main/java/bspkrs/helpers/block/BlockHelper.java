/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;

public class BlockHelper
{
    public static Material getBlockMaterial(Block block)
    {
        return block.func_149688_o();
    }
    
    public static String getUniqueID(Block block)
    {
        return GameData.blockRegistry.func_148750_c(block);
    }
    
    public static Block getBlock(String uniqueID)
    {
        return GameData.blockRegistry.getObject(uniqueID);
    }
    
    public static int damageDropped(Block block, int metadata)
    {
        return block.func_149692_a(metadata);
    }
    
    public static void dropBlockAsItem(Block block, World world, int x, int y, int z, int metadata, int fortuneLevel)
    {
        block.func_149697_b(world, x, y, z, metadata, fortuneLevel);
    }
}
