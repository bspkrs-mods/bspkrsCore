package bspkrs.bspkrscore.fml;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (bspkrsCoreMod.instance.ticker == null)
            bspkrsCoreMod.instance.ticker = new BSCTicker();
        
        FMLCommonHandler.instance().bus().register(bspkrsCoreMod.instance.ticker);
    }
}
