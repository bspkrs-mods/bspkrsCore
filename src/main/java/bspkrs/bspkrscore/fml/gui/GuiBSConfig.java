package bspkrs.bspkrscore.fml.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.bspkrscore.fml.Reference;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiBSConfig extends GuiConfig
{
    public GuiBSConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), true, Reference.MODID, true, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
    
    private static List<IConfigProperty> getProps()
    {
        List<IConfigProperty> props = (new ConfigProperty(Reference.config.getCategory(Configuration.CATEGORY_GENERAL))).getConfigPropertiesList(true);
        props.add(new ConfigProperty(Reference.config.getCategory(Configuration.CATEGORY_GENERAL + ".example_properties")));
        return props;
    }
}
