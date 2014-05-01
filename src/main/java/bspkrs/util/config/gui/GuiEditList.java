package bspkrs.util.config.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

public class GuiEditList extends GuiScreen
{
    protected GuiScreen        parentScreen;
    protected IConfigProperty  prop;
    private GuiEditListEntries guiScrollList;
    private GuiButton          btnResetAll, btnDone;
    private String             title;
    protected String           titleLine2;
    protected int              slotIndex;
    private String[]           currentValues;
    
    public GuiEditList(GuiScreen parentScreen, IConfigProperty prop, int slotIndex, String[] currentValues)
    {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.prop = prop;
        this.slotIndex = slotIndex;
        this.currentValues = currentValues;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        this.guiScrollList = new GuiEditListEntries(this, this.mc, this.prop, this.currentValues);
        this.buttonList.add(this.btnDone = new GuiButton(2000, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.btnResetAll = new GuiButton(2001, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.reset", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                this.guiScrollList.saveListChanges();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 2001)
        {
            this.currentValues = prop.getDefaults();
            this.guiScrollList = new GuiEditListEntries(this, this.mc, this.prop, this.currentValues);
        }
    }
    
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        if (mouseEvent != 0 || !this.guiScrollList.func_148179_a(x, y, mouseEvent))
        {
            this.guiScrollList.mouseClicked(x, y, mouseEvent);
            super.mouseClicked(x, y, mouseEvent);
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
    
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE)
            this.mc.displayGuiScreen(parentScreen);
        else
            this.guiScrollList.keyTyped(eventChar, eventKey);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.guiScrollList.updateScreen();
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
        
        this.btnDone.enabled = this.guiScrollList.isListSavable();
        this.btnResetAll.enabled = !this.guiScrollList.isDefault();
        super.drawScreen(par1, par2, par3);
        this.guiScrollList.drawScreenPost(par1, par2, par3);
    }
    
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}
