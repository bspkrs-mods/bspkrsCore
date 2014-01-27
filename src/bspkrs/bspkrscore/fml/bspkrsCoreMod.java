package bspkrs.bspkrscore.fml;

import java.io.File;
import java.util.EnumSet;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.fml.util.TickerBase;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "bspkrsCore", name = "bspkrsCore", version = "v5.3(" + Const.MCVERSION + ")", dependencies = "before:*", useMetadata = true)
public class bspkrsCoreMod implements IConnectionHandler
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
    
    private Configuration       config;
    
    @SideOnly(Side.CLIENT)
    protected TickerBase        ticker;
    private boolean             isCommandRegistered;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        metadata = event.getModMetadata();
        
        File file = event.getSuggestedConfigurationFile();
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new Configuration(file);
        
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
        
        proxy.registerTickHandler();
        
        if (event.getSide().isClient())
        {
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
    
    /**
     * 2) Called when a player logs into the server SERVER SIDE
     */
    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {}
    
    /**
     * If you don't want the connection to continue, return a non-empty string here SERVER SIDE
     */
    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }
    
    /**
     * 1) Fired when a remote connection is opened CLIENT SIDE
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
    {}
    
    /**
     * 1) Fired when a local connection is opened CLIENT SIDE
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {}
    
    /**
     * Fired when a connection closes ALL SIDES
     */
    @Override
    public void connectionClosed(INetworkManager manager)
    {
        // This is probably unnecessary, but I'll leave it
        ticker.removeTicks(EnumSet.of(TickType.CLIENT));
    }
    
    /**
     * 3) Fired when the client established the connection to the server CLIENT SIDE
     */
    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        ticker.addTicks(EnumSet.of(TickType.CLIENT));
    }
}
