package bspkrs.util.config.gui;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import bspkrs.util.CommonUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPropertyList extends GuiListExtended
{
    private final GuiConfig             parentGuiConfig;
    private final Minecraft             mc;
    private final IGuiConfigListEntry[] listEntries;
    private int                         maxLabelTextWidth = 0;
    
    public GuiPropertyList(GuiConfig parent, Minecraft mc)
    {
        super(mc, parent.width, parent.height, 23, parent.height - 32, 20);
        this.parentGuiConfig = parent;
        this.setShowSelectionBox(false);
        this.mc = mc;
        this.listEntries = new IGuiConfigListEntry[parent.properties.length];
        int i = 0;
        String s = null;
        
        for (int k = 0; k < parent.properties.length; ++k)
        {
            IConfigProperty prop = parent.properties[k];
            
            int l = mc.fontRenderer.getStringWidth(I18n.format(prop.getLanguageKey(), new Object[0]));
            
            if (l > this.maxLabelTextWidth)
            {
                this.maxLabelTextWidth = l;
            }
            
            if (prop.getType().equals(Boolean.class))
                this.listEntries[i++] = new GuiPropertyList.BooleanProp(prop, null);
            else if (prop.getType().equals(int.class))
                this.listEntries[i++] = new GuiPropertyList.IntegerProp(prop, null);
            else if (prop.getType().equals(String.class))
            {
                if (prop.getValidValues().length > 0)
                    this.listEntries[i++] = new GuiPropertyList.SelectStringProp(prop, null);
                else
                    this.listEntries[i++] = new GuiPropertyList.StringProp(prop, null);
            }
        }
    }
    
    @Override
    protected int getSize()
    {
        return this.listEntries.length;
    }
    
    /**
     * Gets the IGuiListEntry object for the given index
     */
    @Override
    public IGuiConfigListEntry getListEntry(int index)
    {
        return this.listEntries[index];
    }
    
    @Override
    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 15;
    }
    
    /**
     * Gets the width of the list
     */
    @Override
    public int getListWidth()
    {
        return super.getListWidth() + 32;
    }
    
    protected void keyTyped(char eventChar, int eventKey)
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].keyTyped(eventChar, eventKey);
    }
    
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].mouseClicked(x, y, mouseEvent);
    }
    
    @SideOnly(Side.CLIENT)
    public class BooleanProp implements IGuiConfigListEntry
    {
        private final IConfigProperty prop;
        private final String          propName;
        private final GuiButton       btnValue;
        private final GuiButton       btnDefault;
        
        private BooleanProp(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey(), new Object[0]);
            this.btnValue = new GuiButton(0, 0, 0, 75, 18, I18n.format(String.valueOf(prop.getBoolean()), new Object[0]));
            this.btnDefault = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void drawEntry(int p_148279_1_, int x, int y, int top, int bottom, Tessellator tessellator, int mouseX, int mouseY, boolean p_148279_9_)
        {
            GuiPropertyList.this.mc.fontRenderer.drawString(this.propName, x + 90 - GuiPropertyList.this.maxLabelTextWidth, y + bottom / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnDefault.xPosition = x + 190;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !this.prop.isDefault();
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            this.btnValue.xPosition = x + 105;
            this.btnValue.yPosition = y;
            this.btnValue.displayString = String.valueOf(this.prop.getBoolean());
            this.btnValue.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
        }
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnValue.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                prop.set(!prop.getBoolean());
                return true;
            }
            else if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                prop.setToDefault();
                return true;
            }
            else
                return false;
        }
        
        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnValue.mouseReleased(x, y);
            this.btnDefault.mouseReleased(x, y);
        }
        
        BooleanProp(IConfigProperty prop, Object obj)
        {
            this(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {}
        
        @Override
        public void updateCursorCounter()
        {}
        
        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {}
    }
    
    @SideOnly(Side.CLIENT)
    public class SelectStringProp implements IGuiConfigListEntry
    {
        private final IConfigProperty prop;
        private final String          propName;
        private final GuiButton       btnValue;
        private final GuiButton       btnDefault;
        private int                   index;
        
        private SelectStringProp(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey(), new Object[0]);
            this.btnValue = new GuiButton(0, 0, 0, 75, 18, I18n.format(prop.getString(), new Object[0]));
            this.btnDefault = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
            
            resetIndex();
        }
        
        private void resetIndex()
        {
            for (int i = 0; i < prop.getValidValues().length; i++)
                if (prop.getValidValues()[i].equalsIgnoreCase(prop.getString()))
                {
                    index = i;
                    break;
                }
        }
        
        @Override
        public void drawEntry(int p_148279_1_, int x, int y, int top, int bottom, Tessellator tessellator, int mouseX, int mouseY, boolean p_148279_9_)
        {
            GuiPropertyList.this.mc.fontRenderer.drawString(this.propName, x + 90 - GuiPropertyList.this.maxLabelTextWidth, y + bottom / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnDefault.xPosition = x + 190;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !this.prop.isDefault();
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            this.btnValue.xPosition = x + 105;
            this.btnValue.yPosition = y;
            this.btnValue.displayString = this.prop.getString();
            this.btnValue.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
        }
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnValue.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                if (++this.index >= prop.getValidValues().length)
                    this.index = 0;
                
                prop.set(prop.getValidValues()[this.index]);
                
                btnValue.displayString = prop.getString();
                return true;
            }
            else if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                prop.setToDefault();
                resetIndex();
                return true;
            }
            else
                return false;
        }
        
        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnValue.mouseReleased(x, y);
            this.btnDefault.mouseReleased(x, y);
        }
        
        SelectStringProp(IConfigProperty prop, Object obj)
        {
            this(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {}
        
        @Override
        public void updateCursorCounter()
        {}
        
        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {}
    }
    
    @SideOnly(Side.CLIENT)
    public class StringProp implements IGuiConfigListEntry
    {
        private final IConfigProperty prop;
        private final String          propName;
        private final GuiTextField    textFieldValue;
        private final GuiButton       btnDefault;
        
        private StringProp(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey(), new Object[0]);
            this.textFieldValue = new GuiTextField(GuiPropertyList.this.mc.fontRenderer, 0, 0, 75, 18);
            this.btnDefault = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void drawEntry(int p_148279_1_, int x, int y, int top, int bottom, Tessellator tessellator, int mouseX, int mouseY, boolean p_148279_9_)
        {
            GuiPropertyList.this.mc.fontRenderer.drawString(this.propName, x + 90 - GuiPropertyList.this.maxLabelTextWidth, y + bottom / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnDefault.xPosition = x + 190;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !this.prop.isDefault();
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            try
            {
                Field xPos;
                Field yPos;
                if (CommonUtils.isObfuscatedEnv())
                {
                    xPos = GuiTextField.class.getDeclaredField("field_146209_f");
                    yPos = GuiTextField.class.getDeclaredField("field_146210_g");
                }
                else
                {
                    xPos = GuiTextField.class.getDeclaredField("xPosition");
                    yPos = GuiTextField.class.getDeclaredField("yPosition");
                }
                xPos.setAccessible(true);
                xPos.setInt(this.textFieldValue, x + 105);
                yPos.setAccessible(true);
                yPos.setInt(this.textFieldValue, y);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.textFieldValue.setText(prop.getString());
            this.textFieldValue.drawTextBox();
        }
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                this.prop.setToDefault();
                return true;
            }
            else
            {
                return false;
            }
        }
        
        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnDefault.mouseReleased(x, y);
        }
        
        StringProp(IConfigProperty prop, Object obj)
        {
            this(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            String before = this.textFieldValue.getText();
            this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
            
            if (!before.equals(this.textFieldValue.getText()))
                if (this.textFieldValue.getText().trim().length() > 0)
                    this.prop.set(this.textFieldValue.getText().trim());
                else
                    this.prop.setToDefault();
        }
        
        @Override
        public void updateCursorCounter()
        {
            this.textFieldValue.updateCursorCounter();
        }
        
        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {
            this.textFieldValue.mouseClicked(x, y, mouseEvent);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public class IntegerProp implements IGuiConfigListEntry
    {
        private final IConfigProperty prop;
        private final String          propName;
        private final GuiTextField    textFieldValue;
        private final GuiButton       btnDefault;
        
        private IntegerProp(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey(), new Object[0]);
            this.textFieldValue = new GuiTextField(GuiPropertyList.this.mc.fontRenderer, 0, 0, 75, 18);
            this.btnDefault = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void drawEntry(int p_148279_1_, int x, int y, int top, int bottom, Tessellator tessellator, int mouseX, int mouseY, boolean p_148279_9_)
        {
            GuiPropertyList.this.mc.fontRenderer.drawString(this.propName, x + 90 - GuiPropertyList.this.maxLabelTextWidth, y + bottom / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnDefault.xPosition = x + 190;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !this.prop.isDefault();
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            try
            {
                Field xPos;
                Field yPos;
                if (CommonUtils.isObfuscatedEnv())
                {
                    xPos = GuiTextField.class.getDeclaredField("field_146209_f");
                    yPos = GuiTextField.class.getDeclaredField("field_146210_g");
                }
                else
                {
                    xPos = GuiTextField.class.getDeclaredField("xPosition");
                    yPos = GuiTextField.class.getDeclaredField("yPosition");
                }
                xPos.setAccessible(true);
                xPos.setInt(this.textFieldValue, x + 105);
                yPos.setAccessible(true);
                yPos.setInt(this.textFieldValue, y);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.textFieldValue.setText(prop.getString());
            this.textFieldValue.drawTextBox();
        }
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                this.prop.setToDefault();
                return true;
            }
            else
            {
                return false;
            }
        }
        
        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnDefault.mouseReleased(x, y);
        }
        
        IntegerProp(IConfigProperty prop, Object obj)
        {
            this(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            String validChars = "0123456789";
            String before = this.textFieldValue.getText();
            if (validChars.contains(String.valueOf(eventChar)) || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
                    || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
            
            if (!before.equals(this.textFieldValue.getText()))
                if (this.textFieldValue.getText().trim().length() > 0)
                    this.prop.set(Integer.parseInt(this.textFieldValue.getText().trim()));
                else
                    this.prop.setToDefault();
        }
        
        @Override
        public void updateCursorCounter()
        {
            this.textFieldValue.updateCursorCounter();
        }
        
        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {
            this.textFieldValue.mouseClicked(x, y, mouseEvent);
        }
    }
    
    public interface IGuiConfigListEntry extends GuiListExtended.IGuiListEntry
    {
        public void keyTyped(char eventChar, int eventKey);
        
        public void updateCursorCounter();
        
        public void mouseClicked(int x, int y, int mouseEvent);
    }
}