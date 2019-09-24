package bspkrs.testmod;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "testmod", name = "TestMod", version = "2.0", dependencies = "required-after:bspkrsCore",
        guiFactory = "bspkrs.testmod.ModGuiFactoryHandler")
public class TestMod
{
    @Metadata(value = "testmod")
    public static ModMetadata   metadata;
    
    @Instance(value = "testmod")
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
