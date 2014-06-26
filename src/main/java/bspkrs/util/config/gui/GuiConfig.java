package bspkrs.util.config.gui;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import bspkrs.util.config.ConfigChangedEvent;
import bspkrs.util.config.ConfigChangedEvent.OnConfigChangedEvent;
import bspkrs.util.config.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Deprecated
public class GuiConfig extends GuiScreen
{
    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    public final GuiScreen             parentScreen;
    public String                      title        = "Config GUI";
    public String                      titleLine2;
    public final List<IConfigProperty> properties;
    public GuiPropertyList             propertyList;
    private GuiButtonExt               btnDefaultAll;
    private GuiButtonExt               btnUndoAll;
    private GuiCheckBox                chkApplyGlobally;
    @Deprecated
    protected Method                   saveAction;
    @Deprecated
    protected Object                   configObject;
    @Deprecated
    protected Method                   afterSaveAction;
    @Deprecated
    protected Object                   afterSaveObject;
    public final String                modID;
    public final boolean               allowNonHotLoadConfigChanges;
    public final boolean               areAllPropsHotLoadable;
    private boolean                    needsRefresh = true;
    private HoverChecker               checkBoxHoverChecker;
    
    @Deprecated
    public GuiConfig(GuiScreen parentScreen, IConfigProperty[] properties, boolean areAllPropsHotLoadable, String modID,
            boolean allowNonHotLoadConfigChanges, String title)
    {
        this(parentScreen, properties, areAllPropsHotLoadable, modID, allowNonHotLoadConfigChanges, title, null);
    }
    
    /**
     * GuiConfig constructor that will use ConfigChangedEvent when editing is concluded.
     * 
     * @param parentScreen the parent GuiScreen object
     * @param properties a List of IConfigProperty objects
     * @param areAllPropsHotLoadable send true if every property on this screen is able to be modified on the fly while a world is running
     * @param modID the mod ID for the mod whose config settings will be edited
     * @param allowNonHotLoadConfigChanges send true if all config properties can be modified, send false when only isHotLoadable() == true
     *            properties can be edited
     * @param title the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
     *            edited.
     */
    public GuiConfig(GuiScreen parentScreen, List<IConfigProperty> properties, boolean areAllPropsHotLoadable, String modID,
            boolean allowNonHotLoadConfigChanges, String title)
    {
        this(parentScreen, properties, areAllPropsHotLoadable, modID, allowNonHotLoadConfigChanges, title, null);
    }
    
    @Deprecated
    public GuiConfig(GuiScreen parentScreen, IConfigProperty[] properties, boolean areAllPropsHotLoadable, String modID,
            boolean allowNonHotLoadConfigChanges, String title, String titleLine2)
    {
        this(parentScreen, Arrays.asList(properties), areAllPropsHotLoadable, modID, allowNonHotLoadConfigChanges, title, titleLine2);
    }
    
    /**
     * GuiConfig constructor that will use ConfigChangedEvent when editing is concluded.
     * 
     * @param parentScreen the parent GuiScreen object
     * @param properties a List of IConfigProperty objects
     * @param modID the mod ID for the mod whose config settings will be edited
     * @param allowNonHotLoadConfigChanges send true if all config properties can be modified, send false when only isHotLoadable() == true
     *            properties can be edited
     * @param title the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
     *            edited.
     * @param titleLine2 the desired title second line for this screen. Typically this is used to send the category path of the category
     *            currently being edited.
     */
    public GuiConfig(GuiScreen parentScreen, List<IConfigProperty> properties, boolean areAllPropsHotLoadable, String modID,
            boolean allowNonHotLoadConfigChanges, String title, String titleLine2)
    {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.properties = properties;
        this.propertyList = new GuiPropertyList(this, mc);
        this.areAllPropsHotLoadable = areAllPropsHotLoadable;
        this.modID = modID;
        this.allowNonHotLoadConfigChanges = allowNonHotLoadConfigChanges;
        if (title != null)
            this.title = title;
        this.titleLine2 = titleLine2;
        if (this.titleLine2 != null && this.titleLine2.startsWith(" > "))
            this.titleLine2 = this.titleLine2.replaceFirst(" > ", "");
    }
    
    @Deprecated
    public GuiConfig(GuiScreen parentScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        this(parentScreen, properties, saveAction, configObject, afterSaveAction, afterSaveObject, null);
    }
    
