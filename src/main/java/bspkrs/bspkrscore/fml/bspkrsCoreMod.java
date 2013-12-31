package bspkrs.bspkrscore.fml;

import java.io.File;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "bspkrsCore", name = "bspkrsCore", version = "v6.0(" + Const.MCVERSION + ")", dependencies = "before:*", useMetadata = true)
public class bspkrsCoreMod
{
    // config stuff
    public boolean              allowUpdateCheck          = true;
    public boolean              allowDebugOutput          = false;
    public int                  updateTimeoutMilliseconds = 3000;
    
    @Metadata(value = "bspkrsCore")
    public static ModMetadata   metadata;
    
    @Instance(value = "bspkrsCore")
    public static bspkrsCoreMod instance;
    
    @SidedProxy(clientSide = "bspkrs.bspkrscore.fml.ClientProxy", serverSide = "bspkrs.bspkrscore.fml.CommonProxy")
    public static CommonProxy   proxy;
    
    protected ModVersionChecker versionChecker;
    private final String        versionURL                = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    private final String        mcfTopic                  = "http://www.minecraftforum.net/topic/1114612-";
    
    private BSConfiguration     config;
    
    @SideOnly(Side.CLIENT)
    protected BSCTicker         ticker;
    private boolean             isCommandRegistered;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        String ctgyGen = BSConfiguration.CATEGORY_GENERAL;
        
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new BSConfiguration(file);
        
        config.load();
        
        allowUpdateCheck = config.getBoolean("allowUpdateCheck", ctgyGen, allowUpdateCheck,
                "Set to true to allow checking for updates for ALL of my mods, false to disable");
        allowDebugOutput = config.getBoolean("allowDebugOutput", ctgyGen, allowDebugOutput, "");
        updateTimeoutMilliseconds = config.getInt("updateTimeoutMilliseconds", ctgyGen, updateTimeoutMilliseconds, 100, 30000,
                "The timeout in milliseconds for the version update check.");
        
        config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
        
        //proxy.registerTickHandler();
        
        if (event.getSide().isClient())
        {
            FMLCommonHandler.instance().bus().register(new NetworkHandler());
            ClientCommandHandler.instance.registerCommand(new CommandBS());
            isCommandRegistered = true;
        }
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (!isCommandRegistered)
            event.registerServerCommand(new CommandBS());
    }
}
