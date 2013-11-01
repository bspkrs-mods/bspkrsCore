package bspkrs.bspkrscore.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.fml.util.TickerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSCTicker extends TickerBase
{
    private boolean   allowUpdateCheck;
    private Minecraft mcClient;
    
    public BSCTicker(EnumSet<TickType> tickTypes)
    {
        super(tickTypes);
        allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    @Override
    public boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);
        
        if (allowUpdateCheck && !keepTicking)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && bspkrsCoreMod.instance.versionChecker != null)
                if (!bspkrsCoreMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : bspkrsCoreMod.instance.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            
            allowUpdateCheck = false;
        }
        
        if (bspkrsCoreMod.instance.allowDebugOutput && !keepTicking && mcClient.theWorld.isRemote)
        {
            mcClient.thePlayer.addChatMessage("\2470\2470\2471\2472\2473\2474\2475\2476\2477\247e\247f");
        }
        
        return keepTicking;
    }
    
    @Override
    public String getLabel()
    {
        return "BSCTicker";
    }
    
}
