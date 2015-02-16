package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerGameTickHandler()
    {
        if (BSCClientTicker.allowUpdateCheck)
            new BSCClientTicker();
    }
}
