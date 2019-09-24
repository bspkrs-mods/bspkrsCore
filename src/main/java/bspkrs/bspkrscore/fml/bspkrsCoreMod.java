package bspkrs.bspkrscore.fml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.UniqueNameListGenerator;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", useMetadata = true, guiFactory = Reference.GUI_FACTORY)
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

    @Metadata(value = Reference.MODID)
    public static ModMetadata   metadata;

    @Instance(value = Reference.MODID)
    public static bspkrsCoreMod instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy   proxy;

    protected ModVersionChecker versionChecker;
    protected final String      versionURL                       = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    protected final String      mcfTopic                         = "http://www.minecraftforum.net/topic/1114612-";

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

        Reference.config = new Configuration(file);

        syncConfig();
    }

    public void syncConfig()
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        Reference.config.load();

        Reference.config.setCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for bspkrsCore, then click the Config button to modify these settings.");

        List<String> orderedKeys = new ArrayList<String>(ConfigElement.values().length);

        allowUpdateCheck = Reference.config.getBoolean(ConfigElement.ALLOW_UPDATE_CHECK.key(), ctgyGen, allowUpdateCheckDefault,
                ConfigElement.ALLOW_UPDATE_CHECK.desc(), ConfigElement.ALLOW_UPDATE_CHECK.languageKey());
        orderedKeys.add(ConfigElement.ALLOW_UPDATE_CHECK.key());
        updateTimeoutMilliseconds = Reference.config.getInt(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key(), ctgyGen, updateTimeoutMillisecondsDefault,
                100, 30000, ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.desc(), ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.languageKey());
        orderedKeys.add(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key());
        allowDebugOutput = Reference.config.getBoolean(ConfigElement.ALLOW_DEBUG_OUTPUT.key(), ctgyGen, allowDebugOutput,
                ConfigElement.ALLOW_DEBUG_OUTPUT.desc(), ConfigElement.ALLOW_DEBUG_OUTPUT.languageKey());
        orderedKeys.add(ConfigElement.ALLOW_DEBUG_OUTPUT.key());
        generateUniqueNamesFile = Reference.config.getBoolean(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key(), ctgyGen, generateUniqueNamesFileDefault,
                ConfigElement.GENERATE_UNIQUE_NAMES_FILE.desc(), ConfigElement.GENERATE_UNIQUE_NAMES_FILE.languageKey());
        orderedKeys.add(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key());

        Reference.config.setCategoryPropertyOrder(ctgyGen, orderedKeys);

        if (Reference.config.hasCategory(ctgyGen + ".example_properties"))
            Reference.config.removeCategory(Reference.config.getCategory(ctgyGen + ".example_properties"));

        Reference.config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(Reference.MODID, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }

        if (event.getSide().isClient())
        {
            FMLCommonHandler.instance().bus().register(new NetworkHandler());
            try
            {
                ClientCommandHandler.instance.registerCommand(new CommandBS());
                isCommandRegistered = true;
            }
            catch (Throwable e)
            {
                isCommandRegistered = false;
            }
        }

        FMLCommonHandler.instance().bus().register(instance);
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

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Reference.MODID))
        {
            Reference.config.save();
            syncConfig();
        }
    }
}
