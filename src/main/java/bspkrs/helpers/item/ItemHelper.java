/**
 * Copyright (C) 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.item;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;

public class ItemHelper
{
    public static String getUniqueID(Item item)
    {
        return GameData.getItemRegistry().getNameForObject(item);
    }
    
    public static Item getItem(String uniqueID)
    {
        return GameData.getItemRegistry().getObject(uniqueID);
    }
    
    public static boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase elb)
    {
        return itemStack.getItem().onBlockDestroyed(itemStack, world, block, x, y, z, elb);
    }
}
