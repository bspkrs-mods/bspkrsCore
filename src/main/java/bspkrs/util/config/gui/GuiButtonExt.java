package bspkrs.util.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import bspkrs.client.util.HUDUtils;

public class GuiButtonExt extends GuiButton
{
    public GuiButtonExt(int id, int xPos, int yPos, String displayString)
    {
        super(id, xPos, yPos, displayString);
    }
    
    public GuiButtonExt(int id, int xPos, int yPos, int width, int height, String displayString)
    {
        super(id, xPos, yPos, width, height, displayString);
    }
    
    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            HUDUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;
            
            if (packedFGColour != 0)
            {
                color = packedFGColour;
            }
            else if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.field_146123_n)
            {
                color = 16777120;
            }
            
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int elipsisWidth = mc.fontRenderer.getStringWidth("...");
            
            if (strWidth > width - 6 && strWidth > elipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - elipsisWidth).trim() + "...";
            
            this.drawCenteredString(mc.fontRenderer, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);
        }
    }
}
