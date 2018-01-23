package bspkrs.bspkrscore.fml;

import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.config.*;
import java.io.*;
import java.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.client.*;
import net.minecraft.command.*;
import bspkrs.util.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, useMetadata = true, guiFactory = "bspkrs.bspkrscore.fml.gui.ModGuiFactoryHandler")
public class bspkrsCoreMod
{
    /*
     * @SuppressWarnings("unused")
     * private final boolean allowUpdateCheckDefault = true;
     * public boolean allowUpdateCheck;
     */
    @SuppressWarnings("unused")
    private final boolean allowDebugOutputDefault = false;
    public boolean allowDebugOutput;
    @SuppressWarnings("unused")
    private final int updateTimeoutMillisecondsDefault = 3000;
    public int updateTimeoutMilliseconds;
    @SuppressWarnings("unused")
    private final boolean generateUniqueNamesFileDefault = true;
    public boolean generateUniqueNamesFile;
    @Mod.Metadata(Reference.MODID)
    public static ModMetadata METADATA;
    @Mod.Instance(Reference.MODID)
    public static bspkrsCoreMod INSTANCE;
    @SidedProxy(clientSide = "bspkrs.bspkrscore.fml.ClientProxy", serverSide = "bspkrs.bspkrscore.fml.CommonProxy")
    public static CommonProxy proxy;
    // protected ModVersionChecker versionChecker;
    // protected final String versionURL;
    protected final String mcfTopic = "http://www.minecraftforum.net/topic/1114612-";
    @SideOnly(Side.CLIENT)
    protected BSCClientTicker ticker;
    private boolean isCommandRegistered;

    public bspkrsCoreMod()
    {
        // this.allowUpdateCheck = false;
        this.allowDebugOutput = false;
        this.updateTimeoutMilliseconds = 3000;
        this.generateUniqueNamesFile = true;
        // this.versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/bspkrsCore.version";
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        bspkrsCoreMod.METADATA = event.getModMetadata();
        final File file = event.getSuggestedConfigurationFile();
        if(!CommonUtils.isObfuscatedEnv())
        {}
        Reference.config = new Configuration(file);
        this.syncConfig();
    }

    public void syncConfig()
    {
        final String ctgyGen = "general";
        Reference.config.load();
        Reference.config.setCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \nOn the Mods list screen select the entry for bspkrsCore, then click the Config button to modify these settings.");
        final List<String> orderedKeys = new ArrayList<String>(ConfigElement.values().length);
        /*
         * this.allowUpdateCheck = Reference.config.getBoolean(ConfigElement.ALLOW_UPDATE_CHECK.key(), ctgyGen, true, ConfigElement.ALLOW_UPDATE_CHECK.desc(),
         * ConfigElement.ALLOW_UPDATE_CHECK.languageKey());
         * orderedKeys.add(ConfigElement.ALLOW_UPDATE_CHECK.key());
         */
        this.updateTimeoutMilliseconds = Reference.config.getInt(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key(), ctgyGen, 3000, 100, 30000, ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.desc(), ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.languageKey());
        orderedKeys.add(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key());
        this.allowDebugOutput = Reference.config.getBoolean(ConfigElement.ALLOW_DEBUG_OUTPUT.key(), ctgyGen, this.allowDebugOutput, ConfigElement.ALLOW_DEBUG_OUTPUT.desc(), ConfigElement.ALLOW_DEBUG_OUTPUT.languageKey());
        orderedKeys.add(ConfigElement.ALLOW_DEBUG_OUTPUT.key());
        this.generateUniqueNamesFile = Reference.config.getBoolean(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key(), ctgyGen, true, ConfigElement.GENERATE_UNIQUE_NAMES_FILE.desc(), ConfigElement.GENERATE_UNIQUE_NAMES_FILE.languageKey());
        orderedKeys.add(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key());
        Reference.config.setCategoryPropertyOrder(ctgyGen, (List<String>)orderedKeys);
        if(Reference.config.hasCategory(ctgyGen + ".example_properties"))
        {
            Reference.config.removeCategory(Reference.config.getCategory(ctgyGen + ".example_properties"));
        }
        Reference.config.save();
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event)
    {
        /*
         * if(this.allowUpdateCheck)
         * {
         * (this.versionChecker = new ModVersionChecker(Reference.MODID, bspkrsCoreMod.metadata.version, this.versionURL, "http://www.minecraftforum.net/topic/1114612-")).checkVersionWithLogging();
         * }
         */
        if(event.getSide().isClient())
        {
            MinecraftForge.EVENT_BUS.register(new NetworkHandler());
            try
            {
                ClientCommandHandler.instance.registerCommand((ICommand)new CommandBS());
                this.isCommandRegistered = true;
            }
            catch(Throwable e)
            {
                this.isCommandRegistered = false;
            }
        }
        MinecraftForge.EVENT_BUS.register(bspkrsCoreMod.INSTANCE);
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        if(this.generateUniqueNamesFile)
        {
            UniqueNameListGenerator.instance().run();
        }
    }

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event)
    {
        if(!this.isCommandRegistered)
        {
            event.registerServerCommand((ICommand)new CommandBS());
        }
    }

    @SubscribeEvent
    public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if(event.getModID().equals(Reference.MODID))
        {
            Reference.config.save();
            this.syncConfig();
        }
    }
}
