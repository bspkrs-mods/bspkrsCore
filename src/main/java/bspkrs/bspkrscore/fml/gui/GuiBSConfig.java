package bspkrs.bspkrscore.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.bspkrscore.fml.ConfigElement;
import bspkrs.bspkrscore.fml.Reference;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.Property;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiBSConfig extends GuiConfig
{
    public GuiBSConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), true, "bspkrsCore", true, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
    
    private static IConfigProperty[] getProps()
    {
        ConfigCategory cc = Reference.config.getCategory(Configuration.CATEGORY_GENERAL);
        IConfigProperty[] props = new IConfigProperty[ConfigElement.values().length + 1];
        for (int i = 0; i < ConfigElement.values().length; i++)
        {
            ConfigElement ce = ConfigElement.values()[i];
            Property prop = cc.get(ce.key());
            if (prop != null)
                props[i] = new ConfigProperty(prop, ce.propertyType());
        }
        
        props[props.length - 1] = new ConfigProperty(Reference.config.getCategory(Configuration.CATEGORY_GENERAL + ".example_properties"));
        
        return props;
    }
}
