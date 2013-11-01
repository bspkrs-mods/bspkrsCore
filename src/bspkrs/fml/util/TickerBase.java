package bspkrs.fml.util;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public abstract class TickerBase implements ITickHandler
{
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    
    public TickerBase()
    {}
    
    public TickerBase(EnumSet<TickType> tickTypes)
    {
        this.addTicks(tickTypes);
    }
    
    public TickerBase addTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (!this.tickTypes.contains(tt))
                this.tickTypes.add(tt);
        
        return this;
    }
    
    public TickerBase removeTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (this.tickTypes.contains(tt))
                this.tickTypes.remove(tt);
        
        return this;
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
    
    public abstract boolean onTick(TickType tickType, boolean isTickStart);
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return this.tickTypes;
    }
    
    @Override
    public abstract String getLabel();
    
}
