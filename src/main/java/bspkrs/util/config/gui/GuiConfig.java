package bspkrs.util.config.gui;

import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiConfig extends GuiScreen
{
    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen           parentScreen;
    protected String            title = "Controls";
    protected IConfigProperty[] properties;
    private GuiPropertyList     propertyList;
    private GuiButton           btnResetAll;
    private Method              saveAction;
    private Object              configObject;
    private Method              afterSaveAction;
    private Object              afterSaveObject;
    
    public GuiConfig(GuiScreen par1GuiScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        this.parentScreen = par1GuiScreen;
        this.properties = properties;
        this.saveAction = saveAction;
        this.configObject = configObject;
        this.afterSaveAction = afterSaveAction;
        this.afterSaveObject = afterSaveObject;
        this.propertyList = new GuiPropertyList(this, Minecraft.getMinecraft());
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        this.propertyList = new GuiPropertyList(this, this.mc);
        this.buttonList.add(new GuiButton(2000, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.btnResetAll = new GuiButton(2001, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.reset", new Object[0])));
        this.title = configObject.toString().replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/").substring(0, mc.mcDataDir.getAbsolutePath().length() - 1), ".minecraft/");
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                this.propertyList.saveProperties();
                this.saveAction.invoke(configObject);
                this.afterSaveAction.invoke(afterSaveObject);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 2001)
        {
            for (int i = 0; i < properties.length; i++)
            {
                properties[i].setToDefault();
            }
        }
    }
    
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        if (mouseEvent != 0 || !this.propertyList.func_148179_a(x, y, mouseEvent))
        {
            this.propertyList.mouseClicked(x, y, mouseEvent);
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
        if (mouseEvent != 0 || !this.propertyList.func_148181_b(x, y, mouseEvent))
        {
            super.mouseMovedOrUp(x, y, mouseEvent);
        }
    }
    
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        this.propertyList.keyTyped(eventChar, eventKey);
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.propertyList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
        this.btnResetAll.enabled = false;
        
        for (int i = 0; i < properties.length; i++)
            if (!properties[i].isDefault())
            {
                this.btnResetAll.enabled = true;
                break;
            }
        
        super.drawScreen(par1, par2, par3);
    }
    
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}