package bspkrs.helpers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
}
