package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    protected void registerGameTickHandler()
    {
        /*
         * if(BSCClientTicker.allowUpdateCheck)
         * {
         * new BSCClientTicker();
         * }
         */
    }
}
