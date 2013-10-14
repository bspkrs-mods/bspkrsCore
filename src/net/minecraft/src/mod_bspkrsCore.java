package net.minecraft.src;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;

@Deprecated
public class mod_bspkrsCore extends BaseMod
{
    public static boolean allowUpdateCheck          = bspkrsCoreMod.instance.allowUpdateCheck;
    public static boolean allowDebugOutput          = bspkrsCoreMod.instance.allowDebugOutput;
    public static int     updateTimeoutMilliseconds = bspkrsCoreMod.instance.updateTimeoutMilliseconds;
    
    @Override
    public String getName()
    {
        return bspkrsCoreMod.metadata.name;
    }
    
    @Override
    public String getVersion()
    {
        return bspkrsCoreMod.metadata.version;
    }
    
    @Override
    public void load()
    {}
}
