package bspkrs.testmod;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiTestModConfig extends GuiConfig
{
    public GuiTestModConfig(GuiScreen parent)
    {
        super(parent, (new ConfigElement(TestModSimpleConfig.getConfig().getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
                "TestMod", false, false, GuiConfig.getAbridgedConfigPath(TestModSimpleConfig.getConfig().toString()));
    }
}
