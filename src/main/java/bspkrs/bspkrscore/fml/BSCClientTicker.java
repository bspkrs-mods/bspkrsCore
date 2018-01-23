package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.eventhandler.*;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class BSCClientTicker
{
    /*
     * public static boolean allowUpdateCheck;
     */
    private Minecraft mcClient;
    public static boolean isRegistered;

    public BSCClientTicker()
    {
        if(!BSCClientTicker.isRegistered)
        {
            this.mcClient = FMLClientHandler.instance().getClient();
            MinecraftForge.EVENT_BUS.register(this);
            BSCClientTicker.isRegistered = true;
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event)
    {
        if(!event.phase.equals((Object)TickEvent.Phase.START))
        {
            final boolean keepTicking = this.mcClient == null || this.mcClient.player == null || this.mcClient.world == null;
            if(!keepTicking && BSCClientTicker.isRegistered)
            {
                /*
                 * if(bspkrsCoreMod.instance.allowUpdateCheck && bspkrsCoreMod.instance.versionChecker != null && !bspkrsCoreMod.instance.versionChecker.isCurrentVersion())
                 * {
                 * for(final String msg : bspkrsCoreMod.instance.versionChecker.getInGameMessage())
                 * {
                 * this.mcClient.player.sendMessage(new TextComponentString(msg));
                 * }
                 * }
                 */
                /*
                 * BSCClientTicker.allowUpdateCheck = false;
                 */
                if(!bspkrsCoreMod.INSTANCE.allowDebugOutput || keepTicking || this.mcClient.world.isRemote)
                {}
                MinecraftForge.EVENT_BUS.unregister(this);
                BSCClientTicker.isRegistered = false;
            }
        }
    }

    public static boolean isRegistered()
    {
        return BSCClientTicker.isRegistered;
    }

    static
    {
        /*
         * BSCClientTicker.allowUpdateCheck = bspkrsCoreMod.INSTANCE.allowUpdateCheck;
         */
        BSCClientTicker.isRegistered = false;
    }
}
