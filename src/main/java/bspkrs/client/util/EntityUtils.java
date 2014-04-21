package bspkrs.client.util;

import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.BSLog;

import com.mojang.authlib.GameProfile;

public class EntityUtils
{
    private static boolean erroredOut = false;
    
    public static void resetErroredOut(boolean bol)
    {
        erroredOut = bol;
    }
    
    public static void drawEntityOnScreenAtRotation(int posX, int posY, float scale, float xAngle, float yAngle, EntityLivingBase ent)
    {
        if (!erroredOut)
        {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glTranslatef(posX, posY, 50.0F);
            GL11.glScalef((-scale), scale, scale);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            float f2 = ent.renderYawOffset;
            float f3 = ent.rotationYaw;
            float f4 = ent.rotationPitch;
            float f5 = ent.rotationYawHead;
            GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(xAngle, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yAngle, 0.0F, 1.0F, 0.0F);
            ent.renderYawOffset = (float) Math.atan(2.0F / 40.0F) * 20.0F;
            ent.rotationYaw = (float) Math.atan(2.0F / 40.0F) * 40.0F;
            ent.rotationPitch = -((float) Math.atan(2.0F / 40.0F)) * 20.0F;
            ent.rotationYawHead = ent.renderYawOffset;
            GL11.glTranslatef(0.0F, ent.yOffset, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float viewY = RenderManager.instance.playerViewY;
            try
            {
                RenderManager.instance.playerViewY = 180.0F;
                RenderManager.instance.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                erroredOut = true;
            }
            finally
            {
                GL11.glTranslatef(0.0F, -0.22F, 0.0F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 255.0F * 0.8F, 255.0F * 0.8F);
                RenderManager.instance.playerViewY = viewY;
                ent.renderYawOffset = f2;
                ent.rotationYaw = f3;
                ent.rotationPitch = f4;
                ent.rotationYawHead = f5;
                GL11.glPopMatrix();
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            }
        }
    }
    
    public static void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        if (!erroredOut)
        {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glTranslatef(posX, posY, 50.0F);
            GL11.glScalef((-scale), scale, scale);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            float f2 = ent.renderYawOffset;
            float f3 = ent.rotationYaw;
            float f4 = ent.rotationPitch;
            float f5 = ent.prevRotationYawHead;
            float f6 = ent.rotationYawHead;
            GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
            ent.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
            ent.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
            ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
            ent.rotationYawHead = ent.rotationYaw;
            ent.prevRotationYawHead = ent.rotationYaw;
            GL11.glTranslatef(0.0F, ent.yOffset, 0.0F);
            try
            {
                RenderManager.instance.playerViewY = 180.0F;
                RenderManager.instance.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                erroredOut = true;
            }
            finally
            {
                ent.renderYawOffset = f2;
                ent.rotationYaw = f3;
                ent.rotationPitch = f4;
                ent.prevRotationYawHead = f5;
                ent.rotationYawHead = f6;
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
                GL11.glTranslatef(0.0F, 0.0F, 20F);
                GL11.glPopMatrix();
            }
        }
    }
    
    public static float getEntityScale(EntityLivingBase ent, float baseScale, float targetHeight)
    {
        return (targetHeight / Math.max(Math.max(ent.width, ent.height), ent.ySize)) * baseScale;
    }
    
    public static EntityLivingBase getRandomLivingEntity(World world)
    {
        return getRandomLivingEntity(world, null, 5, null);
    }
    
    public static EntityLivingBase getRandomLivingEntity(World world, List blacklist, int numberOfAttempts, String[] fallBackPlayerNames)
    {
        Random random = new Random();
        Set entities = EntityList.stringToClassMapping.keySet();
        
        if (blacklist != null)
            entities.removeAll(blacklist);
        
        Object[] entStrings = entities.toArray(new Object[] {});
        int id;
        Class clazz;
        
        int tries = 0;
        do
        {
            id = random.nextInt(entStrings.length);
            clazz = (Class) EntityList.stringToClassMapping.get(entStrings[id]);
        }
        while (!EntityLivingBase.class.isAssignableFrom(clazz) && ++tries <= numberOfAttempts);
        
        if (!EntityLivingBase.class.isAssignableFrom(clazz))
        {
            if (fallBackPlayerNames != null)
                return new EntityOtherPlayerMP(world, new GameProfile("", fallBackPlayerNames[random.nextInt(fallBackPlayerNames.length)]));
            else
                return (EntityLivingBase) EntityList.createEntityByName("Chicken", world);
        }
        
        if (bspkrsCoreMod.instance.allowDebugOutput)
            BSLog.info(entStrings[id].toString());
        
        return (EntityLivingBase) EntityList.createEntityByName((String) entStrings[id], world);
    }
}
