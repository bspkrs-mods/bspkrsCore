package bspkrs.bspkrscore.fml;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        bspkrsCoreMod.instance.ticker = new BSCTicker(EnumSet.noneOf(TickType.class));
        TickRegistry.registerTickHandler(bspkrsCoreMod.instance.ticker, Side.CLIENT);
    }
}
