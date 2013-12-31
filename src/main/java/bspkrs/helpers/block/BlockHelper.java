package bspkrs.helpers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockHelper
{
    public static Material getBlockMaterial(Block block)
    {
        return block.func_149688_o();
    }
}
