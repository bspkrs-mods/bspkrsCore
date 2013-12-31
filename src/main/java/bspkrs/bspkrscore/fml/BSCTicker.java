package bspkrs.bspkrscore.fml;

import net.minecraft.client.Minecraft;
import bspkrs.fml.util.TickerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSCTicker<T extends TickEvent> extends TickerBase<T>
{
    private boolean   allowUpdateCheck;
    private Minecraft mcClient;
    
    public BSCTicker()
    {
        allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    @Override
    public boolean onTick(T event)
    {
        if (event.phase.equals(Phase.START))
        {
            return true;
        }
        
        boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);
        
        if (allowUpdateCheck && !keepTicking)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && bspkrsCoreMod.instance.versionChecker != null)
                if (!bspkrsCoreMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : bspkrsCoreMod.instance.versionChecker.getInGameMessage())
                        mcClient.thePlayer.sendChatMessage(msg);
            
            allowUpdateCheck = false;
        }
        
        if (bspkrsCoreMod.instance.allowDebugOutput && !keepTicking && mcClient.theWorld.isRemote)
        {
            mcClient.thePlayer.sendChatMessage("\2470\2470\2471\2472\2473\2474\2475\2476\2477\247e\247f");
        }
        
        return keepTicking;
    }
}
