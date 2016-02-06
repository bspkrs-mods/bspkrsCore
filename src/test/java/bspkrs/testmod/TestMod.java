package bspkrs.testmod;

import bspkrs.util.Const;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "TestMod", name = "TestMod", version = "2.0", dependencies = "required-after:bspkrsCore",
        guiFactory = "bspkrs.testmod.ModGuiFactoryHandler", updateJSON = Const.VERSION_URL_BASE + "TestMod" + Const.VERSION_URL_EXT)
public class TestMod
{
    @Metadata(value = "TestMod")
    public static ModMetadata   metadata;

    @Instance(value = "TestMod")
    public static TestMod       instance;

    @SidedProxy(clientSide = "bspkrs.testmod.ClientProxy", serverSide = "bspkrs.testmod.CommonProxy")
    public static CommonProxy   proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        TestModSimpleConfig.initConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerTickHandler();
    }
}
