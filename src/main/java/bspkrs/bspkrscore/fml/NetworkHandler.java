package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class NetworkHandler
{
    @SubscribeEvent
    public void clientLoggedIn(ClientConnectedToServerEvent event)
    {
        bspkrsCoreMod.proxy.registerGameTickHandler();
    }
}
