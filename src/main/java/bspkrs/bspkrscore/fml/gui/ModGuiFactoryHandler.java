package bspkrs.bspkrscore.fml.gui;

import net.minecraftforge.fml.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import java.util.*;

public class ModGuiFactoryHandler implements IModGuiFactory
{
    @Override
    public void initialize(final Minecraft minecraftInstance)
    {}

    @Override
    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new GuiBSConfig(parentScreen);
    }
}
