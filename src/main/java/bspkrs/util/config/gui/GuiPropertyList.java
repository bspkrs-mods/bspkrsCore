package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPropertyList extends GuiListExtended
{
    public final GuiConfig           parentGuiConfig;
    public final Minecraft           mc;
    public List<IGuiConfigListEntry> listEntries;
    /**
     * The max width of the label of all IGuiConfigListEntry objects.
     */
    public int                       maxLabelTextWidth  = 0;
    /**
     * The max x boundary of all IGuiConfigListEntry objects.
     */
    public int                       maxEntryRightBound = 0;
    /**
     * The x position where the label should be drawn.
     */
    public int                       labelX;
    /**
     * The x position where the control should be drawn.
     */
    public int                       controlX;
    /**
     * The width of the control.
     */
    public int                       controlWidth;
    /**
     * The minimum x position where the Undo/Default buttons will start
     */
    public int                       resetX;
    /**
     * The x position of the scroll bar.
     */
    public int                       scrollBarX;
    
    public GuiPropertyList(GuiConfig parent, Minecraft mc)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);
        this.parentGuiConfig = parent;
        this.setShowSelectionBox(false);
        this.mc = mc;
        this.listEntries = new ArrayList<IGuiConfigListEntry>();
        int i = 0;
        String s = null;
        
        for (IConfigProperty prop : parent.properties)
        {
            if (prop != null)
            {
                if (prop.isProperty()) // as opposed to being a child category entry
                {
                    int l = mc.fontRenderer.getStringWidth(I18n.format(prop.getLanguageKey()));
                    
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
        scrollBarX = this.width;
        
        for (IConfigProperty prop : parent.properties)
        {
            if (prop != null)
            {
                if (prop.hasCustomIGuiConfigListEntry())
                    try
                    {
                        this.listEntries.add(prop.getCustomIGuiConfigListEntryClass()
                                .getConstructor(GuiConfig.class, GuiPropertyList.class, IConfigProperty.class)
                                .newInstance(this.parentGuiConfig, this, prop));
                    }
                    catch (Throwable e)
                    {
                        FMLLog.severe("There was a critical error instantiating the custom IGuiConfigListEntry for property %s.", prop.getName());
                        e.printStackTrace();
                    }
                else if (prop.isProperty())
                {
                    if (prop.isList())
                        this.listEntries.add(new GuiPropertyList.EditListPropEntry(this.parentGuiConfig, this, prop));
                    else if (prop.getType().equals(ConfigGuiType.BOOLEAN))
                        this.listEntries.add(new GuiPropertyList.BooleanPropEntry(this.parentGuiConfig, this, prop));
                    else if (prop.getType().equals(ConfigGuiType.INTEGER))
                        this.listEntries.add(new GuiPropertyList.IntegerPropEntry(this.parentGuiConfig, this, prop));
                    else if (prop.getType().equals(ConfigGuiType.DOUBLE))
                        this.listEntries.add(new GuiPropertyList.DoublePropEntry(this.parentGuiConfig, this, prop));
                    else if (prop.getType().equals(ConfigGuiType.COLOR))
                    {
                        if (prop.getValidValues() != null && prop.getValidValues().length > 0)
                            this.listEntries.add(new GuiPropertyList.ColorPropEntry(this.parentGuiConfig, this, prop));
                        else
                            this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.BLOCK_LIST))
                    {
                        // TODO:
                        this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.ITEMSTACK_LIST))
                    {
                        // TODO:
                        this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.ENTITY_LIST))
                    {
                        // TODO:
                        this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.BIOME_LIST))
                    {
                        // TODO:
                        this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.DIMENSION_LIST))
                    {
                        // TODO:
                        this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                    else if (prop.getType().equals(ConfigGuiType.MOD_ID))
                    {
                        Map<String, String> values = new TreeMap<String, String>();
                        for (ModContainer mod : Loader.instance().getActiveModList())
                            values.put(mod.getModId(), mod.getName());
                        this.listEntries.add(new SelectValuePropEntry(this.parentGuiConfig, this, prop, values));
                    }
                    else if (prop.getType().equals(ConfigGuiType.STRING))
                    {
                        if (prop.getValidValues() != null && prop.getValidValues().length > 0)
                            this.listEntries.add(new GuiPropertyList.SelectStringPropEntry(this.parentGuiConfig, this, prop));
                        else
                            this.listEntries.add(new GuiPropertyList.StringPropEntry(this.parentGuiConfig, this, prop));
                    }
                }
                else if (prop.getType().equals(ConfigGuiType.CONFIG_CATEGORY))
                    this.listEntries.add(new GuiConfigCategoryListEntry(this.parentGuiConfig, this, prop));
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
        
        this.maxLabelTextWidth = 0;
        for (IGuiConfigListEntry entry : this.listEntries)
            if (entry.getLabelWidth() > this.maxLabelTextWidth)
                this.maxLabelTextWidth = entry.getLabelWidth();
        
        this.top = parentGuiConfig.titleLine2 != null ? 33 : 23;
        this.bottom = parentGuiConfig.height - 32;
        this.left = 0;
        this.right = width;
        int viewWidth = this.maxLabelTextWidth + 8 + (width / 2);
        labelX = (this.width / 2) - (viewWidth / 2);
        controlX = labelX + maxLabelTextWidth + 8;
        resetX = (this.width / 2) + (viewWidth / 2) - 45;
        
        this.maxEntryRightBound = 0;
        for (IGuiConfigListEntry entry : this.listEntries)
            if (entry.getEntryRightBound() > this.maxEntryRightBound)
                this.maxEntryRightBound = entry.getEntryRightBound();
        
        scrollBarX = this.maxEntryRightBound + 5;
        controlWidth = maxEntryRightBound - controlX - 45;
    }
    
    @Override
    public int getSize()
    {
        return this.listEntries.size();
    }
    
    /**
     * Gets the IGuiListEntry object for the given index
     */
    @Override
    public IGuiConfigListEntry getListEntry(int index)
    {
        return this.listEntries.get(index);
    }
    
    @Override
    public int getScrollBarX()
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
    
    /**
     * This method is a pass-through for IGuiConfigListEntry objects that require keystrokes. Called from the parent GuiConfig screen.
     */
    public void keyTyped(char eventChar, int eventKey)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            entry.keyTyped(eventChar, eventKey);
    }
    
    /**
     * This method is a pass-through for IGuiConfigListEntry objects that contain GuiTextField elements. Called from the parent GuiConfig
     * screen.
     */
    public void updateScreen()
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            entry.updateCursorCounter();
    }
    
    /**
     * This method is a pass-through for IGuiConfigListEntry objects that contain GuiTextField elements. Called from the parent GuiConfig
     * screen.
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseEvent)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            entry.mouseClicked(mouseX, mouseY, mouseEvent);
    }
    
    /**
     * Saves all properties on this screen / child screens.
     */
    public void saveProperties()
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            entry.saveProperty();
    }
    
    /**
     * Returns true if all IGuiConfigListEntry objects on this screen are set to default. If includeSubCategoryProps is true sub-category
     * objects are checked as well.
     */
    public boolean areAllPropsDefault(boolean includeSubCategoryProps)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            if ((includeSubCategoryProps || !(entry instanceof GuiConfigCategoryListEntry)) && !entry.isDefault())
                return false;
        
        return true;
    }
    
    /**
     * Sets all IGuiConfigListEntry objects on this screen to default. If includeSubCategoryProps is true sub-category objects are set as
     * well.
     */
    public void setAllPropsDefault(boolean includeSubCategoryProps)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            if ((includeSubCategoryProps || !(entry instanceof GuiConfigCategoryListEntry)))
                entry.setToDefault();
    }
    
    /**
     * Returns true if any IGuiConfigListEntry objects on this screen are changed. If includeSubCategoryProps is true sub-category objects
     * are checked as well.
     */
    public boolean areAnyPropsChanged(boolean includeSubCategoryProps)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            if ((includeSubCategoryProps || !(entry instanceof GuiConfigCategoryListEntry)) && entry.isChanged())
                return true;
        
        return false;
    }
    
    /**
     * Returns true if any IGuiConfigListEntry objects on this screen are enabled. If includeSubCategoryProps is true sub-category objects
     * are checked as well.
     */
    public boolean areAnyPropsEnabled(boolean includeSubCategoryProps)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            if ((includeSubCategoryProps || !(entry instanceof GuiConfigCategoryListEntry)) && entry.enabled())
                return true;
        
        return false;
    }
    
    /**
     * Reverts changes to all IGuiConfigListEntry objects on this screen. If includeSubCategoryProps is true sub-category objects are
     * reverted as well.
     */
    public void undoAllChanges(boolean includeSubCategoryProps)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            if ((includeSubCategoryProps || !(entry instanceof GuiConfigCategoryListEntry)))
                entry.undoChanges();
    }
    
    /**
     * Calls the drawToolTip() method for all IGuiConfigListEntry objects on this screen. This is called from the parent GuiConfig screen
     * after drawing all other elements.
     */
    public void drawScreenPost(int mouseX, int mouseY, float partialTicks)
    {
        for (IGuiConfigListEntry entry : this.listEntries)
            entry.drawToolTip(mouseX, mouseY);
    }
    
    /*******************************************************************************
     * IGuiListEntry Inner Classes
     *******************************************************************************/
    
    /**
     * BooleanPropEntry
     * 
     * Provides a GuiButton that toggles between true and false.
     */
    public static class BooleanPropEntry extends ButtonPropEntry
    {
        protected final boolean beforeValue;
        protected boolean       currentValue;
        
        private BooleanPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            this.beforeValue = prop.getBoolean();
            this.currentValue = beforeValue;
            this.btnValue.enabled = enabled();
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
            if (enabled())
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
            if (enabled())
            {
                currentValue = Boolean.valueOf(prop.getDefault());
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isChanged()
        {
            return currentValue != beforeValue;
        }
        
        @Override
        public void undoChanges()
        {
            if (enabled())
            {
                currentValue = beforeValue;
                updateValueButtonText();
            }
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled() && isChanged())
                prop.set(currentValue);
        }
    }
    
    /**
     * SelectStringPropEntry
     * 
     * Provides a GuiButton that cycles through the prop's validValues array. If the current prop value is not a valid value, the first
     * entry replaces the current value.
     */
    public static class SelectStringPropEntry extends ButtonPropEntry
    {
        protected final int beforeIndex;
        protected final int defaultIndex;
        protected int       currentIndex;
        
        private SelectStringPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            beforeIndex = getIndex(prop.getString());
            defaultIndex = getIndex(prop.getDefault());
            currentIndex = beforeIndex;
            this.btnValue.enabled = enabled();
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
            if (enabled())
            {
                if (++this.currentIndex >= prop.getValidValues().length)
                    this.currentIndex = 0;
                
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isDefault()
        {
            return currentIndex == defaultIndex;
        }
        
        @Override
        public void setToDefault()
        {
            if (enabled())
            {
                currentIndex = defaultIndex;
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isChanged()
        {
            return currentIndex != beforeIndex;
        }
        
        @Override
        public void undoChanges()
        {
            if (enabled())
            {
                currentIndex = beforeIndex;
                updateValueButtonText();
            }
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled() && isChanged())
                prop.set(prop.getValidValues()[currentIndex]);
        }
    }
    
    /**
     * ColorPropEntry
     * 
     * Provides a GuiButton that cycles through the list of chat color codes.
     */
    public static class ColorPropEntry extends SelectStringPropEntry
    {
        ColorPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            this.btnValue.enabled = enabled();
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
     * SelectValuePropEntry
     * 
     * Provides a GuiButton with the current value as the displayString. Accepts a Map of selectable values with the signature <String,
     * String> where the key is the String to be selected and the value is the String that will show on the selection list. EG: a map of Mod
     * ID values where the key is the Mod ID and the value is the Mod Name.
     */
    public static class SelectValuePropEntry extends ButtonPropEntry
    {
        private final String        beforeValue;
        private String              currentValue;
        private Map<String, String> selectableValues;
        
        public SelectValuePropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop, Map<String, String> selectableValues)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            beforeValue = prop.getString();
            currentValue = prop.getString();
            this.selectableValues = selectableValues;
            updateValueButtonText();
        }
        
        @Override
        public void updateValueButtonText()
        {
            this.btnValue.displayString = currentValue;
        }
        
        @Override
        public void valueButtonPressed(int slotIndex)
        {
            mc.displayGuiScreen(new GuiSelectString(this.parentGuiConfig, prop, slotIndex, selectableValues, currentValue, enabled()));
        }
        
        public void setValueFromChildScreen(String newValue)
        {
            if (enabled() && !currentValue.equals(newValue))
            {
                currentValue = newValue;
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isDefault()
        {
            return prop.getDefault().equals(currentValue);
        }
        
        @Override
        public void setToDefault()
        {
            if (enabled())
            {
                this.currentValue = prop.getDefault();
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isChanged()
        {
            return !beforeValue.equals(currentValue);
        }
        
        @Override
        public void undoChanges()
        {
            if (enabled())
            {
                currentValue = beforeValue;
                updateValueButtonText();
            }
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled() && isChanged())
                this.prop.set(currentValue);
        }
    }
    
    /**
     * EditListPropEntry
     * 
     * Provides a GuiButton with the list contents as the displayString. Clicking the button navigates to a screen where the list can be
     * edited.
     */
    public static class EditListPropEntry extends ButtonPropEntry
    {
        private final String[] beforeValues;
        private String[]       currentValues;
        
        public EditListPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            beforeValues = prop.getStringList();
            currentValues = prop.getStringList();
            updateValueButtonText();
        }
        
        @Override
        public void updateValueButtonText()
        {
            this.btnValue.displayString = "";
            for (String s : currentValues)
                this.btnValue.displayString += ", [" + s + "]";
            
            this.btnValue.displayString = this.btnValue.displayString.replaceFirst(", ", "");
        }
        
        @Override
        public void valueButtonPressed(int slotIndex)
        {
            mc.displayGuiScreen(new GuiEditList(this.parentGuiConfig, prop, slotIndex, currentValues, enabled()));
        }
        
        public void setListFromChildScreen(String[] newList)
        {
            if (enabled() && !Arrays.deepEquals(currentValues, newList))
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
            if (enabled())
            {
                this.currentValues = prop.getDefaults();
                updateValueButtonText();
            }
        }
        
        @Override
        public boolean isChanged()
        {
            return !Arrays.deepEquals(beforeValues, currentValues);
        }
        
        @Override
        public void undoChanges()
        {
            if (enabled())
            {
                currentValues = beforeValues;
                updateValueButtonText();
            }
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled() && isChanged())
                this.prop.set(currentValues);
        }
    }
    
    /**
     * ButtonPropEntry
     * 
     * Provides a basic GuiButton entry to be used as a base for other entries that require a button for the value.
     */
    public static abstract class ButtonPropEntry extends GuiConfigListEntryBase
    {
        protected final GuiButtonExt btnValue;
        
        public ButtonPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            this.btnValue = new GuiButtonExt(0, this.parentPropertyList.controlX, 0, this.parentPropertyList.controlWidth, 18, I18n.format(prop.getString()));
        }
        
        /**
         * Updates the displayString of the value button.
         */
        public abstract void updateValueButtonText();
        
        /**
         * Called when the value button has been clicked.
         */
        public abstract void valueButtonPressed(int slotIndex);
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            try
            {
                ReflectionHelper.setIntValue(GuiButton.class, "field_146120_f", "width", this.btnValue, this.parentPropertyList.controlWidth);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            this.btnValue.xPosition = this.parentGuiConfig.propertyList.controlX;
            this.btnValue.yPosition = y;
            this.btnValue.drawButton(this.mc, mouseX, mouseY);
        }
        
        /**
         * Returns true if the mouse has been pressed on this control.
         */
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnValue.mousePressed(this.mc, x, y))
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
     * 
     * Provides a GuiTextField for user input. Input is restricted to ensure the value can be parsed using Integer.parseInteger().
     */
    public static class IntegerPropEntry extends StringPropEntry
    {
        private final int beforeValue;
        
        private IntegerPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            this.beforeValue = prop.getInt();
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
            {
                String validChars = "0123456789";
                String before = this.textFieldValue.getText();
                if (validChars.contains(String.valueOf(eventChar))
                        || (!before.startsWith("-") && this.textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                        || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
                        || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                    this.textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);
                
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
            if (enabled())
                this.textFieldValue.setText(String.valueOf(beforeValue));
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled())
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
     * 
     * Provides a GuiTextField for user input. Input is restricted to ensure the value can be parsed using Double.parseDouble().
     */
    public static class DoublePropEntry extends StringPropEntry
    {
        private final double beforeValue;
        
        private DoublePropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            this.beforeValue = prop.getDouble();
        }
        
        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
            {
                String validChars = "0123456789";
                String before = this.textFieldValue.getText();
                if (validChars.contains(String.valueOf(eventChar)) ||
                        (!before.startsWith("-") && this.textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                        || (!before.contains(".") && eventChar == '.')
                        || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
                        || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                    this.textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);
                
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
            if (enabled())
                this.textFieldValue.setText(String.valueOf(beforeValue));
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled())
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
     * 
     * Provides a GuiTextField for user input.
     */
    public static class StringPropEntry extends GuiConfigListEntryBase
    {
        protected final GuiTextField textFieldValue;
        private final String         beforeValue;
        
        private StringPropEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            beforeValue = prop.getString();
            this.textFieldValue = new GuiTextField(this.mc.fontRenderer, this.parentPropertyList.controlX + 1, 0, this.parentPropertyList.controlWidth - 3, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(prop.getString());
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            try
            {
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146209_f", "xPosition", this.textFieldValue, -1) != this.parentPropertyList.controlX + 2)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146209_f", "xPosition", this.textFieldValue, this.parentPropertyList.controlX + 2);
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, -1) != y + 1)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, y + 1);
                if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146218_h", "width", this.textFieldValue, -1) != this.parentPropertyList.controlWidth - 4)
                    ReflectionHelper.setIntValue(GuiTextField.class, "field_146218_h", "width", this.textFieldValue, this.parentPropertyList.controlWidth - 4);
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
            if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
            {
                this.textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);
                
                if (prop.getValidStringPattern() != null)
                {
                    if (prop.getValidStringPattern().matcher(this.textFieldValue.getText().trim()).matches())
                        isValidValue = true;
                    else
                        isValidValue = false;
                }
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
            if (enabled())
            {
                this.textFieldValue.setText(this.prop.getDefault());
                keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_HOME);
            }
        }
        
        @Override
        public boolean isChanged()
        {
            return !this.beforeValue.equals(textFieldValue.getText());
        }
        
        @Override
        public void undoChanges()
        {
            if (enabled())
                this.textFieldValue.setText(beforeValue);
        }
        
        @Override
        public void saveProperty()
        {
            if (enabled())
                if (isChanged() && this.isValidValue)
                    this.prop.set(this.textFieldValue.getText());
                else if (isChanged() && !this.isValidValue)
                    this.prop.setToDefault();
        }
        
        @Override
        public boolean enabled()
        {
            return parentGuiConfig.allowNonHotLoadConfigChanges || parentGuiConfig.areAllPropsHotLoadable || prop.isHotLoadable();
        }
    }
    
    /**
     * GuiConfigCategoryListEntry
     * 
     * Provides an entry that consists of a GuiButton for navigating to the child category GuiConfig screen.
     */
    public static class GuiConfigCategoryListEntry extends GuiConfigListEntryBase
    {
        protected GuiConfig          subGuiConfig;
        protected final GuiButtonExt btnSelectCategory;
        
        public GuiConfigCategoryListEntry(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            super(parentGuiConfig, parentPropertyList, prop);
            
            subGuiConfig = new GuiConfig(this.parentGuiConfig, this.prop.getConfigPropertiesList(false), this.prop.isHotLoadable(), this.parentGuiConfig.modID,
                    this.parentGuiConfig.allowNonHotLoadConfigChanges, this.parentGuiConfig.title, this.prop.getQualifiedName());
            
            this.btnSelectCategory = new GuiButtonExt(0, 0, 0, 300, 18, I18n.format(propName));
            this.tooltipHoverChecker = new HoverChecker(this.btnSelectCategory, 800);
            
            this.drawLabel = false;
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            this.btnSelectCategory.xPosition = listWidth / 2 - 150;
            this.btnSelectCategory.yPosition = y;
            this.btnSelectCategory.drawButton(this.mc, mouseX, mouseY);
            
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            boolean canHover = mouseY < this.parentGuiConfig.propertyList.bottom && mouseY > this.parentGuiConfig.propertyList.top;
            
            if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
                this.parentGuiConfig.drawToolTip(toolTip, mouseX, mouseY);
            
            super.drawToolTip(mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnSelectCategory.mousePressed(this.mc, x, y))
            {
                btnSelectCategory.func_146113_a(mc.getSoundHandler());
                Minecraft.getMinecraft().displayGuiScreen(subGuiConfig);
                return true;
            }
            else
                return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnSelectCategory.mouseReleased(x, y);
        }
        
        @Override
        public boolean isDefault()
        {
            if (this.subGuiConfig.propertyList != null)
                return this.subGuiConfig.propertyList.areAllPropsDefault(true);
            else
                return true;
        }
        
        @Override
        public void setToDefault()
        {
            this.subGuiConfig.propertyList.setAllPropsDefault(true);
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
        {
            if (this.subGuiConfig.propertyList != null)
                this.subGuiConfig.propertyList.saveProperties();
        }
        
        @Override
        public boolean isChanged()
        {
            if (this.subGuiConfig.propertyList != null)
                return this.subGuiConfig.propertyList.areAnyPropsChanged(true);
            else
                return false;
        }
        
        @Override
        public void undoChanges()
        {
            
            if (this.subGuiConfig.propertyList != null)
                this.subGuiConfig.propertyList.undoAllChanges(true);
        }
        
        @Override
        public boolean enabled()
        {
            return parentGuiConfig.allowNonHotLoadConfigChanges || parentGuiConfig.areAllPropsHotLoadable || prop.isHotLoadable();
        }
        
        @Override
        public int getLabelWidth()
        {
            return 0;
        }
        
        @Override
        public int getEntryRightBound()
        {
            return this.parentPropertyList.width / 2 + 155 + 22 + 18;
        }
    }
    
    /**
     * GuiConfigListEntryBase
     * 
     * Provides a base entry for others to extend. Handles drawing the prop label (if drawLabel == true) and the Undo/Default buttons.
     */
    public static abstract class GuiConfigListEntryBase implements IGuiConfigListEntry
    {
        protected final GuiConfig       parentGuiConfig;
        protected final GuiPropertyList parentPropertyList;
        protected final IConfigProperty prop;
        protected final Minecraft       mc;
        protected final String          propName;
        protected final GuiButtonExt    btnUndoChanges;
        protected final GuiButtonExt    btnDefault;
        protected List                  toolTip;
        protected List                  undoToolTip;
        protected List                  defaultToolTip;
        protected boolean               isValidValue = true;
        protected HoverChecker          tooltipHoverChecker;
        protected HoverChecker          undoHoverChecker;
        protected HoverChecker          defaultHoverChecker;
        protected boolean               drawLabel;
        
        public GuiConfigListEntryBase(GuiConfig parentGuiConfig, GuiPropertyList parentPropertyList, IConfigProperty prop)
        {
            this.parentGuiConfig = parentGuiConfig;
            this.parentPropertyList = parentPropertyList;
            this.prop = prop;
            this.mc = Minecraft.getMinecraft();
            String trans = I18n.format(prop.getLanguageKey());
            if (!trans.equals(prop.getLanguageKey()))
                this.propName = trans;
            else
                this.propName = prop.getName();
            this.btnUndoChanges = new GuiButtonExt(0, 0, 0, 18, 18, "↩");
            this.btnDefault = new GuiButtonExt(0, 0, 0, 18, 18, "☄");
            
            this.undoHoverChecker = new HoverChecker(this.btnUndoChanges, 800);
            this.defaultHoverChecker = new HoverChecker(this.btnDefault, 800);
            this.undoToolTip = Arrays.asList(new String[] { I18n.format("bspkrs.configgui.tooltip.undoChanges") });
            this.defaultToolTip = Arrays.asList(new String[] { I18n.format("bspkrs.configgui.tooltip.resetToDefault") });
            
            this.drawLabel = true;
            
            String comment;
            
            if (prop.getType().equals(ConfigGuiType.INTEGER))
                comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                        "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinIntValue(), prop.getMaxIntValue());
            else
                comment = I18n.format(prop.getLanguageKey() + ".tooltip",
                        "\n" + EnumChatFormatting.AQUA, prop.getDefault(), prop.getMinDoubleValue(), prop.getMaxDoubleValue());
            
            if (!comment.equals(prop.getLanguageKey() + ".tooltip"))
                toolTip = this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
            else if (prop.getComment() != null && !prop.getComment().trim().isEmpty())
                toolTip = this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + prop.getComment(), 300);
            else
                toolTip = this.mc.fontRenderer.listFormattedStringToWidth(
                        EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            boolean isChanged = isChanged();
            
            if (drawLabel)
            {
                String label = (!isValidValue ? EnumChatFormatting.RED.toString() :
                        (isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString()))
                        + (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + this.propName;
                this.mc.fontRenderer.drawString(
                        label,
                        this.parentGuiConfig.propertyList.labelX,
                        y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2,
                        16777215);
            }
            
            this.btnUndoChanges.xPosition = this.parentPropertyList.scrollBarX - 44;
            this.btnUndoChanges.yPosition = y;
            this.btnUndoChanges.enabled = enabled() && isChanged;
            this.btnUndoChanges.displayString = "↩";
            this.btnUndoChanges.drawButton(this.mc, mouseX, mouseY);
            
            this.btnDefault.xPosition = this.parentPropertyList.scrollBarX - 22;
            this.btnDefault.yPosition = y;
            this.btnDefault.enabled = enabled() && !isDefault();
            this.btnDefault.displayString = "☄";
            this.btnDefault.drawButton(this.mc, mouseX, mouseY);
            
            if (this.tooltipHoverChecker == null)
                this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, this.parentGuiConfig.propertyList.controlX - 8, 800);
            else
                this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, this.parentGuiConfig.propertyList.controlX - 8);
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            boolean canHover = mouseY < this.parentGuiConfig.propertyList.bottom && mouseY > this.parentGuiConfig.propertyList.top;
            if (toolTip != null && this.tooltipHoverChecker != null)
            {
                if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
                    this.parentGuiConfig.drawToolTip(toolTip, mouseX, mouseY);
            }
            
            if (this.undoHoverChecker.checkHover(mouseX, mouseY, canHover))
                this.parentGuiConfig.drawToolTip(undoToolTip, mouseX, mouseY);
            
            if (this.defaultHoverChecker.checkHover(mouseX, mouseY, canHover))
                this.parentGuiConfig.drawToolTip(defaultToolTip, mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnDefault.mousePressed(this.mc, x, y))
            {
                btnDefault.func_146113_a(mc.getSoundHandler());
                setToDefault();
                return true;
            }
            else if (this.btnUndoChanges.mousePressed(this.mc, x, y))
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
        
        @Override
        public boolean enabled()
        {
            return parentGuiConfig.allowNonHotLoadConfigChanges || parentGuiConfig.areAllPropsHotLoadable || prop.isHotLoadable();
        }
        
        @Override
        public int getLabelWidth()
        {
            return this.mc.fontRenderer.getStringWidth(this.propName);
        }
        
        @Override
        public int getEntryRightBound()
        {
            return this.parentPropertyList.resetX + 40;
        }
        
    }
    
    /**
     * Provides an interface for defining GuiPropertyList.listEntry objects.
     */
    public interface IGuiConfigListEntry extends GuiListExtended.IGuiListEntry
    {
        /**
         * Is this list entry enabled?
         * 
         * @return true if this entry's controls should be enabled, false otherwise.
         */
        public boolean enabled();
        
        /**
         * Handles user keystrokes for any GuiTextField objects in this entry. Call {@code GuiTextField.keyTyped()} for any GuiTextField
         * objects that should receive the input provided.
         */
        public void keyTyped(char eventChar, int eventKey);
        
        /**
         * Call {@code GuiTextField.updateCursorCounter()} for any GuiTextField objects in this entry.
         */
        public void updateCursorCounter();
        
        /**
         * Call {@code GuiTextField.mouseClicked()} for and GuiTextField objects in this entry.
         */
        public void mouseClicked(int x, int y, int mouseEvent);
        
        /**
         * Is this entry's value equal to the default value? Generally true should be returned if this entry is not a property or category
         * entry.
         * 
         * @return true if this entry's value is equal to this entry's default value.
         */
        public boolean isDefault();
        
        /**
         * Sets this entry's value to the default value.
         */
        public void setToDefault();
        
        /**
         * Handles reverting any changes that have occurred to this entry.
         */
        public void undoChanges();
        
        /**
         * Has the value of this entry changed?
         * 
         * @return true if changes have been made to this entry's value, false otherwise.
         */
        public boolean isChanged();
        
        /**
         * Handles saving any changes that have been made to this entry back to the underlying object. It is a good practice to check
         * isChanged() before performing the save action.
         */
        public void saveProperty();
        
        /**
         * Handles drawing any tooltips that apply to this entry. This method is called after all other GUI elements have been drawn to the
         * screen, so it could also be used to draw any GUI element that needs to be drawn after all entries have had drawEntry() called.
         */
        public void drawToolTip(int mouseX, int mouseY);
        
        /**
         * Gets this entry's label width.
         */
        public int getLabelWidth();
        
        /**
         * Gets this entry's right-hand x boundary. This value is used to control where the scroll bar is placed.
         */
        public int getEntryRightBound();
    }
}