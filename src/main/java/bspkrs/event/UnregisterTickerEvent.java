package bspkrs.event;

import cpw.mods.fml.common.eventhandler.Event;

public class UnregisterTickerEvent extends Event
{
    public final String modID;
    public final Object ticker;
    
    public UnregisterTickerEvent(String modID, Object ticker)
    {
        this.modID = modID;
        this.ticker = ticker;
    }
}
