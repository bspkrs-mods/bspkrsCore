package bspkrs.bspkrscore.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import bspkrs.bspkrscore.fml.Reference;

public class GuiBSConfig extends GuiConfig
{
	public GuiBSConfig (GuiScreen parent)
	{
		super(parent, (new ConfigElement(Reference.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
	}
}
