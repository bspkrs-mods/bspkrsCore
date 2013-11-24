package bspkrs.testmod;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        TestMod.instance.ticker = new TMTicker(EnumSet.noneOf(TickType.class));
        TickRegistry.registerTickHandler(TestMod.instance.ticker, Side.CLIENT);
    }
}
