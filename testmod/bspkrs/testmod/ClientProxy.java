package bspkrs.testmod;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (!TMTicker.isRegistered())
            MinecraftForge.EVENT_BUS.register(new TMTicker());
    }
}
