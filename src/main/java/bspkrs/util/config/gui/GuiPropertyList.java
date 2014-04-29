package bspkrs.util.config.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import bspkrs.util.CommonUtils;
import bspkrs.util.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPropertyList extends GuiListExtended
{
    private final GuiConfig             parentGuiConfig;
    private final Minecraft             mc;
    private final IGuiConfigListEntry[] listEntries;
    private int                         maxLabelTextWidth = 0;
    private final int                   labelX;
    private final int                   controlX;
    private final int                   controlWidth;
    private final int                   resetX;
    private final int                   scrollBarX;
    
    public GuiPropertyList(GuiConfig parent, Minecraft mc)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);
        this.parentGuiConfig = parent;
        this.setShowSelectionBox(false);
        this.mc = mc;
        this.listEntries = new IGuiConfigListEntry[parent.properties.length];
        int i = 0;
        String s = null;
        
        for (IConfigProperty prop : parent.properties)
        {
            if (prop != null)
            {
                if (prop.isProperty()) // as opposed to being a child category entry
                {
                    int l = mc.fontRenderer.getStringWidth(I18n.format(prop.getLanguageKey(), new Object[0]));
                    
                    if (l > this.maxLabelTextWidth)
                    {
                        this.maxLabelTextWidth = l;
                    }
                }
                
                if (prop.getType().equals(ConfigGuiType.BOOLEAN))
                    this.listEntries[i++] = new GuiPropertyList.BooleanPropEntry(prop);
                else if (prop.getType().equals(ConfigGuiType.INTEGER))
                    this.listEntries[i++] = new GuiPropertyList.IntegerPropEntry(prop);
                else if (prop.getType().equals(ConfigGuiType.DOUBLE))
                    this.listEntries[i++] = new GuiPropertyList.DoublePropEntry(prop);
                else if (prop.getType().equals(ConfigGuiType.COLOR))
                {
                    if (prop.getValidValues().length > 0)
                        this.listEntries[i++] = new GuiPropertyList.ColorPropEntry(prop);
                    else
                        this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.BLOCK_LIST))
                {
                    // TODO:
                    this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.ITEMSTACK_LIST))
                {
                    // TODO:
                    this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.ENTITY_LIST))
                {
                    // TODO:
                    this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.BIOME_LIST))
                {
                    // TODO:
                    this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.DIMENSION_LIST))
                {
                    // TODO:
                    this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.STRING))
                {
                    if (prop.getValidValues().length > 0)
                        this.listEntries[i++] = new GuiPropertyList.SelectStringPropEntry(prop);
                    else
                        this.listEntries[i++] = new GuiPropertyList.StringPropEntry(prop);
                }
                else if (prop.getType().equals(ConfigGuiType.CONFIG_CATEGORY))
                    this.listEntries[i++] = new GuiConfigCategoryListEntry(prop);
            }
        }
        
        int viewWidth = this.maxLabelTextWidth + 8 + (width / 2);
        labelX = (this.width / 2) - (viewWidth / 2);
        controlX = labelX + maxLabelTextWidth + 8;
        resetX = (this.width / 2) + (viewWidth / 2) - 45;
        controlWidth = resetX - controlX - 5;
        scrollBarX = resetX + 45;
        
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
        return scrollBarX;
    }
    
    /**
     * Gets the width of the list
     */
    @Override
    public int getListWidth()
    {
        return parentGuiConfig.width;
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
    
    protected void saveProperties()
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].saveProperty();
    }
    
    protected boolean areAllPropsDefault()
    {
        for (int i = 0; i < this.getSize(); i++)
            if (!listEntries[i].isDefault())
                return false;
        
        return true;
    }
    
    protected void setAllPropsDefault()
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].setToDefault();
    }
    
    protected void drawScreenPost(int mouseX, int mouseY, float f)
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].drawToolTip(mouseX, mouseY);
    }
    
    /**
     * IGuiListEntry Inner Classes
     */
    
    /**
     * BooleanPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class BooleanPropEntry extends ButtonPropEntry
    {
        
        private BooleanPropEntry(IConfigProperty prop)
        {
            super(prop);
        }
        
        @Override
        public void updateValueButtonText()
        {
            String trans = I18n.format(String.valueOf(prop.getString()));
            if (!trans.equals(prop.getString()))
                this.btnValue.displayString = trans;
            else
                this.btnValue.displayString = this.prop.getString();
            btnValue.packedFGColour = prop.getBoolean() ? CommonUtils.getColorCode('2', true) : CommonUtils.getColorCode('4', true);
        }
        
        @Override
        public void valueButtonPressed()
        {
            prop.set(!prop.getBoolean());
        }
    }
    
    /**
     * SelectStringPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class SelectStringPropEntry extends ButtonPropEntry
    {
        private int index;
        
        private SelectStringPropEntry(IConfigProperty prop)
        {
            super(prop);
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
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY))
            {
                resetIndex();
                return true;
            }
            
            return false;
        }
        
        @Override
        public void updateValueButtonText()
        {
            String trans = I18n.format(String.valueOf(prop.getString()));
            if (!trans.equals(prop.getString()))
                this.btnValue.displayString = trans;
            else
                this.btnValue.displayString = this.prop.getString();
        }
        
        @Override
        public void valueButtonPressed()
        {
            if (++this.index >= prop.getValidValues().length)
                this.index = 0;
            
            prop.set(prop.getValidValues()[this.index]);
        }
    }
    
    /**
     * ColorPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class ColorPropEntry extends SelectStringPropEntry
    {
        ColorPropEntry(IConfigProperty prop)
        {
            super(prop);
        }
        
        @Override
        public void drawEntry(int p_148279_1_, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean p_148279_9_)
        {
            this.btnValue.packedFGColour = CommonUtils.getColorCode(this.prop.getString().charAt(0), true);
            super.drawEntry(p_148279_1_, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, p_148279_9_);
        }
        
        @Override
        public void updateValueButtonText()
        {
            String trans = I18n.format(String.valueOf(prop.getString()));
            if (!trans.equals(prop.getString()))
                this.btnValue.displayString = trans + " - " + I18n.format("bspkrs.configgui.sampletext");
            else
                this.btnValue.displayString = this.prop.getString() + " - " + I18n.format("bspkrs.configgui.sampletext");
        }
    }
    
    /**
     * ColorPropEntry
     */
    @SideOnly(Side.CLIENT)
    public abstract class ButtonPropEntry extends GuiConfigListEntry
    {
        protected final GuiButton btnValue;
        
        private ButtonPropEntry(IConfigProperty prop)
        {
            super(prop);
            int listWidth = GuiPropertyList.this.getListWidth();
            this.btnValue = new GuiButton(0, 0, 0, 160, 18, I18n.format(prop.getString(), new Object[0]));
        }
        
        public abstract void updateValueButtonText();
        
        public abstract void valueButtonPressed();
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            try
            {
                ReflectionHelper.setIntValue(GuiButton.class, "field_146120_f", "width", this.btnValue, GuiPropertyList.this.controlWidth);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.btnValue.xPosition = GuiPropertyList.this.controlX;
            this.btnValue.yPosition = y;
            updateValueButtonText();
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
                valueButtonPressed();
                return true;
            }
            else
                return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }
        
        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
            this.btnValue.mouseReleased(x, y);
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
        
        @Override
        public void saveProperty()
        {}
    }
    
    /**
     * IntegerPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class IntegerPropEntry extends StringPropEntry
    {
        private IntegerPropEntry(IConfigProperty prop)
        {
            super(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            String validChars = "0123456789";
            String before = this.textFieldValue.getText();
            if (validChars.contains(String.valueOf(eventChar))
                    || (!before.startsWith("-") && this.textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                    || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
                    || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
            
            if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-"))
            {
                try
                {
                    long value = Long.parseLong(textFieldValue.getText().trim());
                    if (value < prop.getMinIntValue() || value > prop.getMaxIntValue())
                        this.isValidValue = false;
                    else
                        this.isValidValue = true;
                }
                catch (Throwable e)
                {
                    this.isValidValue = false;
                }
            }
            else
                this.isValidValue = false;
        }
        
        @Override
        public void saveProperty()
        {
            try
            {
                int value = Integer.parseInt(textFieldValue.getText().trim());
                if (value >= prop.getMinIntValue() && value <= prop.getMaxIntValue())
                    this.prop.set(value);
                else
                    this.prop.setToDefault();
            }
            catch (Throwable e)
            {
                this.prop.setToDefault();
            }
            
        }
    }
    
    /**
     * DoublePropEntry
     */
    @SideOnly(Side.CLIENT)
    public class DoublePropEntry extends StringPropEntry
    {
        private DoublePropEntry(IConfigProperty prop)
        {
            super(prop);
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            String validChars = "0123456789";
            String before = this.textFieldValue.getText();
            if (validChars.contains(String.valueOf(eventChar)) ||
                    (!before.startsWith("-") && this.textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                    || (!before.contains(".") && eventChar == '.')
                    || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
                    || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
            
            if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-"))
            {
                try
                {
                    double value = Double.parseDouble(textFieldValue.getText().trim());
                    if (value < prop.getMinDoubleValue() || value > prop.getMaxDoubleValue())
                        this.isValidValue = false;
                    else
                        this.isValidValue = true;
                }
                catch (Throwable e)
                {
                    this.isValidValue = false;
                }
            }
            else
                this.isValidValue = false;
        }
        
        @Override
        public void saveProperty()
        {
            try
            {
                double value = Double.parseDouble(textFieldValue.getText().trim());
                if (value >= prop.getMinDoubleValue() && value <= prop.getMaxDoubleValue())
                    this.prop.set(value);
                else
                    this.prop.setToDefault();
                
            }
            catch (Throwable e)
            {
                this.prop.setToDefault();
            }
            
        }
    }
    
    /**
     * StringPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class StringPropEntry extends GuiConfigListEntry
    {
        protected final GuiTextField textFieldValue;
        
        private StringPropEntry(IConfigProperty prop)
        {
            super(prop);
            this.textFieldValue = new GuiTextField(GuiPropertyList.this.mc.fontRenderer, 0, 0, 200 - 4, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(prop.getString());
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            try
            {
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146209_f", "xPosition", this.textFieldValue, -1) != GuiPropertyList.this.controlX + 1)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146209_f", "xPosition", this.textFieldValue, GuiPropertyList.this.controlX + 1);
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, -1) != y + 1)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, y + 1);
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146218_h", "width", this.textFieldValue, -1) != GuiPropertyList.this.controlWidth - 3)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146218_h", "width", this.textFieldValue, GuiPropertyList.this.controlWidth - 3);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.textFieldValue.drawTextBox();
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
            
            if (prop.getValidStringPattern() != null)
            {
                if (prop.getValidStringPattern().matcher(this.textFieldValue.getText().trim()).matches())
                    isValidValue = true;
                else
                    isValidValue = false;
            }
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
        
        @Override
        public boolean isDefault()
        {
            return this.prop.getDefault().trim().equals(this.textFieldValue.getText().trim());
        }
        
        @Override
        public void setToDefault()
        {
            this.textFieldValue.setText(this.prop.getDefault());
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
        }
        
        @Override
        public void saveProperty()
        {
            if (this.textFieldValue.getText().trim().length() > 0)
                this.prop.set(this.textFieldValue.getText().trim());
            else
                this.prop.setToDefault();
        }
    }
    
    /**
     * GuiConfigListEntry
     */
    @SideOnly(Side.CLIENT)
    public abstract class GuiConfigListEntry implements IGuiConfigListEntry
    {
        protected final IConfigProperty prop;
        protected final String          propName;
        protected final GuiButton       btnDefault;
        private long                    hoverStart   = -1;
        private List                    toolTip;
        protected boolean               isValidValue = true;
        private HoverChecker            tooltipHoverChecker;
        
        public GuiConfigListEntry(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey(), new Object[0]);
            this.btnDefault = new GuiButton(0, 0, 0, 40, 18, I18n.format("controls.reset", new Object[0]));
            
            String comment;
            
            if (prop.getType().equals(int.class))
                comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                        "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinIntValue(), prop.getMaxIntValue());
            else
                comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                        "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinDoubleValue(), prop.getMaxDoubleValue());
            
            if (!comment.equals(prop.getLanguageKey() + ".tooltip"))
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
            else if (prop.getComment() != null && !prop.getComment().trim().isEmpty())
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
            else
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            GuiPropertyList.this.mc.fontRenderer.drawString(
                    isValidValue ? this.propName : EnumChatFormatting.RED + this.propName,
                    GuiPropertyList.this.labelX,
                    y + slotHeight / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2,
                    16777215);
            this.btnDefault.xPosition = GuiPropertyList.this.resetX;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !isDefault();
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            
            if (this.tooltipHoverChecker == null)
                this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, GuiPropertyList.this.controlX - 8, 800);
            else
                this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, GuiPropertyList.this.controlX - 8);
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            if (toolTip != null && this.tooltipHoverChecker != null)
            {
                boolean canHover = mouseY < GuiPropertyList.this.bottom && mouseY > GuiPropertyList.this.top;
                if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
                    GuiPropertyList.this.parentGuiConfig.drawToolTip(toolTip, mouseX, mouseY);
            }
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                setToDefault();
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnDefault.mouseReleased(x, y);
        }
        
        @Override
        public boolean isDefault()
        {
            return prop.isDefault();
        }
        
        @Override
        public void setToDefault()
        {
            this.prop.setToDefault();
        }
        
        @Override
        public abstract void keyTyped(char eventChar, int eventKey);
        
        @Override
        public abstract void updateCursorCounter();
        
        @Override
        public abstract void mouseClicked(int x, int y, int mouseEvent);
        
        @Override
        public abstract void saveProperty();
        
    }
    
    /**
     * GuiConfigListEntry
     */
    @SideOnly(Side.CLIENT)
    public class GuiConfigCategoryListEntry implements IGuiConfigListEntry
    {
        protected final IConfigProperty prop;
        protected final String          propName;
        protected final GuiButton       btnSelectCategory;
        private long                    hoverStart = -1;
        private List                    toolTip;
        private HoverChecker            tooltipHoverChecker;
        
        public GuiConfigCategoryListEntry(IConfigProperty prop)
        {
            this.prop = prop;
            
            if (I18n.format(prop.getLanguageKey()).equals(prop.getLanguageKey()))
                this.propName = I18n.format(prop.getName());
            else
                this.propName = I18n.format(prop.getLanguageKey());
            
            this.btnSelectCategory = new GuiButton(0, 0, 0, 300, 18, I18n.format(propName, new Object[0]));
            
            String comment = I18n.format(prop.getLanguageKey() + ".tooltip");
            
            if (!comment.equals(prop.getLanguageKey() + ".tooltip"))
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
            else if (prop.getComment() != null && !prop.getComment().trim().isEmpty())
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
            else
                toolTip = GuiPropertyList.this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            this.btnSelectCategory.xPosition = listWidth / 2 - 150;
            this.btnSelectCategory.yPosition = y;
            this.btnSelectCategory.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            
            if (this.tooltipHoverChecker == null)
                this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, listWidth / 2 - 150, listWidth / 2 + 150, 800);
            else
                this.tooltipHoverChecker.updateBounds(y, y + slotHeight, listWidth / 2 - 150, listWidth / 2 + 150);
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            if (toolTip != null && this.tooltipHoverChecker != null)
            {
                boolean canHover = mouseY < GuiPropertyList.this.bottom && mouseY > GuiPropertyList.this.top;
                if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
                    GuiPropertyList.this.parentGuiConfig.drawToolTip(toolTip, mouseX, mouseY);
            }
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnSelectCategory.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                GuiConfig subCatGui = new GuiConfig(GuiPropertyList.this.parentGuiConfig, prop.getConfigProperties(),
                        null, GuiPropertyList.this.parentGuiConfig.configObject, null, null, prop.getQualifiedName());
                Minecraft.getMinecraft().displayGuiScreen(subCatGui);
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnSelectCategory.mouseReleased(x, y);
        }
        
        @Override
        public boolean isDefault()
        {
            return prop.isDefault();
        }
        
        @Override
        public void setToDefault()
        {}
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {}
        
        @Override
        public void updateCursorCounter()
        {}
        
        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {}
        
        @Override
        public void saveProperty()
        {}
        
    }
    
    @SideOnly(Side.CLIENT)
    public interface IGuiConfigListEntry extends GuiListExtended.IGuiListEntry
    {
        public void keyTyped(char eventChar, int eventKey);
        
        public void updateCursorCounter();
        
        public void mouseClicked(int x, int y, int mouseEvent);
        
        public boolean isDefault();
        
        public void setToDefault();
        
        public void saveProperty();
        
        public void drawToolTip(int mouseX, int mouseY);
    }
}