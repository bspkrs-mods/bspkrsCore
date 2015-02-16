package bspkrs.bspkrscore.fml;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    private BSMainMenuRenderTicker mainMenuTicker;

    @Override
    protected void registerGameTickHandler()
    {
        if (BSCClientTicker.allowUpdateCheck)
            new BSCClientTicker();
    }

    @Override
    protected void registerMainMenuTickHandler()
    {
        mainMenuTicker = new BSMainMenuRenderTicker();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (bspkrsCoreMod.instance.showMainMenuMobs)
            if ((event.gui instanceof GuiMainMenu) && !mainMenuTicker.isRegistered())
                mainMenuTicker.register();
            else if (mainMenuTicker.isRegistered())
                mainMenuTicker.unRegister();
    }
}
