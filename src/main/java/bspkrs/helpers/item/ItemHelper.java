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
