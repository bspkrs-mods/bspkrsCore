package bspkrs.util.config.gui;

import net.minecraft.client.gui.GuiButton;
import bspkrs.util.ReflectionHelper;

public class HoverChecker
{
    private int       top, bottom, left, right, threshold;
    private GuiButton button;
    private long      hoverStart;
    
    public HoverChecker(int top, int bottom, int left, int right, int threshold)
    {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.threshold = threshold;
        this.hoverStart = -1;
    }
    
    public HoverChecker(GuiButton button, int threshold)
    {
        this.button = button;
        this.threshold = threshold;
    }
    
    public void updateBounds(int top, int bottom, int left, int right)
    {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }
    
    public boolean checkHover(int mouseX, int mouseY)
    {
        return checkHover(mouseX, mouseY, true);
    }
    
    public boolean checkHover(int mouseX, int mouseY, boolean canHover)
    {
        if (this.button != null)
        {
            this.top = button.yPosition;
            this.bottom = button.yPosition + ReflectionHelper.getIntValue(GuiButton.class, "field_146121_g", "height", button, 18);
            this.left = button.xPosition;
            this.right = button.xPosition + button.getButtonWidth();
        }
        
        if (canHover && hoverStart == -1 && mouseY >= top && mouseY <= bottom && mouseX >= left && mouseX <= right)
            hoverStart = System.currentTimeMillis();
        else if (!canHover || mouseY < top || mouseY > bottom || mouseX < left || mouseX > right)
            resetHoverTimer();
        
        return canHover && hoverStart != -1 && System.currentTimeMillis() - hoverStart >= threshold;
    }
    
    public void resetHoverTimer()
    {
        hoverStart = -1;
    }
}
