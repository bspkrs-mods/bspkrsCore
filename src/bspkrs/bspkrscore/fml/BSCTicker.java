package bspkrs.bspkrscore.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSCTicker implements ITickHandler
{
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    private boolean           allowUpdateCheck;
    private Minecraft         mcClient;
    
    public BSCTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    public void addTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (!this.tickTypes.contains(tt))
                this.tickTypes.add(tt);
    }
    
    public void removeTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (this.tickTypes.contains(tt))
                this.tickTypes.remove(tt);
    }
    
    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, true);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, false);
    }
    
    private void tick(EnumSet<TickType> tickTypes, boolean isStart)
    {
        for (TickType tickType : tickTypes)
        {
            if (!onTick(tickType, isStart))
            {
                tickTypes.remove(tickType);
                tickTypes.removeAll(tickType.partnerTicks());
            }
        }
    }
    
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
    public EnumSet<TickType> ticks()
    {
        return this.tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "BSCTicker";
    }
    
}
