package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NetworkHandler
{
    @SubscribeEvent
    public void clientLoggedIn(final FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        bspkrsCoreMod.proxy.registerGameTickHandler();
    }
}
