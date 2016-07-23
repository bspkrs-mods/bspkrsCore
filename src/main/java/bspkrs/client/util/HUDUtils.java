package bspkrs.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public final class HUDUtils
{
    private static int[] colorCodes = new int[] { 0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215,
                                    0, 42, 10752, 10794, 2752512, 2752554, 2763264, 2763306, 1381653, 1381695, 1392405, 1392447, 4134165, 4134207, 4144917, 4144959 };

    public static int getColorCode(char c, boolean isLighter)
    {
        return colorCodes[isLighter ? "0123456789abcdef".indexOf(c) : "0123456789abcdef".indexOf(c) + 16];
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param borderSize the size of the box's borders
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int borderSize, float zLevel)
    {
        drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. The provided ResourceLocation object will be bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param res the ResourceLocation object that contains the desired image
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param borderSize the size of the box's borders
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int borderSize, float zLevel)
    {
        drawContinuousTexturedBox(res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. The provided ResourceLocation object will be bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param res the ResourceLocation object that contains the desired image
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Border
        // Top Left
        drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < (xPasses + (remainderWidth > 0 ? 1 : 0)); i++)
        {
            // Top Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < (yPasses + (remainderHeight > 0 ? 1 : 0)); j++)
                drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }

        // Side Borders
        for (int j = 0; j < (yPasses + (remainderHeight > 0 ? 1 : 0)); j++)
        {
            // Left Border
            drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos((x + 0), (y + height), zLevel).tex(((u + 0) * var7), ((v + height) * var8)).endVertex();
        worldRenderer.pos((x + width), (y + height), zLevel).tex(((u + width) * var7), ((v + height) * var8)).endVertex();
        worldRenderer.pos((x + width), (y + 0), zLevel).tex(((u + width) * var7), ((v + 0) * var8)).endVertex();
        worldRenderer.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * var7), ((v + 0) * var8)).endVertex();
        tessellator.draw();
    }

    /**
     * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the specified position.
     */
    public static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y)
    {
        renderItemOverlayIntoGUI(fontRenderer, itemStack, x, y, true, true);
    }

    /**
     * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the specified position.
     */
    public static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y, boolean showDamageBar, boolean showCount)
    {
        if ((itemStack != null) && (showDamageBar || showCount))
        {
            if (itemStack.isItemDamaged() && showDamageBar)
            {
                int var11 = (int) Math.round(13.0D - ((itemStack.getItemDamage() * 13.0D) / itemStack.getMaxDamage()));
                int var7 = (int) Math.round(255.0D - ((itemStack.getItemDamage() * 255.0D) / itemStack.getMaxDamage()));
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator var8 = Tessellator.getInstance();
                int var9 = ((255 - var7) << 16) | (var7 << 8);
                int var10 = (((255 - var7) / 4) << 16) | 16128;
                renderQuad(var8, x + 2, y + 13, 13, 2, 0);
                renderQuad(var8, x + 2, y + 13, 12, 1, var10);
                renderQuad(var8, x + 2, y + 13, var11, 1, var9);
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (showCount)
            {
                int count = 0;

                if (itemStack.getMaxStackSize() > 1)
                    count = HUDUtils.countInInventory(Minecraft.getMinecraft().thePlayer, itemStack.getItem(), itemStack.getItemDamage());
                else if (itemStack.getItem().equals(Items.BOW))
                    count = HUDUtils.countInInventory(Minecraft.getMinecraft().thePlayer, Items.ARROW);

                if (count > 1)
                {
                    String var6 = "" + count;
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    GlStateManager.disableBlend();
                    fontRenderer.drawStringWithShadow(var6, (x + 19) - 2 - fontRenderer.getStringWidth(var6), y + 6 + 3, 16777215);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                }
            }
        }
    }

    /**
     * Adds a quad to the tesselator at the specified position with the set width and height and color. Args: tessellator, x, y, width,
     * height, color
     */
    public static void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color)
    {
        VertexBuffer worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        worldRenderer.pos(x + 0, y + 0, 0.0D).color(r, g, b, a).endVertex();
        worldRenderer.pos(x + 0, y + height, 0.0D).color(r, g, b, a).endVertex();
        worldRenderer.pos(x + width, y + height, 0.0D).color(r, g, b, a).endVertex();
        worldRenderer.pos(x + width, y + 0, 0.0D).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    public static int countInInventory(EntityPlayer player, Item item)
    {
        return countInInventory(player, item, -1);
    }

    public static int countInInventory(EntityPlayer player, Item item, int md)
    {
        int count = 0;
        for (int i = 0; i < player.inventory.mainInventory.length; i++)
            if ((player.inventory.mainInventory[i] != null) && item.equals(player.inventory.mainInventory[i].getItem()) && ((md == -1) || (player.inventory.mainInventory[i].getMetadata() == md)))
                count += player.inventory.mainInventory[i].stackSize;
        return count;
    }

    public static String stripCtrl(String s)
    {
        return s.replaceAll("(?i)\247[0-9a-fklmnor]", "");
    }
}
