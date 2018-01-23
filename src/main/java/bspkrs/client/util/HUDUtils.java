package bspkrs.client.util;

import net.minecraft.util.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;

public final class HUDUtils
{
    private static int[] colorCodes;

    public static int getColorCode(final char c, final boolean isLighter)
    {
        return HUDUtils.colorCodes[isLighter ? "0123456789abcdef".indexOf(c) : ("0123456789abcdef".indexOf(c) + 16)];
    }

    public static void drawContinuousTexturedBox(final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int borderSize, final float zLevel)
    {
        drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    public static void drawContinuousTexturedBox(final ResourceLocation res, final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int borderSize, final float zLevel)
    {
        drawContinuousTexturedBox(res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }

    public static void drawContinuousTexturedBox(final ResourceLocation res, final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int topBorder, final int bottomBorder, final int leftBorder, final int rightBorder, final float zLevel)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    public static void drawContinuousTexturedBox(final int x, final int y, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight, final int topBorder, final int bottomBorder, final int leftBorder, final int rightBorder, final float zLevel)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        final int fillerWidth = textureWidth - leftBorder - rightBorder;
        final int fillerHeight = textureHeight - topBorder - bottomBorder;
        final int canvasWidth = width - leftBorder - rightBorder;
        final int canvasHeight = height - topBorder - bottomBorder;
        final int xPasses = canvasWidth / fillerWidth;
        final int remainderWidth = canvasWidth % fillerWidth;
        final int yPasses = canvasHeight / fillerHeight;
        final int remainderHeight = canvasHeight % fillerHeight;
        drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);
        for(int i = 0; i < xPasses + ((remainderWidth > 0) ? 1 : 0); ++i)
        {
            drawTexturedModalRect(x + leftBorder + i * fillerWidth, y, u + leftBorder, v, (i == xPasses) ? remainderWidth : fillerWidth, topBorder, zLevel);
            drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses) ? remainderWidth : fillerWidth, bottomBorder, zLevel);
            for(int j = 0; j < yPasses + ((remainderHeight > 0) ? 1 : 0); ++j)
            {
                drawTexturedModalRect(x + leftBorder + i * fillerWidth, y + topBorder + j * fillerHeight, u + leftBorder, v + topBorder, (i == xPasses) ? remainderWidth : fillerWidth, (j == yPasses) ? remainderHeight : fillerHeight, zLevel);
            }
        }
        for(int k = 0; k < yPasses + ((remainderHeight > 0) ? 1 : 0); ++k)
        {
            drawTexturedModalRect(x, y + topBorder + k * fillerHeight, u, v + topBorder, leftBorder, (k == yPasses) ? remainderHeight : fillerHeight, zLevel);
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + k * fillerHeight, u + leftBorder + fillerWidth, v + topBorder, rightBorder, (k == yPasses) ? remainderHeight : fillerHeight, zLevel);
        }
    }

    public static void drawTexturedModalRect(final int x, final int y, final int u, final int v, final int width, final int height, final float zLevel)
    {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((u + 0) * var7), (double)((v + height) * var8)).endVertex();
        worldRenderer.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((u + width) * var7), (double)((v + height) * var8)).endVertex();
        worldRenderer.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((u + width) * var7), (double)((v + 0) * var8)).endVertex();
        worldRenderer.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((u + 0) * var7), (double)((v + 0) * var8)).endVertex();
        tessellator.draw();
    }

    public static void renderItemOverlayIntoGUI(final FontRenderer fontRenderer, final ItemStack itemStack, final int x, final int y)
    {
        renderItemOverlayIntoGUI(fontRenderer, itemStack, x, y, true, true);
    }

    public static void renderItemOverlayIntoGUI(final FontRenderer fontRenderer, final ItemStack itemStack, final int x, final int y, final boolean showDamageBar, final boolean showCount)
    {
        if(itemStack != null && (showDamageBar || showCount))
        {
            if(itemStack.isItemDamaged() && showDamageBar)
            {
                final int var11 = (int)Math.round(13.0 - itemStack.getItemDamage() * 13.0 / itemStack.getMaxDamage());
                final int var12 = (int)Math.round(255.0 - itemStack.getItemDamage() * 255.0 / itemStack.getMaxDamage());
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                final Tessellator var13 = Tessellator.getInstance();
                final int var14 = 255 - var12 << 16 | var12 << 8;
                final int var15 = (255 - var12) / 4 << 16 | 0x3F00;
                renderQuad(var13, x + 2, y + 13, 13, 2, 0);
                renderQuad(var13, x + 2, y + 13, 12, 1, var15);
                renderQuad(var13, x + 2, y + 13, var11, 1, var14);
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            if(showCount)
            {
                int count = 0;
                if(itemStack.getMaxStackSize() > 1)
                {
                    count = countInInventory((EntityPlayer)Minecraft.getMinecraft().player, itemStack.getItem(), itemStack.getItemDamage());
                }
                else if(itemStack.getItem().equals(Items.BOW))
                {
                    count = countInInventory((EntityPlayer)Minecraft.getMinecraft().player, Items.ARROW);
                }
                if(count > 1)
                {
                    final String var16 = "" + count;
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    GlStateManager.disableBlend();
                    fontRenderer.drawStringWithShadow(var16, (float)(x + 19 - 2 - fontRenderer.getStringWidth(var16)), (float)(y + 6 + 3), 16777215);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                }
            }
        }
    }

    public static void renderQuad(final Tessellator tessellator, final int x, final int y, final int width, final int height, final int color)
    {
        final BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.putColor4(color);
        worldRenderer.pos((double)(x + 0), (double)(y + 0), 0.0).endVertex();
        worldRenderer.pos((double)(x + 0), (double)(y + height), 0.0).endVertex();
        worldRenderer.pos((double)(x + width), (double)(y + height), 0.0).endVertex();
        worldRenderer.pos((double)(x + width), (double)(y + 0), 0.0).endVertex();
        tessellator.draw();
    }

    public static int countInInventory(final EntityPlayer player, final Item item)
    {
        return countInInventory(player, item, -1);
    }

    public static int countInInventory(final EntityPlayer player, final Item item, final int md)
    {
        int count = 0;
        for(int i = 0; i < player.inventory.mainInventory.size(); ++i)
        {
            if(player.inventory.mainInventory.get(i) != null && item.equals(player.inventory.mainInventory.get(i).getItem()) && (md == -1 || player.inventory.mainInventory.get(i).getMetadata() == md))
            {
                count += player.inventory.mainInventory.get(i).getCount();
            }
        }
        return count;
    }

    public static String stripCtrl(final String s)
    {
        return s.replaceAll("(?i)�[0-9a-fklmnor]", "");
    }

    static
    {
        HUDUtils.colorCodes = new int[] {0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215, 0, 42, 10752, 10794, 2752512, 2752554, 2763264, 2763306, 1381653, 1381695, 1392405, 1392447, 4134165, 4134207, 4144917, 4144959};
    }
}
