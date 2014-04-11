package bspkrs.fml.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class DelayedGuiDisplayTicker
{
    private int       delayTicks;
    private Minecraft mcClient;
    private GuiScreen screen;
    
    public DelayedGuiDisplayTicker(int delayTicks, GuiScreen screen)
    {
        this.delayTicks = delayTicks;
        this.mcClient = FMLClientHandler.instance().getClient();
        this.screen = screen;
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onTick(ServerTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        if (--delayTicks <= 0)
        {
            mcClient.displayGuiScreen(screen);
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
}
