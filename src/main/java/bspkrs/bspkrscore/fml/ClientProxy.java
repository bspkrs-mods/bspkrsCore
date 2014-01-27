package bspkrs.bspkrscore.fml;

public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerTickHandler()
    {
        if (bspkrsCoreMod.instance.allowDebugOutput || BSCClientTicker.allowUpdateCheck)
            new BSCClientTicker();
    }
}
