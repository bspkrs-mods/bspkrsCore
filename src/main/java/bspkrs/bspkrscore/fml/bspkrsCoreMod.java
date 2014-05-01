package bspkrs.bspkrscore.fml;

import static bspkrs.util.config.Configuration.CATEGORY_GENERAL;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.UniqueNameListGenerator;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;
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

@Mod(modid = "bspkrsCore", name = "bspkrsCore", version = "6.9(" + Const.MCVERSION + ")", dependencies = "before:*", useMetadata = true,
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
    
    // Example config stuff
    private final boolean[]     fixedBooleanListDefault          = new boolean[] { true, true, true, false, false, false };
    public boolean[]            fixedBooleanList                 = Arrays.copyOf(fixedBooleanListDefault, fixedBooleanListDefault.length);
    private final String[]      variablePatternStringListDefault = new String[] { "minecraft:stone_pickaxe,1,0", "minecraft:stone_shovel,1,0",
                                                                 "minecraft:stone_sword,1,0,{RepairCost:2,display:{Name:\"Rapier\",},}",
                                                                 "minecraft:stone_axe,1,0", "minecraft:apple,16,0", "minecraft:torch,16,0",
                                                                 "minecraft:enchanted_book,1,0,{StoredEnchantments:[0:{id:3s,lvl:4s,},],}",
                                                                 "minecraft:enchanted_book,1,0,{StoredEnchantments:[0:{id:51s,lvl:1s,},],}",
                                                                 "minecraft:anvil,1,0" };
    private final Pattern       variablePatternStringListPattern = Pattern.compile("[A-Za-z]+:[A-Za-z_]+,[0-9]+,[0-9]+($|,\\{.+?\\}$)");
    public String[]             variablePatternStringList        = Arrays.copyOf(variablePatternStringListDefault, variablePatternStringListDefault.length);
    private final String        regularStringDefault             = "This is a regular string, anything goes.";
    public String               regularString                    = regularStringDefault;
    private final String        patternStringDefault             = "Only, comma, separated, words, can, be, entered, in, this, box";
    public String               patternString                    = patternStringDefault;
    private final Pattern       patternStringPattern             = Pattern.compile("([A-Za-z]+((,){1}( )*|$))+?");
    private final String        selectStringDefault              = "This";
    public String               selectString                     = selectStringDefault;
    private final String[]      selectStringValues               = new String[] { "This", "property", "cycles", "through", "a", "list", "of", "valid", "choices." };
    private final int           unboundedIntegerDefault          = 25;
    public int                  unboundedInteger                 = unboundedIntegerDefault;
    private final int           boundedIntegerDefault            = 100;
    public int                  boundedInteger                   = boundedIntegerDefault;
    private final float         unboundedFloatDefault            = 25.0F;
    public float                unboundedFloat                   = unboundedFloatDefault;
    private final float         boundedFloatDefault              = 100.0F;
    public float                boundedFloat                     = boundedFloatDefault;
    private final String        chatColorPickerDefault           = "c";
    public String               chatColorPicker                  = chatColorPickerDefault;
    private final String[]      chatColorPickerValues            = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
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
        Property temp;
        String ctgyGen = BSConfiguration.CATEGORY_GENERAL;
        config.load();
        
        config.addCustomCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for bspkrsCore, then click the Config button to modify these settings.");
        
        allowUpdateCheck = config.getBoolean(ConfigElement.ALLOW_UPDATE_CHECK.key(), ctgyGen, allowUpdateCheckDefault, ConfigElement.ALLOW_UPDATE_CHECK.desc(), ConfigElement.ALLOW_UPDATE_CHECK.languageKey());
        allowDebugOutput = config.getBoolean(ConfigElement.ALLOW_DEBUG_OUTPUT.key(), ctgyGen, allowDebugOutput, ConfigElement.ALLOW_DEBUG_OUTPUT.desc(), ConfigElement.ALLOW_DEBUG_OUTPUT.languageKey());
        updateTimeoutMilliseconds = config.getInt(ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.key(), ctgyGen, updateTimeoutMillisecondsDefault, 100, 30000, ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.desc(), ConfigElement.UPDATE_TIMEOUT_MILLISECONDS.languageKey());
        generateUniqueNamesFile = config.getBoolean(ConfigElement.GENERATE_UNIQUE_NAMES_FILE.key(), ctgyGen, generateUniqueNamesFileDefault, ConfigElement.GENERATE_UNIQUE_NAMES_FILE.desc(), ConfigElement.GENERATE_UNIQUE_NAMES_FILE.languageKey());
        showMainMenuMobs = config.getBoolean(ConfigElement.SHOW_MAIN_MENU_MOBS.key(), ctgyGen, showMainMenuMobsDefault, ConfigElement.SHOW_MAIN_MENU_MOBS.desc(), ConfigElement.SHOW_MAIN_MENU_MOBS.languageKey());
        
        // example stuff
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "fixedBooleanList", fixedBooleanListDefault, "This is a Boolean list that has a fixed length of 6.", true);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        fixedBooleanList = temp.getBooleanList();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "variablePatternStringList", variablePatternStringListDefault, "This is a String List that is validated using a Pattern object. 27 entries are allowed.", false, 27, variablePatternStringListPattern);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        variablePatternStringList = temp.getStringList();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "regularString", regularStringDefault, "Just a regular String... no requirements.");
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        regularString = temp.getString();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "patternString", patternStringDefault, "This comma-separated String is validated using a Pattern object.", patternStringPattern);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        patternString = temp.getString();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "selectString", selectStringDefault, "If a String[] of valid values is given to a String property, the GUI control is a cycle button.", selectStringValues);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        selectString = temp.getString();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "unboundedInteger", unboundedIntegerDefault, "Integer prop without bounds.");
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        unboundedInteger = temp.getInt();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "boundedInteger", boundedIntegerDefault, "Integer prop with bounds.", -1, 200);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        boundedInteger = temp.getInt();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "unboundedFloat", unboundedFloatDefault, "Float prop without bounds.");
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        unboundedFloat = (float) temp.getDouble();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "boundedFloat", boundedFloatDefault, "Float prop with bounds.", -1.1F, 225.25F);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        boundedFloat = (float) temp.getDouble();
        temp = config.get(CATEGORY_GENERAL + ".example_properties", "chatColorPicker", chatColorPickerDefault, "This property selects a color code for chat formatting.", Property.Type.COLOR, chatColorPickerValues);
        temp.setLanguageKey("bspkrs.configgui.example." + temp.getName());
        chatColorPicker = temp.getString();
        
        config.addCustomCategoryComment(ctgyGen + ".example_properties", "This section contains example properties to demo the config GUI controls.");
        config.addCustomCategoryLanguageKey(ctgyGen + ".example_properties", "bspkrs.configgui.ctgy.example_properties");
        
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
