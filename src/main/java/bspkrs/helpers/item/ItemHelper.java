/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.item;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameData;

public class ItemHelper
{
    public static String getUniqueID(Item item)
    {
        return GameData.itemRegistry.func_148750_c(item);
    }
    
    public static Item getItem(String id)
    {
        return (Item) Item.field_150901_e.getObject(id);
    }
}
