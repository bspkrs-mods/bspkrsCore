package bspkrs.util.config.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;

public class GuiEditListEntries extends GuiListExtended
{
    private GuiEditList             parentGuiEditList;
    private Minecraft               mc;
    private IConfigProperty         prop;
    private List<IGuiEditListEntry> listEntries;
    private boolean                 isDirty;
    private boolean                 canAddMoreEntries;
    
    public GuiEditListEntries(GuiEditList parent, Minecraft mc, IConfigProperty prop)
    {
        super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);
        this.prop = prop;
        this.isDirty = !this.prop.isDefault();
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || prop.getStringList().length < prop.getMaxListLength());
        
        if (prop.isListLengthFixed() || (prop.getMaxListLength() > -1 && prop.getStringList().length == prop.getMaxListLength()))
            listEntries = new ArrayList<IGuiEditListEntry>(prop.getStringList().length);
        else
            listEntries = new ArrayList<IGuiEditListEntry>(prop.getStringList().length + 1);
        
    }
    
    @Override
    protected int getScrollBarX()
    {
        return width - (width / 4);
    }
    
    @Override
    public IGuiEditListEntry getListEntry(int index)
    {
        if (index == listEntries.size())
            // TODO: return the add new item entry
            return null;
        else
            return listEntries.get(index);
    }
    
    @Override
    protected int getSize()
    {
        if (!canAddMoreEntries)
            return listEntries.size();
        else
            return listEntries.size() + 1;
    }
    
    private void addNewEntryAtIndex(int index)
    {
        
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || prop.getStringList().length < prop.getMaxListLength());
    }
    
    private void removeEntryAtIndex(int index)
    {
        
        this.canAddMoreEntries = !prop.isListLengthFixed() && (prop.getMaxListLength() == -1 || prop.getStringList().length < prop.getMaxListLength());
    }
    
    protected boolean isDirty()
    {
        return isDirty;
    }
    
    protected void keyTyped(char eventChar, int eventKey)
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries.get(i).keyTyped(eventChar, eventKey);
    }
    
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
        for (int i = 0; i < this.getSize(); i++)
            listEntries.get(i).mouseClicked(x, y, mouseEvent);
    }
    
    protected void saveListChanges()
    {
        // TODO:
    }
    
    protected void drawScreenPost(int mouseX, int mouseY, float f)
    {   
        
    }
    
    /**
     * IGuiListEntry Inner Classes
     */
    
    public class EditListStringEntry implements IGuiEditListEntry
    {
        protected final GuiButton    btnAddNewEntryAbove;
        protected final GuiButton    btnRemoveEntry;
        protected final GuiTextField textFieldValue;
        private List                 toolTip;
        private int                  x, y, listWidth, slotHeight;
        
        public EditListStringEntry(String value)
        {
            this.btnAddNewEntryAbove = new GuiButton(0, 0, 0, 18, 18, "+");
            this.btnRemoveEntry = new GuiButton(0, 0, 0, 18, 18, "X");
            this.textFieldValue = new GuiTextField(GuiEditListEntries.this.mc.fontRenderer, 0, 0, 200 - 4, 16);
            this.textFieldValue.setMaxStringLength(10000);
            this.textFieldValue.setText(value);
        }
        
        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
        {
            int half = listWidth / 2;
            this.btnAddNewEntryAbove.xPosition = half + ((half / 2) - 44);
            this.btnAddNewEntryAbove.yPosition = y;
            this.btnAddNewEntryAbove.drawButton(GuiEditListEntries.this.mc, mouseX, mouseY);
            this.btnRemoveEntry.xPosition = half + ((half / 2) - 22);
            this.btnRemoveEntry.yPosition = y;
            this.btnRemoveEntry.drawButton(GuiEditListEntries.this.mc, mouseX, mouseY);
            
            this.x = x;
            this.y = y;
            this.listWidth = listWidth;
            this.slotHeight = slotHeight;
        }
        
        @Override
        public void drawToolTip(int mouseX, int mouseY)
        {   
            
        }
        
        @Override
        public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.btnAddNewEntryAbove.mousePressed(GuiEditListEntries.this.mc, x, y))
            {
                return true;
            }
            else if (this.btnRemoveEntry.mousePressed(GuiEditListEntries.this.mc, x, y))
            {
                return true;
            }
            
            return false;
        }
        
        @Override
        public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {   
            
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
    
    public interface IGuiEditListEntry extends GuiListExtended.IGuiListEntry
    {
        public void keyTyped(char eventChar, int eventKey);
        
        public void updateCursorCounter();
        
        public void mouseClicked(int x, int y, int mouseEvent);
        
        public void drawToolTip(int mouseX, int mouseY);
    }
}
