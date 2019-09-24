package bspkrs.bspkrscore.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSCClientTicker
{
    public static boolean allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
    private Minecraft     mcClient;
    public static boolean isRegistered     = false;

    public BSCClientTicker()
    {
        if (!isRegistered)
        {
            mcClient = FMLClientHandler.instance().getClient();
            FMLCommonHandler.instance().bus().register(this);
            isRegistered = true;
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (!event.phase.equals(Phase.START))
        {
            boolean keepTicking = !(mcClient != null && mcClient.player != null && mcClient.world != null);

            if (!keepTicking && isRegistered)
            {
                if (bspkrsCoreMod.instance.allowUpdateCheck && bspkrsCoreMod.instance.versionChecker != null)
                    if (!bspkrsCoreMod.instance.versionChecker.isCurrentVersion())
                        for (String msg : bspkrsCoreMod.instance.versionChecker.getInGameMessage())
                            mcClient.player.sendMessage(new TextComponentString(msg));

                allowUpdateCheck = false;

//                if (bspkrsCoreMod.instance.allowDebugOutput && !keepTicking && mcClient.world.isRemote)
//                {
//                    //EntityPlayerHelper.addChatMessage(mcClient.thePlayer, new ChatComponentText("\2470\2470\2471\2472\2473\2474\2475\2476\2477\247e\247f"));
//                }

                FMLCommonHandler.instance().bus().unregister(this);
                isRegistered = false;
            }
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
