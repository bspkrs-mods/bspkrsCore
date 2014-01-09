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

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldWrapper extends GuiTextField
{
    public GuiTextFieldWrapper(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5)
    {
        super(par1FontRenderer, par2, par3, par4, par5);
    }
    
    public void updateCursorCounter()
    {
        this.func_146178_a();
    }
    
    public void setText(String s)
    {
        this.func_146180_a(s);
    }
    
    public String getText()
    {
        return this.func_146179_b();
    }
    
    public void setFocused(boolean bol)
    {
        this.func_146195_b(bol);
    }
    
    public boolean isFocused()
    {
        return this.func_146206_l();
    }
    
    public boolean textboxKeyTyped(char ch, int i)
    {
        return this.func_146201_a(ch, i);
    }
    
    public void mouseClicked(int x, int y, int z)
    {
        this.func_146192_a(x, y, z);
    }
    
    public void drawTextBox()
    {
        this.func_146194_f();
    }
}
