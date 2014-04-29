package bspkrs.testmod;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "TestMod", name = "TestMod", version = "2.0", dependencies = "required-after:bspkrsCore",
        guiFactory = "bspkrs.testmod.ModGuiFactoryHandler")
public class TestMod
{
    @Metadata(value = "TestMod")
    public static ModMetadata   metadata;
    
    @Instance(value = "TestMod")
    public static TestMod       instance;
    
    @SidedProxy(clientSide = "bspkrs.testmod.ClientProxy", serverSide = "bspkrs.testmod.CommonProxy")
    public static CommonProxy   proxy;
    
    protected ModVersionChecker versionChecker;
    private final String        versionURL = Const.VERSION_URL + "/Minecraft/TestMod/testMod.version";
    private final String        mcfTopic   = "http://www.minecraftforum.net/topic/1114612-";
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        
        TestModSimpleConfig.initConfig(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
        
        proxy.registerTickHandler();
    }
}