    @Deprecated
    public GuiConfig(GuiScreen parentScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject, String titleLine2)
    {
        this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.properties = Arrays.asList(properties);
        this.saveAction = saveAction;
        this.configObject = configObject;
        this.afterSaveAction = afterSaveAction;
        this.afterSaveObject = afterSaveObject;
        this.propertyList = new GuiPropertyList(this, mc);
        this.titleLine2 = titleLine2;
        this.areAllPropsHotLoadable = false;
        this.modID = null;
        this.allowNonHotLoadConfigChanges = true;
        
        if (mc.mcDataDir.getAbsolutePath().endsWith("."))
            this.title = configObject.toString().replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/").substring(0, mc.mcDataDir.getAbsolutePath().length() - 1), "/.minecraft/");
        else
            this.title = configObject.toString().replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/"), "/.minecraft");
    }
    
    public static String getAbridgedConfigPath(String path)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.mcDataDir.getAbsolutePath().endsWith("."))
            return path.replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/").substring(0, mc.mcDataDir.getAbsolutePath().length() - 1), "/.minecraft/");
        else
            return path.replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/"), "/.minecraft");
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        
        if (this.propertyList == null || this.needsRefresh)
        {
            this.propertyList = new GuiPropertyList(this, mc);
            this.needsRefresh = false;
        }
        
        int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRenderer.getStringWidth("↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")) + 20;
        int resetWidth = mc.fontRenderer.getStringWidth("☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")) + 20;
        int checkWidth = mc.fontRenderer.getStringWidth(I18n.format("bspkrs.configgui.applyGlobally")) + 13;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5 + checkWidth) / 2;
        this.buttonList.add(new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
        this.buttonList.add(this.btnDefaultAll = new GuiButtonExt(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, "☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")));
        this.buttonList.add(btnUndoAll = new GuiButtonExt(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, "↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")));
        this.buttonList.add(chkApplyGlobally = new GuiCheckBox(2003, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5 + resetWidth + 5, this.height - 24, I18n.format("bspkrs.configgui.applyGlobally"), false));
        
        this.checkBoxHoverChecker = new HoverChecker(chkApplyGlobally, 800);
        this.propertyList.initGui();
    }
    
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                if (this.parentScreen == null || !(this.parentScreen instanceof GuiConfig) && this.propertyList.areAnyPropsChanged(true))
                {
                    this.propertyList.saveProperties();
                    
                    if (modID == null)
                    {
                        if (saveAction != null)
                            this.saveAction.invoke(configObject);
                        if (afterSaveAction != null)
                            this.afterSaveAction.invoke(afterSaveObject);
                    }
                    else if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, this.allowNonHotLoadConfigChanges);
                        FMLCommonHandler.instance().bus().post(event);
                        if (!event.getResult().equals(Result.DENY))
                            FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(modID, allowNonHotLoadConfigChanges));
                    }
                }
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 2001)
        {
            this.propertyList.setAllPropsDefault(this.chkApplyGlobally.isChecked());
        }
        else if (button.id == 2002)
        {
            this.propertyList.undoAllChanges(this.chkApplyGlobally.isChecked());
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
        if (eventKey == Keyboard.KEY_ESCAPE)
            this.mc.displayGuiScreen(parentScreen);
        else
            this.propertyList.keyTyped(eventChar, eventKey);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.propertyList.updateScreen();
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.propertyList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
        String title2 = this.titleLine2;
        
        int strWidth = mc.fontRenderer.getStringWidth(title2);
        int elipsisWidth = mc.fontRenderer.getStringWidth("...");
        if (strWidth > width - 6 && strWidth > elipsisWidth)
            title2 = mc.fontRenderer.trimStringToWidth(title2, width - 6 - elipsisWidth).trim() + "...";
        if (title2 != null)
            this.drawCenteredString(this.fontRendererObj, title2, this.width / 2, 18, 16777215);
        
        this.btnUndoAll.enabled = this.propertyList.areAnyPropsEnabled(this.chkApplyGlobally.isChecked()) && this.propertyList.areAnyPropsChanged(this.chkApplyGlobally.isChecked());
        this.btnDefaultAll.enabled = this.propertyList.areAnyPropsEnabled(this.chkApplyGlobally.isChecked()) && !this.propertyList.areAllPropsDefault(this.chkApplyGlobally.isChecked());
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.propertyList.drawScreenPost(mouseX, mouseY, partialTicks);
        if (this.checkBoxHoverChecker.checkHover(mouseX, mouseY))
            this.drawToolTip(Arrays.asList(new String[] { I18n.format("bspkrs.configgui.applyGlobally.tooltip") }), mouseX, mouseY);
    }
    
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}