package net.minecraft.src;

import net.minecraft.client.Minecraft;
import bspkrs.util.ModVersionChecker;

public class mod_bspkrsCore extends BaseMod
{
    @MLProp(info = "Set to true to allow checking for updates for any of the mods I maintain, false to disable")
    public static boolean     allowUpdateCheck = true;
    
    private ModVersionChecker versionChecker;
    private final String      versionURL       = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.6/bspkrsCore.version";
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
        return "v1.0(1.4.7)";
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
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(2, 4);
        }
        ModLoader.setInGameHook(this, true, true);
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (allowUpdateCheck)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            allowUpdateCheck = false;
        }
        
        return false;
    }
}
