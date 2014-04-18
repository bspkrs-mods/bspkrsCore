package bspkrs.bspkrscore.fml;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class NetworkHandler
{
    @SubscribeEvent
    public void clientLoggedIn(ClientConnectedToServerEvent event)
    {
        bspkrsCoreMod.proxy.registerGameTickHandler();
        bspkrsCoreMod.proxy.unRegisterMainMenuTickHandler();
    }
    
    @SubscribeEvent
    public void clientDisconnectedFromServer(ClientDisconnectionFromServerEvent event)
    {
        bspkrsCoreMod.proxy.registerMainMenuTickHandler();
    }
}
