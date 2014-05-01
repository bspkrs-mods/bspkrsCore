package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import bspkrs.client.util.HUDUtils;
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
    private int                         labelX;
    private int                         controlX;
    private int                         controlWidth;
    private int                         resetX;
    private int                         scrollBarX;
    
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
            }
        }
        
        int viewWidth = this.maxLabelTextWidth + 8 + (width / 2);
        labelX = (this.width / 2) - (viewWidth / 2);
        controlX = labelX + maxLabelTextWidth + 8;
        resetX = (this.width / 2) + (viewWidth / 2) - 45;
        controlWidth = resetX - controlX - 5;
        scrollBarX = resetX + 45;
        
        for (IConfigProperty prop : parent.properties)
        {
            if (prop != null)
            {
                if (prop.isList())
                    this.listEntries[i++] = new GuiPropertyList.EditListPropEntry(prop);
                else if (prop.getType().equals(ConfigGuiType.BOOLEAN))
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
    }
    
    protected void initGui()
    {
        this.width = parentGuiConfig.width;
        try
        {
            ReflectionHelper.setIntValue(GuiSlot.class, "field_148158_l", "height", this, parentGuiConfig.height);
        }
        catch (Throwable e)
        {}
        this.top = parentGuiConfig.titleLine2 != null ? 33 : 23;
        this.bottom = parentGuiConfig.height - 32;
        this.left = 0;
        this.right = width;
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
    
    protected void updateScreen()
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].updateCursorCounter();
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
    
    protected boolean areAnyPropsChanged()
    {
        for (int i = 0; i < this.getSize(); i++)
            if (listEntries[i].isChanged())
                return true;
        
        return false;
    }
    
    protected void undoAllChanges()
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i].undoChanges();
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
        private final boolean beforeValue;
        private boolean       currentValue;
        
        private BooleanPropEntry(IConfigProperty prop)
        {
            super(prop);
            this.beforeValue = prop.getBoolean();
            this.currentValue = beforeValue;
            updateValueButtonText();
        }
        
        @Override
        public void updateValueButtonText()
        {
            this.btnValue.displayString = I18n.format(String.valueOf(currentValue));
            btnValue.packedFGColour = currentValue ? HUDUtils.getColorCode('2', true) : HUDUtils.getColorCode('4', true);
        }
        
        @Override
        public void valueButtonPressed(int slotIndex)
        {
            currentValue = !currentValue;
        }
        
        @Override
        public boolean isDefault()
        {
            return currentValue == Boolean.valueOf(prop.getDefault());
        }
        
        @Override
        public void setToDefault()
        {
            currentValue = Boolean.valueOf(prop.getDefault());
            updateValueButtonText();
        }
        
        @Override
        public boolean isChanged()
        {
            return currentValue != beforeValue;
        }
        
        @Override
        public void undoChanges()
        {
            currentValue = beforeValue;
            updateValueButtonText();
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged())
                prop.set(currentValue);
        }
    }
    
    /**
     * SelectStringPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class SelectStringPropEntry extends ButtonPropEntry
    {
        private final int beforeIndex;
        private final int defaultIndex;
        protected int     currentIndex;
        
        private SelectStringPropEntry(IConfigProperty prop)
        {
            super(prop);
            beforeIndex = getIndex(prop.getString());
            defaultIndex = getIndex(prop.getDefault());
            currentIndex = beforeIndex;
            updateValueButtonText();
        }
        
        private int getIndex(String s)
        {
            for (int i = 0; i < prop.getValidValues().length; i++)
                if (prop.getValidValues()[i].equalsIgnoreCase(s))
                {
                    return i;
                }
            
            return 0;
        }
        
        @Override
        public void updateValueButtonText()
        {
            this.btnValue.displayString = I18n.format(prop.getValidValues()[currentIndex]);
        }
        
        @Override
        public void valueButtonPressed(int slotIndex)
        {
            if (++this.currentIndex >= prop.getValidValues().length)
                this.currentIndex = 0;
            
            updateValueButtonText();
        }
        
        @Override
        public boolean isDefault()
        {
            return currentIndex == defaultIndex;
        }
        
        @Override
        public void setToDefault()
        {
            currentIndex = defaultIndex;
            updateValueButtonText();
        }
        
        @Override
        public boolean isChanged()
        {
            return currentIndex != beforeIndex;
        }
        
        @Override
        public void undoChanges()
        {
            currentIndex = beforeIndex;
            updateValueButtonText();
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged())
                prop.set(prop.getValidValues()[currentIndex]);
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
            updateValueButtonText();
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            this.btnValue.packedFGColour = HUDUtils.getColorCode(this.prop.getValidValues()[currentIndex].charAt(0), true);
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
        }
        
        @Override
        public void updateValueButtonText()
        {
            this.btnValue.displayString = I18n.format(prop.getValidValues()[currentIndex]) + " - " + I18n.format("bspkrs.configgui.sampletext");
        }
    }
    
    /**
     * EditListPropEntry
     */
    public class EditListPropEntry extends ButtonPropEntry
    {
        private final String[] beforeValues;
        private String[]       currentValues;
        
        public EditListPropEntry(IConfigProperty prop)
        {
            super(prop);
            beforeValues = prop.getStringList();
            currentValues = prop.getStringList();
            updateValueButtonText();
        }
        
        @Override
        public void updateValueButtonText()
        {
            String buttonText = "";
            for (String s : currentValues)
                buttonText += ", [" + s + "]";
            
            buttonText = buttonText.replaceFirst(", ", "");
            
            if (mc.fontRenderer.getStringWidth(buttonText) > controlWidth - 6)
                this.btnValue.displayString =
                        mc.fontRenderer.trimStringToWidth(buttonText, controlWidth - 6 - mc.fontRenderer.getStringWidth("...")).trim() + "...";
            else
                this.btnValue.displayString = buttonText;
        }
        
        @Override
        public void valueButtonPressed(int slotIndex)
        {
            mc.displayGuiScreen(new GuiEditList(GuiPropertyList.this.parentGuiConfig, prop, slotIndex, currentValues));
        }
        
        public void setListFromChildScreen(String[] newList)
        {
            if (!Arrays.deepEquals(currentValues, newList))
            {
                currentValues = newList;
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isDefault()
        {
            return Arrays.deepEquals(prop.getDefaults(), currentValues);
        }
        
        @Override
        public void setToDefault()
        {
            this.currentValues = prop.getDefaults();
            updateValueButtonText();
        }
        
        @Override
        public boolean isChanged()
        {
            return !Arrays.deepEquals(beforeValues, currentValues);
        }
        
        @Override
        public void undoChanges()
        {
            currentValues = beforeValues;
            updateValueButtonText();
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged())
                this.prop.set(currentValues);
        }
    }
    
    /**
     * ButtonPropEntry
     */
    @SideOnly(Side.CLIENT)
    public abstract class ButtonPropEntry extends GuiConfigListEntry
    {
        protected final GuiButton btnValue;
        
        private ButtonPropEntry(IConfigProperty prop)
        {
            super(prop);
            this.btnValue = new GuiButton(0, controlX, 0, controlWidth, 18, I18n.format(prop.getString(), new Object[0]));
        }
        
        public abstract void updateValueButtonText();
        
        public abstract void valueButtonPressed(int slotIndex);
        
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
                btnValue.func_146113_a(mc.getSoundHandler());
                valueButtonPressed(index);
                updateValueButtonText();
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
    }
    
    /**
     * IntegerPropEntry
     */
    @SideOnly(Side.CLIENT)
    public class IntegerPropEntry extends StringPropEntry
    {
        private final int beforeValue;
        
        private IntegerPropEntry(IConfigProperty prop)
        {
            super(prop);
            this.beforeValue = prop.getInt();
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
        public boolean isChanged()
        {
            try
            {
                return this.beforeValue != Integer.parseInt(textFieldValue.getText().trim());
            }
            catch (Throwable e)
            {
                return true;
            }
        }
        
        @Override
        public void undoChanges()
        {
            this.textFieldValue.setText(String.valueOf(beforeValue));
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged() && this.isValidValue)
                try
                {
                    int value = Integer.parseInt(textFieldValue.getText().trim());
                    this.prop.set(value);
                    
                }
                catch (Throwable e)
                {
                    this.prop.setToDefault();
                }
            else if (isChanged() && !this.isValidValue)
                try
                {
                    int value = Integer.parseInt(textFieldValue.getText().trim());
                    if (value < prop.getMinIntValue())
                        this.prop.set(prop.getMinIntValue());
                    else
                        this.prop.set(prop.getMaxIntValue());
                    
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
        private final double beforeValue;
        
        private DoublePropEntry(IConfigProperty prop)
        {
            super(prop);
            this.beforeValue = prop.getDouble();
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
        public boolean isChanged()
        {
            try
            {
                return this.beforeValue != Double.parseDouble(textFieldValue.getText().trim());
            }
            catch (Throwable e)
            {
                return true;
            }
        }
        
        @Override
        public void undoChanges()
        {
            this.textFieldValue.setText(String.valueOf(beforeValue));
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged() && this.isValidValue)
                try
                {
                    double value = Double.parseDouble(textFieldValue.getText().trim());
                    this.prop.set(value);
                    
                }
                catch (Throwable e)
                {
                    this.prop.setToDefault();
                }
            else if (isChanged() && !this.isValidValue)
                try
                {
                    double value = Double.parseDouble(textFieldValue.getText().trim());
                    if (value < prop.getMinDoubleValue())
                        this.prop.set(prop.getMinDoubleValue());
                    else
                        this.prop.set(prop.getMaxDoubleValue());
                    
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
        private final String         beforeValue;
        
        private StringPropEntry(IConfigProperty prop)
        {
            super(prop);
            beforeValue = prop.getString();
            this.textFieldValue = new GuiTextField(GuiPropertyList.this.mc.fontRenderer, controlX + 1, 0, controlWidth - 3, 16);
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
            return this.prop.getDefault().equals(this.textFieldValue.getText());
        }
        
        @Override
        public void setToDefault()
        {
            this.textFieldValue.setText(this.prop.getDefault());
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_HOME);
        }
        
        @Override
        public boolean isChanged()
        {
            return !this.beforeValue.equals(textFieldValue.getText());
        }
        
        @Override
        public void undoChanges()
        {
            this.textFieldValue.setText(beforeValue);
        }
        
        @Override
        public void saveProperty()
        {
            if (isChanged() && this.isValidValue)
                this.prop.set(this.textFieldValue.getText());
            else if (isChanged() && !this.isValidValue)
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
        protected final GuiButton       btnUndoChanges;
        protected final GuiButton       btnDefault;
        private long                    hoverStart   = -1;
        private List                    toolTip;
        private List                    undoToolTip;
        private List                    defaultToolTip;
        protected boolean               isValidValue = true;
        private HoverChecker            tooltipHoverChecker;
        private HoverChecker            undoHoverChecker;
        private HoverChecker            defaultHoverChecker;
        
        public GuiConfigListEntry(IConfigProperty prop)
        {
            this.prop = prop;
            this.propName = I18n.format(prop.getLanguageKey());
            this.btnUndoChanges = new GuiButton(0, 0, 0, 18, 18, "↩");
            this.btnDefault = new GuiButton(0, 0, 0, 18, 18, "☄");
            
            this.undoHoverChecker = new HoverChecker(this.btnUndoChanges, 800);
            this.defaultHoverChecker = new HoverChecker(this.btnDefault, 800);
            this.undoToolTip = new ArrayList();
            this.undoToolTip.add(I18n.format("bspkrs.configgui.tooltip.undoChanges"));
            this.defaultToolTip = new ArrayList();
            this.defaultToolTip.add(I18n.format("bspkrs.configgui.tooltip.resetToDefault"));
            
            String comment;
            
            if (prop.getType().equals(ConfigGuiType.INTEGER))
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
            boolean isChanged = isChanged();
            String label = (!isValidValue ? EnumChatFormatting.RED.toString() :
                    (isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString()))
                    + (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + this.propName;
            GuiPropertyList.this.mc.fontRenderer.drawString(
                    label,
                    GuiPropertyList.this.labelX,
                    y + slotHeight / 2 - GuiPropertyList.this.mc.fontRenderer.FONT_HEIGHT / 2,
                    16777215);
            
            this.btnUndoChanges.xPosition = GuiPropertyList.this.resetX;
            this.btnUndoChanges.yPosition = y;
            this.btnUndoChanges.enabled = isChanged;
            this.btnUndoChanges.displayString = "↩";
            this.btnUndoChanges.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            
            this.btnDefault.xPosition = GuiPropertyList.this.resetX + 22;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = !isDefault();
            this.btnDefault.displayString = "☄";
            this.btnDefault.drawButton(GuiPropertyList.this.mc, mouseX, mouseY);
            
            if (this.tooltipHoverChecker == null)
                this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, GuiPropertyList.this.controlX - 8, 800);
            else
                this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, GuiPropertyList.this.controlX - 8);
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            boolean canHover = mouseY < GuiPropertyList.this.bottom && mouseY > GuiPropertyList.this.top;
            if (toolTip != null && this.tooltipHoverChecker != null)
            {
                if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
                    GuiPropertyList.this.parentGuiConfig.drawToolTip(toolTip, mouseX, mouseY);
            }
            
            if (this.undoHoverChecker.checkHover(mouseX, mouseY, canHover))
                GuiPropertyList.this.parentGuiConfig.drawToolTip(undoToolTip, mouseX, mouseY);
            
            if (this.defaultHoverChecker.checkHover(mouseX, mouseY, canHover))
                GuiPropertyList.this.parentGuiConfig.drawToolTip(defaultToolTip, mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnDefault.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                btnDefault.func_146113_a(mc.getSoundHandler());
                setToDefault();
                return true;
            }
            else if (this.btnUndoChanges.mousePressed(GuiPropertyList.this.mc, x, y))
            {
                btnUndoChanges.func_146113_a(mc.getSoundHandler());
                undoChanges();
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
        public abstract boolean isDefault();
        
        @Override
        public abstract void setToDefault();
        
        @Override
        public abstract void keyTyped(char eventChar, int eventKey);
        
        @Override
        public abstract void updateCursorCounter();
        
        @Override
        public abstract void mouseClicked(int x, int y, int mouseEvent);
        
        @Override
        public abstract boolean isChanged();
        
        @Override
        public abstract void undoChanges();
        
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
                btnSelectCategory.func_146113_a(mc.getSoundHandler());
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
        
        @Override
        public boolean isChanged()
        {
            return false;
        }
        
        @Override
        public void undoChanges()
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
        
        public void undoChanges();
        
        public boolean isChanged();
        
        public void saveProperty();
        
        public void drawToolTip(int mouseX, int mouseY);
    }
}