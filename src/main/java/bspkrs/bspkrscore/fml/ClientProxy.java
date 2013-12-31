package bspkrs.bspkrscore.fml;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        bspkrsCoreMod.instance.ticker = new BSCTicker<ClientTickEvent>();
        FMLCommonHandler.instance().bus().register(bspkrsCoreMod.instance.ticker);
    }
}
