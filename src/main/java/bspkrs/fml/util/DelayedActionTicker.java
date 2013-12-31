package bspkrs.fml.util;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public abstract class DelayedActionTicker<T extends TickEvent> extends TickerBase<T>
{
    private int delayTicks;
    
    public DelayedActionTicker(int delayTicks)
    {
        this.delayTicks = delayTicks;
    }
    
    @Override
    public boolean onTick(T event)
    {
        if (event.phase.equals(Phase.START))
            return true;
        else if (--delayTicks == 0)
            onDelayCompletion();
        
        return delayTicks > 0;
    }
    
    /**
     * This method will be called when delayTicks ticks have elapsed.
     */
    protected abstract void onDelayCompletion();
}
