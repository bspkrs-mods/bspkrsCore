package bspkrs.bspkrscore.fml;

import net.minecraftforge.common.config.*;
import java.util.*;
import com.google.common.base.*;
import java.io.*;

@SuppressWarnings("deprecation")
public class Reference
{
    public static final String MODID = "bspkrscore";
    public static final String NAME = "bspkrsCore";
    public static final String MC_VERSION = "[1.12.2]";
    public static final String VERSION = "7.02";
    public static final String MINECRAFT = "minecraft";
    public static final String PROXY_COMMON = "bspkrs.bspkrscore.fml.CommonProxy";
    public static final String PROXY_CLIENT = "bspkrs.bspkrscore.fml.ClientProxy";
    public static final String GUI_FACTORY = "bspkrs.bspkrscore.fml.gui.ModGuiFactoryHandler";
    public static Configuration config;

    static
    {
        final Properties prop = new Properties();
        try
        {
            final InputStream stream = Reference.class.getClassLoader().getResourceAsStream("version.properties");
            prop.load(stream);
            stream.close();
        }
        catch(Exception e)
        {
            Throwables.propagate((Throwable)e);
        }
        // MC_VERSION = prop.getProperty("version.minecraft");
        Reference.config = null;
    }
}
