package bspkrs.bspkrscore.fml.gui;

import java.lang.reflect.Method;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.bspkrscore.fml.ConfigElement;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
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
        super(parent, getProps(), Configuration.class.getDeclaredMethod("save"), bspkrsCoreMod.instance.getConfig(),
                bspkrsCoreMod.class.getDeclaredMethod("syncConfig"), bspkrsCoreMod.instance);
    }
    
    public GuiBSConfig(GuiScreen par1GuiScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        super(par1GuiScreen, properties, saveAction, configObject, afterSaveAction, afterSaveObject);
    }
    
    private static IConfigProperty[] getProps()
    {
        ConfigCategory cc = bspkrsCoreMod.instance.getConfig().getCategory(Configuration.CATEGORY_GENERAL);
        IConfigProperty[] props = new IConfigProperty[ConfigElement.values().length];
        for (int i = 0; i < ConfigElement.values().length; i++)
        {
            ConfigElement ce = ConfigElement.values()[i];
            Property prop = cc.get(ce.key());
            if (prop != null)
                props[i] = new ConfigProperty(prop, ce.propertyType());
        }
        
        return props;
    }
}
