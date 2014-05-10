package bspkrs.bspkrscore.fml;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerGameTickHandler()
    {
        if (bspkrsCoreMod.instance.allowDebugOutput || BSCClientTicker.allowUpdateCheck)
            new BSCClientTicker();
    }
    
    @Override
    protected void registerMainMenuTickHandler()
    {
        MinecraftForge.EVENT_BUS.register(new BSMainMenuRenderTicker());
    }
}
