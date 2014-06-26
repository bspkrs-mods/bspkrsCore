package bspkrs.bspkrscore.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import bspkrs.bspkrscore.fml.Reference;
import cpw.mods.fml.client.config.GuiConfig;

public class GuiBSConfig extends GuiConfig
{
    public GuiBSConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, (new ConfigElement(Reference.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
                Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
}
