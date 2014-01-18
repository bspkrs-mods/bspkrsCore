package bspkrs.bspkrscore.fml;

import java.io.File;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.UniqueNameListGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "bspkrsCore", name = "bspkrsCore", version = "6.0(" + Const.MCVERSION + ")", dependencies = "before:*", useMetadata = true,
        guiFactory = "bspkrs.bspkrscore.fml.gui.ModGuiFactoryHandler")
public class bspkrsCoreMod
{
    // config stuff
    public final String         allowUpdateCheckDesc          = "Set to true to allow checking for updates for ALL of my mods, false to disable";
    public boolean              allowUpdateCheck              = true;
    public final String         allowDebugOutputDesc          = "";
    public boolean              allowDebugOutput              = false;
    public final String         updateTimeoutMillisecondsDesc = "The timeout in milliseconds for the version update check.";
    public int                  updateTimeoutMilliseconds     = 3000;
    public final String         generateUniqueNamesFileDesc   = "When true a file called UniqueNames.txt will be generated in the config folder for convenience. \n" +
                                                                      "The names found in the file are the string representation of blocks and items in Minecraft.\n" +
                                                                      "Mods such as Treecapitator and StartingInventory use them in their config files since IDs are gone.";
    public boolean              generateUniqueNamesFile       = true;
    
    @Metadata(value = "bspkrsCore")
    public static ModMetadata   metadata;
    
    @Instance(value = "bspkrsCore")
    public static bspkrsCoreMod instance;
    
    @SidedProxy(clientSide = "bspkrs.bspkrscore.fml.ClientProxy", serverSide = "bspkrs.bspkrscore.fml.CommonProxy")
    public static CommonProxy   proxy;
    
    protected ModVersionChecker versionChecker;
    private final String        versionURL                    = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    private final String        mcfTopic                      = "http://www.minecraftforum.net/topic/1114612-";
    
    public BSConfiguration      config;
    
    @SideOnly(Side.CLIENT)
    protected BSCClientTicker   ticker;
    private boolean             isCommandRegistered;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new BSConfiguration(file);
        
        syncConfig();
    }
    
    public void syncConfig()
    {
        String ctgyGen = BSConfiguration.CATEGORY_GENERAL;
        config.load();
        
        allowUpdateCheck = config.getBoolean("allowUpdateCheck", ctgyGen, allowUpdateCheck, allowUpdateCheckDesc);
        allowDebugOutput = config.getBoolean("allowDebugOutput", ctgyGen, allowDebugOutput, allowDebugOutputDesc);
        updateTimeoutMilliseconds = config.getInt("updateTimeoutMilliseconds", ctgyGen, updateTimeoutMilliseconds, 100, 30000, updateTimeoutMillisecondsDesc);
        generateUniqueNamesFile = config.getBoolean("generateUniqueNamesFile", ctgyGen, generateUniqueNamesFile, generateUniqueNamesFileDesc);
        
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
        
        if (event.getSide().isClient())
        {
            FMLCommonHandler.instance().bus().register(new NetworkHandler());
            ClientCommandHandler.instance.registerCommand(new CommandBS());
            isCommandRegistered = true;
        }
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (generateUniqueNamesFile)
            UniqueNameListGenerator.instance().run();
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (!isCommandRegistered)
            event.registerServerCommand(new CommandBS());
    }
}
