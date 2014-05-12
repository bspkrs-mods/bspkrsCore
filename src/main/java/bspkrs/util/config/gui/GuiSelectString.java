package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiSelectString extends GuiScreen
{
    protected GuiScreen               parentScreen;
    protected IConfigProperty         prop;
    private GuiSelectStringEntries    guiScrollList;
    private GuiButtonExt              btnUndoChanges, btnDefault, btnDone;
    private String                    title;
    protected String                  titleLine2;
    protected String                  titleLine3;
    protected int                     slotIndex;
    private final Map<String, String> selectableValues;
    private final String              beforeValue;
    private String                    currentValue;
    private HoverChecker              tooltipHoverChecker;
    private List                      toolTip;
    protected boolean                 enabled;
    
    public GuiSelectString(GuiScreen parentScreen, IConfigProperty prop, int slotIndex, Map<String, String> selectableValues, String currentValues, boolean enabled)
    {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.prop = prop;
        this.slotIndex = slotIndex;
        this.selectableValues = selectableValues;
        this.beforeValue = currentValues;
        this.currentValue = currentValues;
        this.toolTip = new ArrayList();
        this.enabled = enabled;
        String propName = I18n.format(prop.getLanguageKey());
        String comment;
        
        if (prop.getType().equals(ConfigGuiType.INTEGER))
            comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                    "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinIntValue(), prop.getMaxIntValue());
        else
            comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                    "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinDoubleValue(), prop.getMaxDoubleValue());
        
        if (!comment.equals(prop.getLanguageKey() + ".tooltip"))
            toolTip = mc.fontRenderer.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
        else if (prop.getComment() != null && !prop.getComment().trim().isEmpty())
            toolTip = mc.fontRenderer.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
        else
            toolTip = mc.fontRenderer.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
        
        if (parentScreen instanceof GuiConfig)
        {
            this.title = ((GuiConfig) parentScreen).title;
            this.titleLine2 = ((GuiConfig) parentScreen).titleLine2;
            this.titleLine3 = I18n.format(prop.getLanguageKey());
            this.tooltipHoverChecker = new HoverChecker(28, 37, 0, parentScreen.width, 800);
            
        }
        else
        {
            this.title = I18n.format(prop.getLanguageKey());
            this.tooltipHoverChecker = new HoverChecker(8, 17, 0, parentScreen.width, 800);
        }
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
        
        int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRenderer.getStringWidth("↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")) + 20;
        int resetWidth = mc.fontRenderer.getStringWidth("☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")) + 20;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
        this.buttonList.add(btnDone = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
        this.buttonList.add(btnDefault = new GuiButtonExt(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, "☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")));
        this.buttonList.add(btnUndoChanges = new GuiButtonExt(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, "↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                this.guiScrollList.saveChanges();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 2001)
        {
            this.currentValue = prop.getDefault();
            this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
        }
        else if (button.id == 2002)
        {
            this.currentValue = beforeValue;
            this.guiScrollList = new GuiSelectStringEntries(this, this.mc, this.prop, this.selectableValues, this.beforeValue, this.currentValue);
        }
    }
    
    /**
     * Called when the mouse is moved or a mouse button is released. Signature: (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or
     * which==1 is mouseUp
     */
    @Override
    protected void mouseMovedOrUp(int x, int y, int mouseEvent)
    {
        if (mouseEvent != 0 || !this.guiScrollList.func_148181_b(x, y, mouseEvent))
        {
            super.mouseMovedOrUp(x, y, mouseEvent);
        }
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.guiScrollList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
        
        if (this.titleLine2 != null)
            this.drawCenteredString(this.fontRendererObj, this.titleLine2, this.width / 2, 18, 16777215);
        
        if (this.titleLine3 != null)
            this.drawCenteredString(this.fontRendererObj, this.titleLine3, this.width / 2, 28, 16777215);
        
        this.btnDefault.enabled = enabled && !this.guiScrollList.isDefault();
        this.btnUndoChanges.enabled = enabled && this.guiScrollList.isChanged();
        super.drawScreen(par1, par2, par3);
        
        if (this.tooltipHoverChecker != null && this.tooltipHoverChecker.checkHover(par1, par2))
            drawToolTip(this.toolTip, par1, par2);
    }
    
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}
