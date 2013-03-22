package net.minecraft.src;

import net.minecraft.client.Minecraft;
import bspkrs.util.ModVersionChecker;

public class mod_bspkrsCore extends BaseMod
{
    @MLProp(info = "Set to true to allow checking for updates for ALL of my mods, false to disable")
    public static boolean     allowUpdateCheck = true;
    
    private ModVersionChecker versionChecker;
    private final String      versionURL       = "http://bspk.rs/Minecraft/1.5.1/bspkrsCore.version";
    private final String      mcfTopic         = "http://www.minecraftforum.net/topic/1114612-";
    
    public mod_bspkrsCore()
    {   
        
    }
    
    @Override
    public String getName()
    {
        return "bspkrsCore";
    }
    
    @Override
    public String getVersion()
    {
        return "v1.03(1.5.1)";
    }
    
    @Override
    public String getPriorities()
    {
        return "before:*";
    }
    
    @Override
    public void load()
    {
        if (allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, ModLoader.getLogger());
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(1, 4);
            ModLoader.setInGameHook(this, true, true);
        }
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (allowUpdateCheck && mc.theWorld.isRemote)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            allowUpdateCheck = false;
        }
        
        return false;
    }
}
