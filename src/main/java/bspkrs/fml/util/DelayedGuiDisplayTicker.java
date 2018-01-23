package bspkrs.fml.util;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class DelayedGuiDisplayTicker
{
    private int delayTicks;
    private Minecraft mcClient;
    private GuiScreen screen;

    public DelayedGuiDisplayTicker(final int delayTicks, final GuiScreen screen)
    {
        this.delayTicks = delayTicks;
        this.mcClient = FMLClientHandler.instance().getClient();
        this.screen = screen;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event)
    {
        if(event.phase.equals((Object)TickEvent.Phase.START))
        {
            return;
        }
        if(--this.delayTicks <= 0)
        {
            this.mcClient.displayGuiScreen(this.screen);
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
    }
}
