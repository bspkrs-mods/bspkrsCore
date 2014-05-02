package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import bspkrs.client.util.HUDUtils;
import bspkrs.util.ReflectionHelper;
import bspkrs.util.config.gui.GuiPropertyList.EditListPropEntry;

public class GuiEditListEntries extends GuiListExtended
{
    private GuiEditList             parentGuiEditList;
    private Minecraft               mc;
    private IConfigProperty         prop;
    private List<IGuiEditListEntry> listEntries;
    private boolean                 isDefault;
    private boolean                 isChanged;
    private boolean                 canAddMoreEntries;
    private final int               controlWidth;
    private final String[]          beforeValues;
    private String[]                currentValues;
    
    public GuiEditListEntries(GuiEditList parent, Minecraft mc, IConfigProperty prop, String[] beforeValues, String[] currentValues)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? (parent.titleLine3 != null ? 43 : 33) : 23, parent.height - 32, 20);
        this.parentGuiEditList = parent;
        this.mc = mc;
        this.prop = prop;
        this.beforeValues = beforeValues;
        this.currentValues = currentValues;
        this.setShowSelectionBox(false);
        this.isChanged = !Arrays.deepEquals(beforeValues, currentValues);
        this.isDefault = Arrays.deepEquals(currentValues, prop.getDefaults());
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || currentValues.length < prop.getMaxListLength());
        
        listEntries = new ArrayList<IGuiEditListEntry>();
        
        controlWidth = (parent.width / 2) - (prop.isListLengthFixed() ? 0 : 48);
        
        if (prop.isList() && prop.getType().equals(ConfigGuiType.BOOLEAN))
            for (String value : currentValues)
                listEntries.add(new EditListBooleanEntry(Boolean.valueOf(value)));
        else if (prop.isList() && prop.getType().equals(ConfigGuiType.INTEGER))
            for (String value : currentValues)
                listEntries.add(new EditListIntegerEntry(Integer.parseInt(value)));
        else if (prop.isList() && prop.getType().equals(ConfigGuiType.DOUBLE))
            for (String value : currentValues)
                listEntries.add(new EditListDoubleEntry(Double.parseDouble(value)));
        else if (prop.isList())
            for (String value : currentValues)
                listEntries.add(new EditListStringEntry(value));
        
        if (!prop.isListLengthFixed())
            listEntries.add(new EditListBaseEntry());
        
    }
    
    @Override
    protected int getScrollBarX()
    {
        return width - (width / 4);
    }
    
    /**
     * Gets the width of the list
     */
    @Override
    public int getListWidth()
    {
        return parentGuiEditList.width;
    }
    
    @Override
    public IGuiEditListEntry getListEntry(int index)
    {
        return listEntries.get(index);
    }
    
    @Override
    protected int getSize()
    {
        return listEntries.size();
    }
    
    private void addNewEntryAtIndex(int index)
    {
        if (prop.isList() && prop.getType().equals(ConfigGuiType.BOOLEAN))
            listEntries.add(index, new EditListBooleanEntry(true));
        else if (prop.isList() && prop.getType().equals(ConfigGuiType.INTEGER))
            listEntries.add(index, new EditListIntegerEntry(0));
        else if (prop.isList() && prop.getType().equals(ConfigGuiType.DOUBLE))
            listEntries.add(index, new EditListDoubleEntry(0.0F));
        else if (prop.isList())
            listEntries.add(index, new EditListStringEntry(""));
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || this.listEntries.size() - 1 < prop.getMaxListLength());
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }
    
    private void removeEntryAtIndex(int index)
    {
        this.listEntries.remove(index);
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || this.listEntries.size() - 1 < prop.getMaxListLength());
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }
    
    protected boolean isChanged()
    {
        return isChanged;
    }
    
    protected boolean isDefault()
    {
        return isDefault;
    }
    
    private void recalculateState()
    {
        isDefault = true;
        isChanged = false;
        
        int listLength = prop.isListLengthFixed() ? listEntries.size() : listEntries.size() - 1;
        
        if (listLength != prop.getDefaults().length)
        {
            isDefault = false;
        }
        
        if (listLength != beforeValues.length)
        {
            isChanged = true;
        }
        
        if (isDefault)
            for (int i = 0; i < listLength; i++)
                if (!prop.getDefaults()[i].equals(listEntries.get(i).getValue()))
                    isDefault = false;
        
        if (!isChanged)
            for (int i = 0; i < listLength; i++)
                if (!beforeValues[i].equals(listEntries.get(i).getValue()))
                    isChanged = true;
    }
    
    protected void keyTyped(char eventChar, int eventKey)
    {
        for (IGuiEditListEntry entry : this.listEntries)
            entry.keyTyped(eventChar, eventKey);
        
        recalculateState();
    }
    
    protected void updateScreen()
    {
        for (IGuiEditListEntry entry : this.listEntries)
            entry.updateCursorCounter();
    }
    
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        for (IGuiEditListEntry entry : this.listEntries)
            entry.mouseClicked(x, y, mouseEvent);
    }
    
    protected boolean isListSavable()
    {
        for (IGuiEditListEntry entry : this.listEntries)
            if (!entry.isValueSavable())
                return false;
        
        return true;
    }
    
    protected void saveListChanges()
    {
        int listLength = prop.isListLengthFixed() ? listEntries.size() : listEntries.size() - 1;
        
        if (parentGuiEditList.slotIndex != -1 && parentGuiEditList.parentScreen != null
                && parentGuiEditList.parentScreen instanceof GuiConfig
                && ((GuiConfig) parentGuiEditList.parentScreen).propertyList.getListEntry(parentGuiEditList.slotIndex) instanceof EditListPropEntry)
        {
            EditListPropEntry entry = (EditListPropEntry) ((GuiConfig) parentGuiEditList.parentScreen).propertyList.getListEntry(parentGuiEditList.slotIndex);
            
            String[] as = new String[listLength];
            for (int i = 0; i < listLength; i++)
                as[i] = listEntries.get(i).getValue();
            
            entry.setListFromChildScreen(as);
        }
        else
        {
            if (prop.isList() && prop.getType().equals(ConfigGuiType.BOOLEAN))
            {
                boolean[] abol = new boolean[listLength];
                for (int i = 0; i < listLength; i++)
                    abol[i] = Boolean.parseBoolean(listEntries.get(i).getValue());
                
                prop.set(abol);
            }
            else if (prop.isList() && prop.getType().equals(ConfigGuiType.INTEGER))
            {
                int[] ai = new int[listLength];
                for (int i = 0; i < listLength; i++)
                    ai[i] = Integer.parseInt(listEntries.get(i).getValue());
                
                prop.set(ai);
            }
            else if (prop.isList() && prop.getType().equals(ConfigGuiType.DOUBLE))
            {
                double[] ad = new double[listLength];
                for (int i = 0; i < listLength; i++)
                    ad[i] = Double.parseDouble(listEntries.get(i).getValue());
                
                prop.set(ad);
            }
            else if (prop.isList())
            {
                String[] as = new String[listLength];
                for (int i = 0; i < listLength; i++)
                    as[i] = listEntries.get(i).getValue();
                
                prop.set(as);
            }
        }
    }
    
    protected void drawScreenPost(int mouseX, int mouseY, float f)
    {
        for (IGuiEditListEntry entry : this.listEntries)
            entry.drawToolTip(mouseX, mouseY);
    }
    
    /**
     * IGuiListEntry Inner Classes
     */
    
    public class EditListDoubleEntry extends EditListStringEntry
    {
        public EditListDoubleEntry(double value)
        {
            super(String.valueOf(value));
            this.isValidated = true;
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
        public String getValue()
        {
            return this.textFieldValue.getText().trim();
        }
    }
    
    public class EditListIntegerEntry extends EditListStringEntry
    {
        public EditListIntegerEntry(int value)
        {
            super(String.valueOf(value));
            this.isValidated = true;
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
        public String getValue()
        {
            return this.textFieldValue.getText().trim();
        }
    }
    
    public class EditListStringEntry extends EditListBaseEntry
    {
        protected final GuiTextField textFieldValue;
        
        public EditListStringEntry(String value)
        {
            super();
            this.textFieldValue = new GuiTextField(GuiEditListEntries.this.mc.fontRenderer, width / 4 + 1, 0, controlWidth - 3, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(value);
            this.isValidated = prop.getValidStringPattern() != null;
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            if (prop.isListLengthFixed() || slotIndex != listEntries.size() - 1)
            {
                this.textFieldValue.setVisible(true);
                try
                {
                    if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, -1) != y + 1)
                        ReflectionHelper.setIntValue(GuiTextField.class, "field_146210_g", "yPosition", this.textFieldValue, y + 1);
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
                this.textFieldValue.drawTextBox();
            }
            else
                this.textFieldValue.setVisible(false);
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
        public String getValue()
        {
            return this.textFieldValue.getText().trim();
        }
        
    }
    
    public class EditListBooleanEntry extends EditListBaseEntry
    {
        protected final GuiButtonExt btnValue;
        private boolean              value;
        
        public EditListBooleanEntry(boolean value)
        {
            super();
            this.value = value;
            this.btnValue = new GuiButtonExt(0, 0, 0, controlWidth, 18, I18n.format(String.valueOf(value)));
            this.isValidated = false;
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            this.btnValue.xPosition = width / 4;
            this.btnValue.yPosition = y;
            
            String trans = I18n.format(String.valueOf(value));
            if (!trans.equals(String.valueOf(value)))
                this.btnValue.displayString = trans;
            else
                this.btnValue.displayString = String.valueOf(value);
            btnValue.packedFGColour = value ? HUDUtils.getColorCode('2', true) : HUDUtils.getColorCode('4', true);
            
            this.btnValue.drawButton(GuiEditListEntries.this.mc, mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnValue.mousePressed(GuiEditListEntries.this.mc, x, y))
            {
                btnValue.func_146113_a(mc.getSoundHandler());
                value = !value;
                recalculateState();
                return true;
            }
            
            return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnValue.mouseReleased(x, y);
            super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
        }
        
        @Override
        public String getValue()
        {
            return String.valueOf(value);
        }
    }
    
    public class EditListBaseEntry implements IGuiEditListEntry
    {
        protected final GuiButtonExt btnAddNewEntryAbove;
        private final HoverChecker   addNewEntryAboveHoverChecker;
        protected final GuiButtonExt btnRemoveEntry;
        private final HoverChecker   removeEntryHoverChecker;
        private List                 addNewToolTip, removeToolTip;
        protected boolean            isValidValue = true;
        protected boolean            isValidated  = false;
        
        public EditListBaseEntry()
        {
            this.btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
            this.btnAddNewEntryAbove.packedFGColour = HUDUtils.getColorCode('2', true);
            this.btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
            this.btnRemoveEntry.packedFGColour = HUDUtils.getColorCode('c', true);
            this.addNewEntryAboveHoverChecker = new HoverChecker(this.btnAddNewEntryAbove, 800);
            this.removeEntryHoverChecker = new HoverChecker(this.btnRemoveEntry, 800);
            this.addNewToolTip = new ArrayList();
            this.removeToolTip = new ArrayList();
            addNewToolTip.add(I18n.format("bspkrs.configgui.tooltip.addNewEntryAbove"));
            removeToolTip.add(I18n.format("bspkrs.configgui.tooltip.removeEntry"));
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            if (this.getValue() != null && this.isValidated)
                GuiEditListEntries.this.mc.fontRenderer.drawString(
                        isValidValue ? EnumChatFormatting.GREEN + "✔" : EnumChatFormatting.RED + "✕",
                        width / 4 - mc.fontRenderer.getStringWidth("✔") - 2,
                        y + slotHeight / 2 - GuiEditListEntries.this.mc.fontRenderer.FONT_HEIGHT / 2,
                        16777215);
            
            int half = width / 2;
            if (canAddMoreEntries)
            {
                this.btnAddNewEntryAbove.visible = true;
                this.btnAddNewEntryAbove.xPosition = half + ((half / 2) - 44);
                this.btnAddNewEntryAbove.yPosition = y;
                this.btnAddNewEntryAbove.drawButton(GuiEditListEntries.this.mc, mouseX, mouseY);
            }
            else
                this.btnAddNewEntryAbove.visible = false;
            
            if (!prop.isListLengthFixed() && slotIndex != listEntries.size() - 1)
            {
                this.btnRemoveEntry.visible = true;
                this.btnRemoveEntry.xPosition = half + ((half / 2) - 22);
                this.btnRemoveEntry.yPosition = y;
                this.btnRemoveEntry.drawButton(GuiEditListEntries.this.mc, mouseX, mouseY);
            }
            else
                this.btnRemoveEntry.visible = false;
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            boolean canHover = mouseY < GuiEditListEntries.this.bottom && mouseY > GuiEditListEntries.this.top;
            if (this.btnAddNewEntryAbove.visible && this.addNewEntryAboveHoverChecker.checkHover(mouseX, mouseY, canHover))
                GuiEditListEntries.this.parentGuiEditList.drawToolTip(this.addNewToolTip, mouseX, mouseY);
            if (this.btnRemoveEntry.visible && this.removeEntryHoverChecker.checkHover(mouseX, mouseY, canHover))
                GuiEditListEntries.this.parentGuiEditList.drawToolTip(this.removeToolTip, mouseX, mouseY);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnAddNewEntryAbove.mousePressed(GuiEditListEntries.this.mc, x, y))
            {
                btnAddNewEntryAbove.func_146113_a(mc.getSoundHandler());
                addNewEntryAtIndex(index);
                recalculateState();
                return true;
            }
            else if (this.btnRemoveEntry.mousePressed(GuiEditListEntries.this.mc, x, y))
            {
                btnRemoveEntry.func_146113_a(mc.getSoundHandler());
                removeEntryAtIndex(index);
                recalculateState();
                return true;
            }
            
            return false;
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            this.btnAddNewEntryAbove.mouseReleased(x, y);
            this.btnRemoveEntry.mouseReleased(x, y);
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
        public boolean isValueSavable()
        {
            return isValidValue;
        }
        
        @Override
        public String getValue()
        {
            return null;
        }
    }
    
    public interface IGuiEditListEntry extends GuiListExtended.IGuiListEntry
    {
        public void keyTyped(char eventChar, int eventKey);
        
        public void updateCursorCounter();
        
        public void mouseClicked(int x, int y, int mouseEvent);
        
        public void drawToolTip(int mouseX, int mouseY);
        
        public boolean isValueSavable();
        
        public String getValue();
    }
}
