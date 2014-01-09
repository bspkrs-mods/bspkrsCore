package bspkrs.bspkrscore.fml;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (!BSCClientTicker.isRegistered())
        {
            bspkrsCoreMod.instance.ticker = new BSCClientTicker();
            FMLCommonHandler.instance().bus().register(bspkrsCoreMod.instance.ticker);
        }
    }
}
