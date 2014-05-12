package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import bspkrs.util.config.gui.GuiPropertyList.SelectValuePropEntry;

public class GuiSelectStringEntries extends GuiListExtended
{
    private GuiSelectString                 parentGuiSelectString;
    private Minecraft                       mc;
    private IConfigProperty                 prop;
    private List<IGuiSelectStringListEntry> listEntries;
    private boolean                         isDefault;
    private boolean                         isChanged;
    private final Map<String, String>       selectableValues;
    private final String                    beforeValue;
    public String                           currentValue;
    private int                             selectedIndex;
    private int                             maxEntryWidth = 0;
    
    public GuiSelectStringEntries(GuiSelectString parent, Minecraft mc, IConfigProperty prop, Map<String, String> selectableValues, String beforeValue, String currentValue)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? (parent.titleLine3 != null ? 43 : 33) : 23, parent.height - 32, 11);
        this.parentGuiSelectString = parent;
        this.mc = mc;
        this.prop = prop;
        this.selectableValues = selectableValues;
        this.beforeValue = beforeValue;
        this.currentValue = currentValue;
        this.setShowSelectionBox(true);
        this.isChanged = beforeValue.equals(currentValue);
        this.isDefault = currentValue.equals(prop.getDefault());
        
        listEntries = new ArrayList<IGuiSelectStringListEntry>();
        
        int index = 0;
        List<Entry<String, String>> sortedList = new ArrayList<Entry<String, String>>(selectableValues.entrySet());
        Collections.sort(sortedList, new EntryComparator());
        
        for (Entry<String, String> entry : sortedList)
        {
            listEntries.add(new ListEntry(entry));
            if (mc.fontRenderer.getStringWidth(entry.getValue()) > maxEntryWidth)
                maxEntryWidth = mc.fontRenderer.getStringWidth(entry.getValue());
            
            if (this.currentValue.equals(entry.getKey()))
                this.selectedIndex = index;
            
            index++;
        }
    }
    
    public static class EntryComparator implements Comparator<Entry<String, String>>
    {
        @Override
        public int compare(Entry<String, String> o1, Entry<String, String> o2)
        {
            int compare = o1.getValue().toLowerCase().compareTo(o2.getValue().toLowerCase());
            
            if (compare == 0)
                compare = o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
            
            return compare;
        }
    }
    
    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    @Override
    protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY)
    {
        selectedIndex = index;
        this.currentValue = listEntries.get(index).getValue();
    }
    
    /**
     * Returns true if the element passed in is currently selected
     */
    @Override
    protected boolean isSelected(int index)
    {
        return index == selectedIndex;
    }
    
    @Override
    protected int getScrollBarX()
    {
        return width / 2 + this.maxEntryWidth / 2 + 5;
    }
    
    /**
     * Gets the width of the list
     */
    @Override
    public int getListWidth()
    {
        return maxEntryWidth + 5;
    }
    
    @Override
    public IGuiSelectStringListEntry getListEntry(int index)
    {
        return listEntries.get(index);
    }
    
    @Override
    protected int getSize()
    {
        return listEntries.size();
    }
    
    protected boolean isChanged()
    {
        return !beforeValue.equals(currentValue);
    }
    
    protected boolean isDefault()
    {
        return currentValue.equals(prop.getDefault());
    }
    
    protected void saveChanges()
    {
        if (parentGuiSelectString.slotIndex != -1 && parentGuiSelectString.parentScreen != null
                && parentGuiSelectString.parentScreen instanceof GuiConfig
                && ((GuiConfig) parentGuiSelectString.parentScreen).propertyList.getListEntry(parentGuiSelectString.slotIndex) instanceof SelectValuePropEntry)
        {
            SelectValuePropEntry entry = (SelectValuePropEntry) ((GuiConfig) parentGuiSelectString.parentScreen).propertyList.getListEntry(parentGuiSelectString.slotIndex);
            
            entry.setValueFromChildScreen(currentValue);
        }
        else
            prop.set(currentValue);
    }
    
    public class ListEntry implements IGuiSelectStringListEntry
    {
        protected final Entry<String, String> value;
        
        public ListEntry(Entry<String, String> value)
        {
            this.value = value;
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            GuiSelectStringEntries.this.mc.fontRenderer.drawString(value.getValue(), x + 1, y, slotIndex == selectedIndex ? 16777215 : 14737632);
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            return false;
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {}
        
        @Override
        public String getValue()
        {
            return value.getKey();
        }
    }
    
    public interface IGuiSelectStringListEntry extends GuiListExtended.IGuiListEntry
    {
        public String getValue();
    }
}
