package bspkrs.testmod;

import java.util.EnumSet;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import bspkrs.fml.util.TickerBase;
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
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "TestMod", name = "TestMod", version = "4.0", dependencies = "before:*")
@NetworkMod(connectionHandler = TestMod.class)
public class TestMod implements IConnectionHandler
{
    // config stuff
    public boolean              allowUpdateCheck = true;
    
    @Metadata(value = "TestMod")
    public static ModMetadata   metadata;
    
    @Instance(value = "TestMod")
    public static TestMod       instance;
    
    @SidedProxy(clientSide = "bspkrs.testmod.ClientProxy", serverSide = "bspkrs.testmod.CommonProxy")
    public static CommonProxy   proxy;
    
    protected ModVersionChecker versionChecker;
    private final String        versionURL       = Const.VERSION_URL + "/Minecraft/TestMod/testMod.version";
    private final String        mcfTopic         = "http://www.minecraftforum.net/topic/1114612-";
    
    @SideOnly(Side.CLIENT)
    protected TickerBase        ticker;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
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
        instance.ticker.removeTicks(EnumSet.of(TickType.CLIENT));
    }
    
    /**
     * 3) Fired when the client established the connection to the server CLIENT SIDE
     */
    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        instance.ticker.addTicks(EnumSet.of(TickType.CLIENT));
    }
}
