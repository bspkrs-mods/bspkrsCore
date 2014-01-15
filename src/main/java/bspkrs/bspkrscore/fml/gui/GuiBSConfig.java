package bspkrs.bspkrscore.fml.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lwjgl.input.Keyboard;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.helpers.client.gui.GuiScreenWrapper;
import bspkrs.helpers.client.gui.GuiTextFieldWrapper;
import bspkrs.util.BSConfiguration;

public class GuiBSConfig extends GuiScreenWrapper
{
    String                        guiTitle;
    private GuiScreen             parent;
    private GuiButton             back, save, allowUpdateCheckButton, allowDebugOutputButton;
    private GuiTextFieldWrapper   updateTimeoutMillisecondsTextBox;
    private final BSConfiguration config;
    private Property              allowUpdateCheck;
    private Property              allowDebugOutput;
    private Property              updateTimeoutMilliseconds;
    
    public GuiBSConfig(GuiScreen parent)
    {
        this.parent = parent;
        config = bspkrsCoreMod.instance.config;
        config.load();
        allowUpdateCheck = config.get(Configuration.CATEGORY_GENERAL, "allowUpdateCheck", true);
        allowDebugOutput = config.get(Configuration.CATEGORY_GENERAL, "allowDebugOutput", false);
        updateTimeoutMilliseconds = config.get(Configuration.CATEGORY_GENERAL, "updateTimeoutMilliseconds", 3000);
        
        guiTitle = config.getConfigFile().getName();
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        buttonList().clear();
        byte byte0 = -16;
        
        int row1, row2, row3, row5;
        row1 = height() / 4 + 24 + byte0;
        row2 = height() / 4 + 24 * 2 + byte0;
        row3 = height() / 4 + 24 * 3 + byte0;
        row5 = height() / 4 + 24 * 5 + byte0;
        
        allowUpdateCheckButton = new GuiButton(-1, width() / 2 + 2, row1, 60, 20, String.valueOf(allowUpdateCheck.getBoolean(true)));
        allowDebugOutputButton = new GuiButton(-2, width() / 2 + 2, row2, 60, 20, String.valueOf(allowDebugOutput.getBoolean(true)));
        updateTimeoutMillisecondsTextBox = new GuiTextFieldWrapper(field_146289_q, width() / 2 + 2, row3, 60, 20);
        updateTimeoutMillisecondsTextBox.setText(String.valueOf(updateTimeoutMilliseconds.getInt()));
        save = new GuiButton(-3, width() / 2 - 62, row5, 60, 20, StatCollector.translateToLocal("bspkrs.configgui.save"));
        back = new GuiButton(-4, width() / 2 + 2, row5, 60, 20, StatCollector.translateToLocal("gui.cancel"));
        
        buttonList().add(allowUpdateCheckButton);
        buttonList().add(allowDebugOutputButton);
        buttonList().add(save);
        buttonList().add(back);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void func_146284_a(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.field_146127_k)
        {
            case -1:
                allowUpdateCheck.set(!allowUpdateCheck.getBoolean(true));
                allowUpdateCheckButton.field_146126_j = String.valueOf(allowUpdateCheck.getBoolean(true));
                break;
            
            case -2:
                allowDebugOutput.set(!allowDebugOutput.getBoolean(true));
                allowDebugOutputButton.field_146126_j = String.valueOf(allowDebugOutput.getBoolean(true));
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
        
        save.field_146124_l = updateTimeoutMillisecondsTextBox.getText().trim().length() > 0 && Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()) > 0;
        
        if (!updateTimeoutMillisecondsTextBox.getText().trim().isEmpty())
            updateTimeoutMilliseconds.set(Integer.valueOf(updateTimeoutMillisecondsTextBox.getText().trim()));
        else
            updateTimeoutMilliseconds.set(3000);
        
        if (c == '\r' && save.field_146124_l)
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
        
        drawCenteredString(field_146289_q, guiTitle, width() / 2, height() / 4 - 16, 0xffffff);
        drawString(field_146289_q, StatCollector.translateToLocal("bspkrs.configgui.allowUpdateCheck"), width() / 2 - 3 - field_146289_q.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.allowUpdateCheck")), height() / 4 + 24 - 16 + 6, 0xffffff);
        drawString(field_146289_q, StatCollector.translateToLocal("bspkrs.configgui.allowDebugOutput"), width() / 2 - 3 - field_146289_q.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.allowDebugOutput")), height() / 4 + 24 * 2 - 16 + 6, 0xffffff);
        drawString(field_146289_q, StatCollector.translateToLocal("bspkrs.configgui.updateTimeoutMilliseconds"), width() / 2 - 3 - field_146289_q.getStringWidth(StatCollector.translateToLocal("bspkrs.configgui.updateTimeoutMilliseconds")), height() / 4 + 24 * 3 - 16 + 6, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
