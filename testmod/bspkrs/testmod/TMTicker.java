package bspkrs.testmod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.helpers.client.entity.EntityPlayerSPHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TMTicker
{
    private Minecraft mcClient;
    
    public TMTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);
        
        if (!event.phase.equals(Phase.START))
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && !keepTicking)
                if (TestMod.instance.versionChecker != null)
                    if (!TestMod.instance.versionChecker.isCurrentVersion())
                        for (String msg : TestMod.instance.versionChecker.getInGameMessage())
                            EntityPlayerSPHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText(msg));
            
            if (!keepTicking)
            {
                FMLCommonHandler.instance().bus().unregister(this);
                TestMod.instance.ticker = null;
            }
        }
    }
}
