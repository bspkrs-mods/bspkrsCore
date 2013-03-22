package bspkrs.fml.util;

import net.minecraft.src.mod_bspkrsCore;

//@TransformerExclusions({ "bspkrs.fml.util.bspkrsCoreProxy" })
public class bspkrsCoreProxy
{
    public boolean                allowUpdateCheck;
    
    public static bspkrsCoreProxy instance;
    
    public bspkrsCoreProxy()
    {
        instance = this;
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
    }
}
