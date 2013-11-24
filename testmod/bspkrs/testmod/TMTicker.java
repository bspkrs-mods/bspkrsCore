package bspkrs.testmod;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.fml.util.TickerBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TMTicker extends TickerBase
{
    private Minecraft mcClient;
    private boolean   allowUpdateCheck;
    
    public TMTicker(EnumSet<TickType> tickTypes)
    {
        super(tickTypes);
        allowUpdateCheck = TestMod.instance.allowUpdateCheck;
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
            if (TestMod.instance.versionChecker != null)
                if (!TestMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : TestMod.instance.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
            
            allowUpdateCheck = false;
        }
        
        return keepTicking;
    }
    
    @Override
    public String getLabel()
    {
        return "TMTicker";
    }
    
}
