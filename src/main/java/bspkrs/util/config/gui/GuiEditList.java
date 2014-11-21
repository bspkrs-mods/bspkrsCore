package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

@Deprecated
public class GuiEditList extends GuiScreen
{
    protected GuiScreen        parentScreen;
    protected IConfigProperty  prop;
    private GuiEditListEntries guiScrollList;
    private GuiButtonExt       btnUndoChanges, btnDefault, btnDone;
    private String             title;
    protected String           titleLine2;
    protected String           titleLine3;
    protected int              slotIndex;
    private final String[]     beforeValues;
    private String[]           currentValues;
    private HoverChecker       tooltipHoverChecker;
    @SuppressWarnings("rawtypes")
    private List               toolTip;
    protected boolean          enabled;

    @SuppressWarnings("rawtypes")
    public GuiEditList(GuiScreen parentScreen, IConfigProperty prop, int slotIndex, String[] currentValues, boolean enabled)
    {
        mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.prop = prop;
        this.slotIndex = slotIndex;
        beforeValues = currentValues;
        this.currentValues = currentValues;
        toolTip = new ArrayList();
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
            toolTip = mc.fontRendererObj.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
        else if ((prop.getComment() != null) && !prop.getComment().trim().isEmpty())
            toolTip = mc.fontRendererObj.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
        else
            toolTip = mc.fontRendererObj.listFormattedStringToWidth(
                    EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);

        if (parentScreen instanceof GuiConfig)
        {
            title = ((GuiConfig) parentScreen).title;
            titleLine2 = ((GuiConfig) parentScreen).titleLine2;
            titleLine3 = I18n.format(prop.getLanguageKey());
            tooltipHoverChecker = new HoverChecker(28, 37, 0, parentScreen.width, 800);

        }
        else
        {
            title = I18n.format(prop.getLanguageKey());
            tooltipHoverChecker = new HoverChecker(8, 17, 0, parentScreen.width, 800);
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        guiScrollList = new GuiEditListEntries(this, mc, prop, beforeValues, currentValues);

        int doneWidth = Math.max(mc.fontRendererObj.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRendererObj.getStringWidth("↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")) + 20;
        int resetWidth = mc.fontRendererObj.getStringWidth("☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")) + 20;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
        buttonList.add(btnDone = new GuiButtonExt(2000, (width / 2) - buttonWidthHalf, height - 29, doneWidth, 20, I18n.format("gui.done")));
        buttonList.add(btnDefault = new GuiButtonExt(2001, ((width / 2) - buttonWidthHalf) + doneWidth + 5 + undoWidth + 5, height - 29, resetWidth, 20, "☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")));
        buttonList.add(btnUndoChanges = new GuiButtonExt(2002, ((width / 2) - buttonWidthHalf) + doneWidth + 5, height - 29, undoWidth, 20, "↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                guiScrollList.saveListChanges();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            mc.displayGuiScreen(parentScreen);
        }
        else if (button.id == 2001)
        {
            currentValues = prop.getDefaults();
            guiScrollList = new GuiEditListEntries(this, mc, prop, beforeValues, currentValues);
        }
        else if (button.id == 2002)
        {
            currentValues = Arrays.copyOf(beforeValues, beforeValues.length);
            guiScrollList = new GuiEditListEntries(this, mc, prop, beforeValues, currentValues);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        if ((mouseEvent != 0) || !guiScrollList.func_148179_a(x, y, mouseEvent))
        {
            guiScrollList.mouseClicked(x, y, mouseEvent);
            super.mouseClicked(x, y, mouseEvent);
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released. Signature: (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or
     * which==1 is mouseUp
     */
    @Override
    protected void mouseReleased(int x, int y, int mouseEvent)
    {
        if ((mouseEvent != 0) || !guiScrollList.func_148181_b(x, y, mouseEvent))
        {
            super.mouseReleased(x, y, mouseEvent);
        }
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE)
            mc.displayGuiScreen(parentScreen);
        else
            guiScrollList.keyTyped(eventChar, eventKey);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        guiScrollList.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        guiScrollList.drawScreen(par1, par2, par3);
        this.drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);

        if (titleLine2 != null)
            this.drawCenteredString(fontRendererObj, titleLine2, width / 2, 18, 16777215);

        if (titleLine3 != null)
            this.drawCenteredString(fontRendererObj, titleLine3, width / 2, 28, 16777215);

        btnDone.enabled = guiScrollList.isListSavable();
        btnDefault.enabled = enabled && !guiScrollList.isDefault();
        btnUndoChanges.enabled = enabled && guiScrollList.isChanged();
        super.drawScreen(par1, par2, par3);
        guiScrollList.drawScreenPost(par1, par2, par3);

        if ((tooltipHoverChecker != null) && tooltipHoverChecker.checkHover(par1, par2))
            drawToolTip(toolTip, par1, par2);
    }

    @SuppressWarnings("rawtypes")
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}
