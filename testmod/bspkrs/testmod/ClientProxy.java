package bspkrs.testmod;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (!TMTicker.isRegistered())
            FMLCommonHandler.instance().bus().register(new TMTicker());
    }
}
