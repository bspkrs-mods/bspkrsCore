package bspkrs.fml.util;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;

@Deprecated
public class bspkrsCoreProxy
{
    public boolean                allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
    
    public static bspkrsCoreProxy instance         = new bspkrsCoreProxy();
}
