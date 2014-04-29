package bspkrs.testmod;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiTestModConfig extends GuiConfig
{
    public GuiTestModConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), Configuration.class.getDeclaredMethod("save"), TestModSimpleConfig.getConfig(),
                TestModSimpleConfig.class.getDeclaredMethod("syncConfig"), null);
    }
    
    private static IConfigProperty[] getProps()
    {
        ConfigCategory cc = TestModSimpleConfig.getConfig().getCategory(Configuration.CATEGORY_GENERAL);
        IConfigProperty[] props = new IConfigProperty[cc.getValues().size()];
        int i = -1;
        for (Property prop : cc.getValues().values())
        {
            if (prop != null)
                props[++i] = new ConfigProperty(prop);
        }
        
        return props;
    }
}
