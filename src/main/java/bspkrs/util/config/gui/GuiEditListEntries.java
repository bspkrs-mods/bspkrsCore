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

@Deprecated
public class GuiEditListEntries extends GuiListExtended
{
    private final GuiEditList             parentGuiEditList;
    private final Minecraft               mc;
    private final IConfigProperty         prop;
    private final List<IGuiEditListEntry> listEntries;
    private boolean                       isDefault;
    private boolean                       isChanged;
    private boolean                       canAddMoreEntries;
    private final int                     controlWidth;
    private final String[]                beforeValues;

    public GuiEditListEntries(GuiEditList parent, Minecraft mc, IConfigProperty prop, String[] beforeValues, String[] currentValues)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? (parent.titleLine3 != null ? 43 : 33) : 23, parent.height - 32, 20);
        parentGuiEditList = parent;
        this.mc = mc;
        this.prop = prop;
        this.beforeValues = beforeValues;
        this.setShowSelectionBox(false);
        isChanged = !Arrays.deepEquals(beforeValues, currentValues);
        isDefault = Arrays.deepEquals(currentValues, prop.getDefaults());
        canAddMoreEntries = !prop.isListLengthFixed() && ((prop.getMaxListLength() == -1) || (currentValues.length < prop.getMaxListLength()));

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
        canAddMoreEntries = !prop.isListLengthFixed() && ((prop.getMaxListLength() == -1) || ((listEntries.size() - 1) < prop.getMaxListLength()));
        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }

    private void removeEntryAtIndex(int index)
    {
        listEntries.remove(index);
        canAddMoreEntries = !prop.isListLengthFixed() && ((prop.getMaxListLength() == -1) || ((listEntries.size() - 1) < prop.getMaxListLength()));
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
        for (IGuiEditListEntry entry : listEntries)
            entry.keyTyped(eventChar, eventKey);

        recalculateState();
    }

    protected void updateScreen()
    {
        for (IGuiEditListEntry entry : listEntries)
            entry.updateCursorCounter();
    }

    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        for (IGuiEditListEntry entry : listEntries)
            entry.mouseClicked(x, y, mouseEvent);
    }

    protected boolean isListSavable()
    {
        for (IGuiEditListEntry entry : listEntries)
            if (!entry.isValueSavable())
                return false;

        return true;
    }

    protected void saveListChanges()
    {
        int listLength = prop.isListLengthFixed() ? listEntries.size() : listEntries.size() - 1;

        if ((parentGuiEditList.slotIndex != -1) && (parentGuiEditList.parentScreen != null)
                && (parentGuiEditList.parentScreen instanceof GuiConfig)
                && (((GuiConfig) parentGuiEditList.parentScreen).propertyList.getListEntry(parentGuiEditList.slotIndex) instanceof EditListPropEntry))
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
        for (IGuiEditListEntry entry : listEntries)
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
            isValidated = true;
        }

        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            if (parentGuiEditList.enabled || (eventKey == Keyboard.KEY_LEFT) || (eventKey == Keyboard.KEY_RIGHT)
                    || (eventKey == Keyboard.KEY_HOME) || (eventKey == Keyboard.KEY_END))
            {
                String validChars = "0123456789";
                String before = textFieldValue.getText();
                if (validChars.contains(String.valueOf(eventChar)) ||
                        (!before.startsWith("-") && (textFieldValue.getCursorPosition() == 0) && (eventChar == '-'))
                        || (!before.contains(".") && (eventChar == '.'))
                        || (eventKey == Keyboard.KEY_BACK) || (eventKey == Keyboard.KEY_DELETE) || (eventKey == Keyboard.KEY_LEFT) || (eventKey == Keyboard.KEY_RIGHT)
                        || (eventKey == Keyboard.KEY_HOME) || (eventKey == Keyboard.KEY_END))
                    textFieldValue.textboxKeyTyped((parentGuiEditList.enabled ? eventChar : Keyboard.CHAR_NONE), eventKey);

                if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-"))
                {
                    try
                    {
                        double value = Double.parseDouble(textFieldValue.getText().trim());
                        if ((value < prop.getMinDoubleValue()) || (value > prop.getMaxDoubleValue()))
                            isValidValue = false;
                        else
                            isValidValue = true;
                    }
                    catch (Throwable e)
                    {
                        isValidValue = false;
                    }
                }
                else
                    isValidValue = false;
            }
        }

        @Override
        public String getValue()
        {
            return textFieldValue.getText().trim();
        }
    }

    public class EditListIntegerEntry extends EditListStringEntry
    {
        public EditListIntegerEntry(int value)
        {
            super(String.valueOf(value));
            isValidated = true;
        }

        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            if (parentGuiEditList.enabled || (eventKey == Keyboard.KEY_LEFT) || (eventKey == Keyboard.KEY_RIGHT)
                    || (eventKey == Keyboard.KEY_HOME) || (eventKey == Keyboard.KEY_END))
            {
                String validChars = "0123456789";
                String before = textFieldValue.getText();
                if (validChars.contains(String.valueOf(eventChar))
                        || (!before.startsWith("-") && (textFieldValue.getCursorPosition() == 0) && (eventChar == '-'))
                        || (eventKey == Keyboard.KEY_BACK) || (eventKey == Keyboard.KEY_DELETE)
                        || (eventKey == Keyboard.KEY_LEFT) || (eventKey == Keyboard.KEY_RIGHT) || (eventKey == Keyboard.KEY_HOME) || (eventKey == Keyboard.KEY_END))
                    textFieldValue.textboxKeyTyped((parentGuiEditList.enabled ? eventChar : Keyboard.CHAR_NONE), eventKey);

                if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-"))
                {
                    try
                    {
                        long value = Long.parseLong(textFieldValue.getText().trim());
                        if ((value < prop.getMinIntValue()) || (value > prop.getMaxIntValue()))
                            isValidValue = false;
                        else
                            isValidValue = true;
                    }
                    catch (Throwable e)
                    {
                        isValidValue = false;
                    }
                }
                else
                    isValidValue = false;
            }
        }

        @Override
        public String getValue()
        {
            return textFieldValue.getText().trim();
        }
    }

    public class EditListStringEntry extends EditListBaseEntry
    {
        protected final GuiTextField textFieldValue;

        public EditListStringEntry(String value)
        {
            super();
            textFieldValue = new GuiTextField(mc.fontRendererObj, (width / 4) + 1, 0, controlWidth - 3, 16);
            textFieldValue.setMaxStringLength(10000);
            textFieldValue.setText(value);
            isValidated = prop.getValidStringPattern() != null;
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            if (prop.isListLengthFixed() || (slotIndex != (listEntries.size() - 1)))
            {
                textFieldValue.setVisible(true);
                try
                {
                    if (ReflectionHelper.getIntValue(GuiTextField.class, "field_146210_g", "yPosition", textFieldValue, -1) != (y + 1))
                        ReflectionHelper.setIntValue(GuiTextField.class, "field_146210_g", "yPosition", textFieldValue, y + 1);
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
                textFieldValue.drawTextBox();
            }
            else
                textFieldValue.setVisible(false);
        }

        @Override
        public void keyTyped(char eventChar, int eventKey)
        {
            if (parentGuiEditList.enabled || (eventKey == Keyboard.KEY_LEFT) || (eventKey == Keyboard.KEY_RIGHT)
                    || (eventKey == Keyboard.KEY_HOME) || (eventKey == Keyboard.KEY_END))
            {
                textFieldValue.textboxKeyTyped((parentGuiEditList.enabled ? eventChar : Keyboard.CHAR_NONE), eventKey);

                if (prop.getValidStringPattern() != null)
                {
                    if (prop.getValidStringPattern().matcher(textFieldValue.getText().trim()).matches())
                        isValidValue = true;
                    else
                        isValidValue = false;
                }
            }
        }

        @Override
        public void updateCursorCounter()
        {
            textFieldValue.updateCursorCounter();
        }

        @Override
        public void mouseClicked(int x, int y, int mouseEvent)
        {
            textFieldValue.mouseClicked(x, y, mouseEvent);
        }

        @Override
        public String getValue()
        {
            return textFieldValue.getText().trim();
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
            btnValue = new GuiButtonExt(0, 0, 0, controlWidth, 18, I18n.format(String.valueOf(value)));
            btnValue.enabled = parentGuiEditList.enabled;
            isValidated = false;
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
            btnValue.xPosition = listWidth / 4;
            btnValue.yPosition = y;

            String trans = I18n.format(String.valueOf(value));
            if (!trans.equals(String.valueOf(value)))
                btnValue.displayString = trans;
            else
                btnValue.displayString = String.valueOf(value);
            btnValue.packedFGColour = value ? HUDUtils.getColorCode('2', true) : HUDUtils.getColorCode('4', true);

            btnValue.drawButton(mc, mouseX, mouseY);
        }

        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (btnValue.mousePressed(mc, x, y))
            {
                btnValue.playPressSound(mc.getSoundHandler());
                value = !value;
                recalculateState();
                return true;
            }

            return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }

        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            btnValue.mouseReleased(x, y);
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
        @SuppressWarnings("rawtypes")
        private final List           addNewToolTip, removeToolTip;
        protected boolean            isValidValue = true;
        protected boolean            isValidated  = false;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public EditListBaseEntry()
        {
            btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
            btnAddNewEntryAbove.packedFGColour = HUDUtils.getColorCode('2', true);
            btnAddNewEntryAbove.enabled = parentGuiEditList.enabled;
            btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
            btnRemoveEntry.packedFGColour = HUDUtils.getColorCode('c', true);
            btnRemoveEntry.enabled = parentGuiEditList.enabled;
            addNewEntryAboveHoverChecker = new HoverChecker(btnAddNewEntryAbove, 800);
            removeEntryHoverChecker = new HoverChecker(btnRemoveEntry, 800);
            addNewToolTip = new ArrayList();
            removeToolTip = new ArrayList();
            addNewToolTip.add(I18n.format("bspkrs.configgui.tooltip.addNewEntryAbove"));
            removeToolTip.add(I18n.format("bspkrs.configgui.tooltip.removeEntry"));
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            if ((this.getValue() != null) && isValidated)
                mc.fontRendererObj.drawString(
                        isValidValue ? EnumChatFormatting.GREEN + "✔" : EnumChatFormatting.RED + "✕",
                        (listWidth / 4) - mc.fontRendererObj.getStringWidth("✔") - 2,
                        (y + (slotHeight / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2),
                        16777215);

            int half = listWidth / 2;
            if (canAddMoreEntries)
            {
                btnAddNewEntryAbove.visible = true;
                btnAddNewEntryAbove.xPosition = half + ((half / 2) - 44);
                btnAddNewEntryAbove.yPosition = y;
                btnAddNewEntryAbove.drawButton(mc, mouseX, mouseY);
            }
            else
                btnAddNewEntryAbove.visible = false;

            if (!prop.isListLengthFixed() && (slotIndex != (listEntries.size() - 1)))
            {
                btnRemoveEntry.visible = true;
                btnRemoveEntry.xPosition = half + ((half / 2) - 22);
                btnRemoveEntry.yPosition = y;
                btnRemoveEntry.drawButton(mc, mouseX, mouseY);
            }
            else
                btnRemoveEntry.visible = false;
        }

        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {
            boolean canHover = (mouseY < GuiEditListEntries.this.bottom) && (mouseY > GuiEditListEntries.this.top);
            if (btnAddNewEntryAbove.visible && addNewEntryAboveHoverChecker.checkHover(mouseX, mouseY, canHover))
                parentGuiEditList.drawToolTip(addNewToolTip, mouseX, mouseY);
            if (btnRemoveEntry.visible && removeEntryHoverChecker.checkHover(mouseX, mouseY, canHover))
                parentGuiEditList.drawToolTip(removeToolTip, mouseX, mouseY);
        }

        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (btnAddNewEntryAbove.mousePressed(mc, x, y))
            {
                btnAddNewEntryAbove.playPressSound(mc.getSoundHandler());
                addNewEntryAtIndex(index);
                recalculateState();
                return true;
            }
            else if (btnRemoveEntry.mousePressed(mc, x, y))
            {
                btnRemoveEntry.playPressSound(mc.getSoundHandler());
                removeEntryAtIndex(index);
                recalculateState();
                return true;
            }

            return false;
        }

        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            btnAddNewEntryAbove.mouseReleased(x, y);
            btnRemoveEntry.mouseReleased(x, y);
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
