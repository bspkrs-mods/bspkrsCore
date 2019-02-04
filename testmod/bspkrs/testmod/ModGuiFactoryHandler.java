package bspkrs.testmod;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ModGuiFactoryHandler implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraftInstance)
    {   
        
    }

    @Override
    public boolean hasConfigGui() {
        return false;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiTestModConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

}
