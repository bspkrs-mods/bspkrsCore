package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import bspkrs.util.BSProp;
import bspkrs.util.BSPropRegistry;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;

public class mod_bspkrsCore extends BaseMod
{
    @BSProp(info = "Set to true to allow checking for updates for ALL of my mods, false to disable")
    public static boolean     allowUpdateCheck          = true;
    @BSProp
    public static boolean     allowDebugOutput          = false;
    @BSProp(info = "The timeout in milliseconds for the version update check.")
    public static int         updateTimeoutMilliseconds = 3000;
    
    private ModVersionChecker versionChecker;
    private final String      versionURL                = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    private final String      mcfTopic                  = "http://www.minecraftforum.net/topic/1114612-";
    private boolean           doUpdateCheck;
    
    public mod_bspkrsCore()
    {
        BSPropRegistry.registerPropHandler(this.getClass());
        doUpdateCheck = allowUpdateCheck;
    }
    
    @Override
    public String getName()
    {
        return "bspkrsCore";
    }
    
    @Override
    public String getVersion()
    {
        return "v3.04(" + Const.MCVERSION + ")";
    }
    
    @Override
    public String getPriorities()
    {
        return "before:*";
    }
    
    @Override
    public void load()
    {
        if (doUpdateCheck)
        {
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(1, 4);
        }
    }
    
    @Override
    public void clientConnect(NetClientHandler nch)
    {
        ModLoader.setInGameHook(this, true, true);
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (doUpdateCheck && mc.theWorld.isRemote)
        {
            if (!versionChecker.isCurrentVersionBySubStringAsFloatNewer(1, 4))
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            doUpdateCheck = false;
        }
        
        if (allowDebugOutput && mc.theWorld.isRemote)
        {
            mc.thePlayer.addChatMessage("\2470\2470\2471\2472\2473\2474\2475\2476\2477\247e\247f");
        }
        
        return false;
    }
}
