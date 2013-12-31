package bspkrs.fml.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public abstract class TickerBase<T extends TickEvent>
{
    @SubscribeEvent
    public void tickEventListener(T event)
    {
        if (!onTick(event))
        {
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }
    
    public abstract boolean onTick(T event);
}
