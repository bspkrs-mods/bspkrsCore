package bspkrs.fml.util;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;

public abstract class DelayedActionTicker extends TickerBase
{
    private int ticksRemaining;
    
    public DelayedActionTicker(int delayTicks)
    {
        ticksRemaining = delayTicks;
    }
    
    public DelayedActionTicker(EnumSet<TickType> tickTypes, int delayTicks)
    {
        super(tickTypes);
        ticksRemaining = delayTicks;
    }
    
    @Override
    public boolean onTick(TickType tickType, boolean isTickStart)
    {
        if (isTickStart)
            return true;
        else if (--ticksRemaining == 0)
            onDelayCompletion();
        
        return ticksRemaining > 0;
    }
    
    @Override
    public abstract String getLabel();
    
    /**
     * This method will be called when ticksRemaining ticks have passed.
     */
    protected abstract void onDelayCompletion();
}
