package bspkrs.bspkrscore.fml;

import java.io.File;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.UniqueNameListGenerator;
import bspkrs.util.config.Configuration;
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

@Mod(modid = "bspkrsCore", name = "bspkrsCore", version = "6.4(" + Const.MCVERSION + ")", dependencies = "before:*", useMetadata = true,
        guiFactory = "bspkrs.bspkrscore.fml.gui.ModGuiFactoryHandler")
public class bspkrsCoreMod
{
    // config stuff
    private final boolean       allowUpdateCheckDefault          = true;
    public boolean              allowUpdateCheck                 = allowUpdateCheckDefault;
    private final boolean       allowDebugOutputDefault          = false;
    public boolean              allowDebugOutput                 = allowDebugOutputDefault;
    private final int           updateTimeoutMillisecondsDefault = 3000;
    public int                  updateTimeoutMilliseconds        = updateTimeoutMillisecondsDefault;
    private final boolean       generateUniqueNamesFileDefault   = true;
    public boolean              generateUniqueNamesFile          = generateUniqueNamesFileDefault;
    private final boolean       showMainMenuMobsDefault          = true;
    public boolean              showMainMenuMobs                 = showMainMenuMobsDefault;
    
    @Metadata(value = "bspkrsCore")
    public static ModMetadata   metadata;
    
    @Instance(value = "bspkrsCore")
    public static bspkrsCoreMod instance;
    
    @SidedProxy(clientSide = "bspkrs.bspkrscore.fml.ClientProxy", serverSide = "bspkrs.bspkrscore.fml.CommonProxy")
    public static CommonProxy   proxy;
    
    protected ModVersionChecker versionChecker;
    private final String        versionURL                       = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    private final String        mcfTopic                         = "http://www.minecraftforum.net/topic/1114612-";
    
    private Configuration       config;
    
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
        
        config = new Configuration(file);
        
        syncConfig();
    }
    
    public void syncConfig()
    {
        String ctgyGen = BSConfiguration.CATEGORY_GENERAL;
        config.load();
        
        config.addCustomCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for bspkrsCore, then click the Config button to modify these settings.");
        
        allowUpdateCheck = config.getBoolean(ConfigElement.ALLOW_UPDATE_CHECK.key(), ctgyGen, allowUpdateCheckDefault, ConfigElement.ALLOW_UPDATE_CHECK.desc(), ConfigElement.ALLOW_UPDATE_CHECK.languageKey());
        allowDebugOutput = config.getBoolean(ConfigElement.ALLOW_DEBUG_OUTPUT.key(), ctgyGen, allowDebugOutput, ConfigElement.ALLOW_DEBUG_OUTPUT.desc(), ConfigElement.ALLOW_DEBUG_OUTPUT.languageKey());
        updateTimeoutMilliseconds = config.getInt(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key(), ctgyGen, updateTimeoutMillisecondsDefault, 100, 30000, ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.desc(), ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.languageKey());
        generateUniqueNamesFile = config.getBoolean(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key(), ctgyGen, generateUniqueNamesFileDefault, ConfigElement.GENERATE_UNIQUE_NAMES_FILE.desc(), ConfigElement.GENERATE_UNIQUE_NAMES_FILE.languageKey());
        showMainMenuMobs = config.getBoolean(ConfigElement.SHOW_MAIN_MENU_MOBS.key(), ctgyGen, showMainMenuMobsDefault, ConfigElement.SHOW_MAIN_MENU_MOBS.desc(), ConfigElement.SHOW_MAIN_MENU_MOBS.languageKey());
        
        config.save();
    }
    
    public Configuration getConfig()
    {
        return config;
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
        
        proxy.registerMainMenuTickHandler();
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (!isCommandRegistered)
            event.registerServerCommand(new CommandBS());
    }
}
