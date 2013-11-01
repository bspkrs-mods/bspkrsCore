package bspkrs.fml.util;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;

public abstract class DelayedActionTicker extends TickerBase
{
    private int delayTicks;
    
    public DelayedActionTicker(int delayTicks)
    {
        this.delayTicks = delayTicks;
    }
    
    public DelayedActionTicker(EnumSet<TickType> tickTypes, int delayTicks)
    {
        super(tickTypes);
        this.delayTicks = delayTicks;
    }
    
    @Override
    public boolean onTick(TickType tickType, boolean isTickStart)
    {
        if (isTickStart)
            return true;
        else if (--delayTicks == 0)
            onDelayCompletion();
        
        return delayTicks > 0;
    }
    
    @Override
    public abstract String getLabel();
    
    /**
     * This method will be called when delayTicks ticks have elapsed.
     */
    protected abstract void onDelayCompletion();
}
