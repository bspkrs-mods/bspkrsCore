package bspkrs.testmod;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TMTicker
{
    private Minecraft      mcClient;
    private static boolean isRegistered = false;
    
    public TMTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
        isRegistered = true;
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        boolean keepTicking = !(mcClient != null && mcClient.player != null && mcClient.world != null);
        
        if (!event.phase.equals(Phase.START))
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && !keepTicking)
                if (TestMod.instance.versionChecker != null)
                    if (!TestMod.instance.versionChecker.isCurrentVersion())
                        for (String msg : TestMod.instance.versionChecker.getInGameMessage())
                            mcClient.player.sendMessage(new TextComponentString(msg));
            
            if (!keepTicking)
            {
                FMLCommonHandler.instance().bus().unregister(this);
                isRegistered = false;
            }
        }
    }
    
    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
