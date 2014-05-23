package bspkrs.testmod;

import static bspkrs.util.config.Configuration.CATEGORY_GENERAL;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;

public class TestModSimpleConfig
{
    private final static boolean   enabledDefault                   = true;
    public static boolean          enabled                          = enabledDefault;
    private final static boolean[] fixedBooleanListDefault          = new boolean[] { true, true, true, false, false, false };
    public static boolean[]        fixedBooleanList                 = Arrays.copyOf(fixedBooleanListDefault, fixedBooleanListDefault.length);
    private final static String[]  variablePatternStringListDefault = new String[] { "minecraft:stone_pickaxe,1,0", "minecraft:stone_shovel,1,0",
                                                                    "minecraft:stone_sword,1,0,{RepairCost:2,display:{Name:\"Rapier\",},}",
                                                                    "minecraft:stone_axe,1,0", "minecraft:apple,16,0", "minecraft:torch,16,0",
                                                                    "minecraft:enchanted_book,1,0,{StoredEnchantments:[0:{id:3s,lvl:4s,},],}",
                                                                    "minecraft:enchanted_book,1,0,{StoredEnchantments:[0:{id:51s,lvl:1s,},],}",
                                                                    "minecraft:anvil,1,0" };
    private final static Pattern   variablePatternStringListPattern = Pattern.compile("[A-Za-z]+:[A-Za-z_]+,[0-9]+,[0-9]+($|,\\{.+?\\}$)");
    public static String[]         variablePatternStringList        = Arrays.copyOf(variablePatternStringListDefault, variablePatternStringListDefault.length);
    private final static String    regularStringDefault             = "This is a regular string, anything goes.";
    public static String           regularString                    = regularStringDefault;
    private final static String    patternStringDefault             = "Only, comma, separated, words, can, be, entered, in, this, box";
    public static String           patternString                    = patternStringDefault;
    private final static Pattern   patternStringPattern             = Pattern.compile("([A-Za-z]+((,){1}( )*|$))+?");
    private final static String    selectStringDefault              = "This";
    public static String           selectString                     = selectStringDefault;
    private final static String[]  selectStringValues               = new String[] { "This", "property", "cycles", "through", "a", "list", "of", "valid", "choices." };
    private final static int       unboundedIntegerDefault          = 25;
    public static int              unboundedInteger                 = unboundedIntegerDefault;
    private final static int       boundedIntegerDefault            = 100;
    public static int              boundedInteger                   = boundedIntegerDefault;
    private final static float     unboundedFloatDefault            = 25.0F;
    public static float            unboundedFloat                   = unboundedFloatDefault;
    private final static float     boundedFloatDefault              = 100.0F;
    public static float            boundedFloat                     = boundedFloatDefault;
    private final static String    chatColorPickerDefault           = "c";
    public static String           chatColorPicker                  = chatColorPickerDefault;
    private final static String[]  chatColorPickerValues            = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    
    private static Configuration   config;
    
    public static Configuration getConfig()
    {
        return config;
    }
    
    public static void initConfig(File file)
    {
        config = new Configuration(file);
        syncConfig();
    }
    
    /**
     * syncConfig() is used to load your config initially, or reload the fields after editing the values in the GUI. It must sync all the
     * metadata used by the GUI controls such as language keys, valid values, or min/max bounds.
     */
    public static void syncConfig()
    {
        Property temp;
        
        config.load();
        
        temp = config.get(CATEGORY_GENERAL, "fixedBooleanList", fixedBooleanListDefault, "This is a Boolean list that has a fixed length of 6.", true, -1, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        fixedBooleanList = temp.getBooleanList();
        temp = config.get(CATEGORY_GENERAL, "variablePatternStringList", variablePatternStringListDefault, "This is a String List that is validated using a Pattern object. 27 entries are allowed.", false, 27, variablePatternStringListPattern, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        variablePatternStringList = temp.getStringList();
        temp = config.get(CATEGORY_GENERAL, "regularString", regularStringDefault, "Just a regular String... no requirements.");
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        regularString = temp.getString();
        temp = config.get(CATEGORY_GENERAL, "patternString", patternStringDefault, "This comma-separated String is validated using a Pattern object.", patternStringPattern, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        patternString = temp.getString();
        temp = config.get(CATEGORY_GENERAL, "selectString", selectStringDefault, "If a String[] of valid values is given to a String property, the GUI control is a cycle button.", selectStringValues, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        selectString = temp.getString();
        temp = config.get(CATEGORY_GENERAL, "unboundedInteger", unboundedIntegerDefault, "Integer prop without bounds.", Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        unboundedInteger = temp.getInt();
        temp = config.get(CATEGORY_GENERAL, "boundedInteger", boundedIntegerDefault, "Integer prop with bounds.", -1, 200, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        boundedInteger = temp.getInt();
        temp = config.get(CATEGORY_GENERAL, "unboundedFloat", unboundedFloatDefault, "Float prop without bounds.", Float.MIN_VALUE, Float.MAX_VALUE, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        unboundedFloat = (float) temp.getDouble();
        temp = config.get(CATEGORY_GENERAL, "boundedFloat", boundedFloatDefault, "Float prop with bounds.", -1.1F, 225.25F, true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        boundedFloat = (float) temp.getDouble();
        temp = config.get(CATEGORY_GENERAL, "chatColorPicker", chatColorPickerDefault, "This property selects a color code for chat formatting.", Property.Type.COLOR);
        temp.setValidValues(chatColorPickerValues);
        temp.setIsHotLoadable(true);
        temp.setPropLanguageKey("bspkrs.configgui.example." + temp.getName());
        chatColorPicker = temp.getString();
        
        config.save();
    }
}
