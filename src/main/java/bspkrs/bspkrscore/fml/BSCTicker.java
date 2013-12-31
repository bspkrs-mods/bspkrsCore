package bspkrs.bspkrscore.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.helpers.client.entity.EntityPlayerSPHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSCTicker
{
    private boolean   allowUpdateCheck;
    private Minecraft mcClient;
    
    public BSCTicker()
    {
        allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (!event.phase.equals(Phase.START))
        {
            boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);
            
            if (allowUpdateCheck && !keepTicking)
            {
                if (bspkrsCoreMod.instance.allowUpdateCheck && bspkrsCoreMod.instance.versionChecker != null)
                    if (!bspkrsCoreMod.instance.versionChecker.isCurrentVersion())
                        for (String msg : bspkrsCoreMod.instance.versionChecker.getInGameMessage())
                            EntityPlayerSPHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText(msg));
                
                allowUpdateCheck = false;
            }
            
            if (bspkrsCoreMod.instance.allowDebugOutput && !keepTicking && mcClient.theWorld.isRemote)
            {
                EntityPlayerSPHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText("\2470\2470\2471\2472\2473\2474\2475\2476\2477\247e\247f"));
            }
            
            if (!keepTicking)
                FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
