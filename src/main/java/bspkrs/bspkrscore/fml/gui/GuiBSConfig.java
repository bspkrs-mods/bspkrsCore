package bspkrs.bspkrscore.fml.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lwjgl.input.Keyboard;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.util.BSConfiguration;

public class GuiBSConfig extends GuiScreen
{
    String                        guiTitle;
    private GuiScreen             parent;
    private GuiButton             back, save, allowUpdateCheckButton, allowDebugOutputButton, generateUniqueNamesFileButton;
    private GuiTextField          updateTimeoutMillisecondsTextBox;
    private final BSConfiguration config;
    private Property              allowUpdateCheck;
    private Property              allowDebugOutput;
    private Property              generateUniqueNamesFile;
    private Property              updateTimeoutMilliseconds;
    
    public GuiBSConfig(GuiScreen parent)
    {
        this.parent = parent;
        config = bspkrsCoreMod.instance.config;
        config.load();
        allowUpdateCheck = config.get(Configuration.CATEGORY_GENERAL, "allowUpdateCheck", bspkrsCoreMod.instance.allowUpdateCheck, bspkrsCoreMod.instance.allowUpdateCheckDesc);
        allowDebugOutput = config.get(Configuration.CATEGORY_GENERAL, "allowDebugOutput", bspkrsCoreMod.instance.allowDebugOutput, bspkrsCoreMod.instance.allowDebugOutputDesc);
        generateUniqueNamesFile = config.get(Configuration.CATEGORY_GENERAL, "generateUniqueNamesFile", bspkrsCoreMod.instance.generateUniqueNamesFile, bspkrsCoreMod.instance.generateUniqueNamesFileDesc);
        updateTimeoutMilliseconds = config.get(Configuration.CATEGORY_GENERAL, "updateTimeoutMilliseconds", bspkrsCoreMod.instance.updateTimeoutMilliseconds, bspkrsCoreMod.instance.updateTimeoutMillisecondsDesc);
        
        guiTitle = config.getConfigFile().getName();
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        buttonList.clear();
        byte byte0 = -16;
        
        int row1, row2, row3, row4, row6;
        row1 = height / 4 + 24 + byte0;
        row2 = height / 4 + 24 * 2 + byte0;
        row3 = height / 4 + 24 * 3 + byte0;
        row4 = height / 4 + 24 * 4 + byte0;
        row6 = height / 4 + 24 * 6 + byte0;
        
        allowUpdateCheckButton = new GuiButton(-1, width / 2 + 2, row1, 60, 20, String.valueOf(allowUpdateCheck.getBoolean(true)));
        allowDebugOutputButton = new GuiButton(-2, width / 2 + 2, row2, 60, 20, String.valueOf(allowDebugOutput.getBoolean(true)));
        generateUniqueNamesFileButton = new GuiButton(-5, width / 2 + 2, row3, 60, 20, String.valueOf(generateUniqueNamesFile.getBoolean(true)));
        updateTimeoutMillisecondsTextBox = new GuiTextField(fontRendererObj, width / 2 + 2, row4, 60, 20);
        updateTimeoutMillisecondsTextBox.setText(String.valueOf(updateTimeoutMilliseconds.getInt()));
        updateTimeoutMillisecondsTextBox.setEnabled(allowUpdateCheck.getBoolean(true));
        save = new GuiButton(-3, width / 2 - 62, row6, 60, 20, StatCollector.translateToLocal("bspkrs.configgui.save"));
        back = new GuiButton(-4, width / 2 + 2, row6, 60, 20, StatCollector.translateToLocal("gui.cancel"));
        
        buttonList.add(allowUpdateCheckButton);
        buttonList.add(allowDebugOutputButton);
        buttonList.add(generateUniqueNamesFileButton);
        buttonList.add(save);
        buttonList.add(back);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case -1:
                allowUpdateCheck.set(!allowUpdateCheck.getBoolean(true));
                allowUpdateCheckButton.displayString = String.valueOf(allowUpdateCheck.getBoolean(true));
                updateTimeoutMillisecondsTextBox.setEnabled(allowUpdateCheck.getBoolean(true));
                break;
            
            case -2:
                allowDebugOutput.set(!allowDebugOutput.getBoolean(true));
                allowDebugOutputButton.displayString = String.valueOf(allowDebugOutput.getBoolean(true));
                break;
            
            case -5:
                generateUniqueNamesFile.set(!generateUniqueNamesFile.getBoolean(true));
                generateUniqueNamesFileButton.displayString = String.valueOf(generateUniqueNamesFile.getBoolean(true));
                break;
            
            case -3:
                config.save();
                bspkrsCoreMod.instance.syncConfig();
                MinecraftHelper.displayGuiScreen(Minecraft.getMinecraft(), parent);
                break;
            
            case -4:
                config.load();
                MinecraftHelper.displayGuiScreen(Minecraft.getMinecraft(), parent);
                break;
        }
    }
    
    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = "0123456789";
        if (validChars.contains(String.valueOf(c)) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME || i == Keyboard.KEY_END)
            if (updateTimeoutMillisecondsTextBox.isFocused())
                updateTimeoutMillisecondsTextBox.textboxKeyTyped(c, i);
        
        save.enabled = updateTimeoutMillisecondsTextBox.getText().trim().length() > 0 && Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()) > 0;
        
        if (!updateTimeoutMillisecondsTextBox.getText().trim().isEmpty()
                && Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()) >= 100
                && Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()) <= 30000)
            updateTimeoutMilliseconds.set(Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()));
        else
            updateTimeoutMilliseconds.set(3000);
        
        if (c == '\r' && save.enabled)
            actionPerformed(save);
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        updateTimeoutMillisecondsTextBox.mouseClicked(par1, par2, par3);
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        updateTimeoutMillisecondsTextBox.updateCursorCounter();
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        updateTimeoutMillisecondsTextBox.drawTextBox();
        
        drawCenteredString(fontRendererObj, guiTitle, width / 2, height / 4 - 16, 0xffffff);
        drawString(fontRendererObj, StatCollector.translateToLocal("bspkrs.configgui.allowUpdateCheck"), width / 2 - 3 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.allowUpdateCheck")), height / 4 + 24 - 16 + 6, 0xffffff);
        drawString(fontRendererObj, StatCollector.translateToLocal("bspkrs.configgui.allowDebugOutput"), width / 2 - 3 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.allowDebugOutput")), height / 4 + 24 * 2 - 16 + 6, 0xffffff);
        drawString(fontRendererObj, StatCollector.translateToLocal("bspkrs.configgui.generateUniqueNamesFile"), width / 2 - 3 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.generateUniqueNamesFile")), height / 4 + 24 * 3 - 16 + 6, 0xffffff);
        drawString(fontRendererObj, StatCollector.translateToLocal("bspkrs.configgui.updateTimeoutMilliseconds"), width / 2 - 3 - fontRendererObj.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.updateTimeoutMilliseconds")), height / 4 + 24 * 4 - 16 + 6, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
