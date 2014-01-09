package bspkrs.helpers.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenWrapper extends GuiScreen
{
    public List buttonList()
    {
        return field_146292_n;
    }
    
    public void drawDefaultBackground()
    {
        this.func_146276_q_();
    }
    
    public int height()
    {
        return this.field_146295_m;
    }
    
    public int width()
    {
        return this.field_146294_l;
    }
    
    protected void actionPerformed(GuiButton button)
    {
        this.func_146284_a(button);
    }
}
