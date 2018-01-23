package bspkrs.bspkrscore.fml.gui;

import net.minecraftforge.fml.client.config.*;
import net.minecraft.client.gui.*;
import bspkrs.bspkrscore.fml.*;
import net.minecraftforge.common.config.ConfigElement;

public class GuiBSConfig extends GuiConfig
{
    public GuiBSConfig(final GuiScreen parent)
    {
        super(parent, new ConfigElement(Reference.config.getCategory("general")).getChildElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
}
