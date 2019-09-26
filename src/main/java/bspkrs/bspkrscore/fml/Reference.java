package bspkrs.bspkrscore.fml;

import java.io.InputStream;
import java.util.Properties;

import net.minecraftforge.common.config.Configuration;

public class Reference
{
    static
    {
        Properties prop = new Properties();

        try
        {
            InputStream stream = Reference.class.getClassLoader().getResourceAsStream("version.properties");
            prop.load(stream);
            stream.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        MC_VERSION = prop.getProperty("version.minecraft");
    }
    public static final String  MODID        = "bspkrscore";
    public static final String  NAME         = "bspkrs Core";
    public static final String  MOD_VERSION  = "8.0.0";
    public static final String  MC_VERSION;
    public static final String  MINECRAFT    = "minecraft";
    public static final String  PROXY_COMMON = "bspkrs.bspkrscore.fml.CommonProxy";
    public static final String  PROXY_CLIENT = "bspkrs.bspkrscore.fml.ClientProxy";
    public static final String  GUI_FACTORY  = "bspkrs.bspkrscore.fml.gui.ModGuiFactoryHandler";

    public static Configuration config       = null;
}