package bspkrs.bspkrscore.fml;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    private BSMainMenuRenderTicker renderTicker;
    
    @Override
    protected void registerGameTickHandler()
    {
        if (bspkrsCoreMod.instance.allowDebugOutput || BSCClientTicker.allowUpdateCheck)
            new BSCClientTicker();
    }
    
    @Override
    protected void registerMainMenuTickHandler()
    {
        if (renderTicker == null)
            renderTicker = new BSMainMenuRenderTicker();
        
        renderTicker.register();
    }
    
    @Override
    protected void unRegisterMainMenuTickHandler()
    {
        if (renderTicker != null)
            renderTicker.unRegister();
    }
}
