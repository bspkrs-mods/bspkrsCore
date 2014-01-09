/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
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
