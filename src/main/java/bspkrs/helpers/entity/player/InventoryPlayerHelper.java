package bspkrs.helpers.entity.player;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

public class InventoryPlayerHelper
{
    public static void clearInventory(InventoryPlayer inv, Item item, int damage)
    {
        inv.func_146027_a(item, damage);
    }
}
