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

@SuppressWarnings("deprecation")
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
        mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.properties = properties;
        propertyList = new GuiPropertyList(this, mc);
        this.areAllPropsHotLoadable = areAllPropsHotLoadable;
        this.modID = modID;
        this.allowNonHotLoadConfigChanges = allowNonHotLoadConfigChanges;
        if (title != null)
            this.title = title;
        this.titleLine2 = titleLine2;
        if ((this.titleLine2 != null) && this.titleLine2.startsWith(" > "))
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
        mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.properties = Arrays.asList(properties);
        this.saveAction = saveAction;
        this.configObject = configObject;
        this.afterSaveAction = afterSaveAction;
        this.afterSaveObject = afterSaveObject;
        propertyList = new GuiPropertyList(this, mc);
        this.titleLine2 = titleLine2;
        areAllPropsHotLoadable = false;
        modID = null;
        allowNonHotLoadConfigChanges = true;

        if (mc.mcDataDir.getAbsolutePath().endsWith("."))
            title = configObject.toString().replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/").substring(0, mc.mcDataDir.getAbsolutePath().length() - 1), "/.minecraft/");
        else
            title = configObject.toString().replace("\\", "/").replace(mc.mcDataDir.getAbsolutePath().replace("\\", "/"), "/.minecraft");
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
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        if ((propertyList == null) || needsRefresh)
        {
            propertyList = new GuiPropertyList(this, mc);
            needsRefresh = false;
        }

        int doneWidth = Math.max(mc.fontRendererObj.getStringWidth(I18n.format("gui.done")) + 20, 100);
        int undoWidth = mc.fontRendererObj.getStringWidth("↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")) + 20;
        int resetWidth = mc.fontRendererObj.getStringWidth("☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")) + 20;
        int checkWidth = mc.fontRendererObj.getStringWidth(I18n.format("bspkrs.configgui.applyGlobally")) + 13;
        int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5 + checkWidth) / 2;
        buttonList.add(new GuiButtonExt(2000, (width / 2) - buttonWidthHalf, height - 29, doneWidth, 20, I18n.format("gui.done")));
        buttonList.add(btnDefaultAll = new GuiButtonExt(2001, ((width / 2) - buttonWidthHalf) + doneWidth + 5 + undoWidth + 5, height - 29, resetWidth, 20, "☄ " + I18n.format("bspkrs.configgui.tooltip.resetToDefault")));
        buttonList.add(btnUndoAll = new GuiButtonExt(2002, ((width / 2) - buttonWidthHalf) + doneWidth + 5, height - 29, undoWidth, 20, "↩ " + I18n.format("bspkrs.configgui.tooltip.undoChanges")));
        buttonList.add(chkApplyGlobally = new GuiCheckBox(2003, ((width / 2) - buttonWidthHalf) + doneWidth + 5 + undoWidth + 5 + resetWidth + 5, height - 24, I18n.format("bspkrs.configgui.applyGlobally"), false));

        checkBoxHoverChecker = new HoverChecker(chkApplyGlobally, 800);
        propertyList.initGui();
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
                if ((parentScreen == null) || (!(parentScreen instanceof GuiConfig) && propertyList.areAnyPropsChanged(true)))
                {
                    propertyList.saveProperties();

                    if (modID == null)
                    {
                        if (saveAction != null)
                            saveAction.invoke(configObject);
                        if (afterSaveAction != null)
                            afterSaveAction.invoke(afterSaveObject);
                    }
                    else if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, allowNonHotLoadConfigChanges);
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
            mc.displayGuiScreen(parentScreen);
        }
        else if (button.id == 2001)
        {
            propertyList.setAllPropsDefault(chkApplyGlobally.isChecked());
        }
        else if (button.id == 2002)
        {
            propertyList.undoAllChanges(chkApplyGlobally.isChecked());
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        if ((mouseEvent != 0) || !propertyList.func_148179_a(x, y, mouseEvent))
        {
            propertyList.mouseClicked(x, y, mouseEvent);
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
        if ((mouseEvent != 0) || !propertyList.func_148181_b(x, y, mouseEvent))
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
            propertyList.keyTyped(eventChar, eventKey);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        propertyList.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        propertyList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);
        String title2 = titleLine2;

        int strWidth = mc.fontRendererObj.getStringWidth(title2);
        int elipsisWidth = mc.fontRendererObj.getStringWidth("...");
        if ((strWidth > (width - 6)) && (strWidth > elipsisWidth))
            title2 = mc.fontRendererObj.trimStringToWidth(title2, width - 6 - elipsisWidth).trim() + "...";
        if (title2 != null)
            this.drawCenteredString(fontRendererObj, title2, width / 2, 18, 16777215);

        btnUndoAll.enabled = propertyList.areAnyPropsEnabled(chkApplyGlobally.isChecked()) && propertyList.areAnyPropsChanged(chkApplyGlobally.isChecked());
        btnDefaultAll.enabled = propertyList.areAnyPropsEnabled(chkApplyGlobally.isChecked()) && !propertyList.areAllPropsDefault(chkApplyGlobally.isChecked());
        super.drawScreen(mouseX, mouseY, partialTicks);
        propertyList.drawScreenPost(mouseX, mouseY, partialTicks);
        if (checkBoxHoverChecker.checkHover(mouseX, mouseY))
            this.drawToolTip(Arrays.asList(new String[] { I18n.format("bspkrs.configgui.applyGlobally.tooltip") }), mouseX, mouseY);
    }

    @SuppressWarnings("rawtypes")
    public void drawToolTip(List stringList, int x, int y)
    {
        this.drawHoveringText(stringList, x, y, fontRendererObj);
    }
}