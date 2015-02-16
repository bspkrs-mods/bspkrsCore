package bspkrs.fml.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

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
    public void onTick(ClientTickEvent event)
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
