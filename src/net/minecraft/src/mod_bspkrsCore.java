package net.minecraft.src;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;

@Deprecated
public class mod_bspkrsCore
{
    public static boolean allowUpdateCheck          = bspkrsCoreMod.instance.allowUpdateCheck;
    public static boolean allowDebugOutput          = bspkrsCoreMod.instance.allowDebugOutput;
    public static int     updateTimeoutMilliseconds = bspkrsCoreMod.instance.updateTimeoutMilliseconds;
    
    public String getName()
    {
        return bspkrsCoreMod.metadata.name;
    }
    
    public String getVersion()
    {
        return bspkrsCoreMod.metadata.version;
    }
}
