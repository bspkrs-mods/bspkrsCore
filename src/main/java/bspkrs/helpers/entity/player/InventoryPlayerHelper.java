/**
 * Copyright (C) 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.entity.player;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

public class InventoryPlayerHelper
{
    public static void clearInventory(InventoryPlayer inv, Item item, int damage)
    {
        inv.clearInventory(item, damage);
    }
}
