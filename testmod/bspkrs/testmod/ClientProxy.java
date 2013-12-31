package bspkrs.testmod;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (TestMod.instance.ticker == null)
            TestMod.instance.ticker = new TMTicker();
        
        FMLCommonHandler.instance().bus().register(TestMod.instance.ticker);
    }
}
