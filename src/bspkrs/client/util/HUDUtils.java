package bspkrs.client.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;

import org.lwjgl.opengl.GL11;

public final class HUDUtils
{
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), (y + height), zLevel, ((u + 0) * var7), ((v + height) * var8));
        tessellator.addVertexWithUV((x + width), (y + height), zLevel, ((u + width) * var7), ((v + height) * var8));
        tessellator.addVertexWithUV((x + width), (y + 0), zLevel, ((u + width) * var7), ((v + 0) * var8));
        tessellator.addVertexWithUV((x + 0), (y + 0), zLevel, ((u + 0) * var7), ((v + 0) * var8));
        tessellator.draw();
    }
    
    /**
     * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the specified position.
     */
    public static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y)
    {
        if (itemStack != null)
        {
            if (itemStack.isItemDamaged())
            {
                int var11 = (int) Math.round(13.0D - itemStack.getItemDamageForDisplay() * 13.0D / itemStack.getMaxDamage());
                int var7 = (int) Math.round(255.0D - itemStack.getItemDamageForDisplay() * 255.0D / itemStack.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator var8 = Tessellator.instance;
                int var9 = 255 - var7 << 16 | var7 << 8;
                int var10 = (255 - var7) / 4 << 16 | 16128;
                renderQuad(var8, x + 2, y + 13, 13, 2, 0);
                renderQuad(var8, x + 2, y + 13, 12, 1, var10);
                renderQuad(var8, x + 2, y + 13, var11, 1, var9);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            
            int count = 0;
            
            if (itemStack.getMaxStackSize() > 1)
                count = HUDUtils.countInInventory(ModLoader.getMinecraftInstance().thePlayer, itemStack.itemID, itemStack.getItemDamage());
            else if (itemStack.itemID == Item.bow.itemID)
                count = HUDUtils.countInInventory(ModLoader.getMinecraftInstance().thePlayer, Item.arrow.itemID);
            
            if (count > 1)
            {
                String var6 = "" + count;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                fontRenderer.drawStringWithShadow(var6, x + 19 - 2 - fontRenderer.getStringWidth(var6), y + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
    
    /**
     * Adds a quad to the tesselator at the specified position with the set width and height and color. Args: tessellator, x, y, width,
     * height, color
     */
    public static void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color)
    {
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex((x + 0), (y + 0), 0.0D);
        tessellator.addVertex((x + 0), (y + height), 0.0D);
        tessellator.addVertex((x + width), (y + height), 0.0D);
        tessellator.addVertex((x + width), (y + 0), 0.0D);
        tessellator.draw();
    }
    
    public static int countInInventory(EntityPlayer player, int ID)
    {
        return countInInventory(player, ID, -1);
    }
    
    public static int countInInventory(EntityPlayer player, int ID, int md)
    {
        int count = 0;
        for (int i = 0; i < player.inventory.mainInventory.length; i++)
            if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].itemID == ID && (md == -1 || player.inventory.mainInventory[i].getItemDamage() == md))
                count += player.inventory.mainInventory[i].stackSize;
        return count;
    }
    
    public static String stripCtrl(String s)
    {
        return s.replaceAll("(?i)\247[0-9a-fklmnor]", "");
    }
}
