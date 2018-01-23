package bspkrs.client.util;

import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import bspkrs.bspkrscore.fml.*;
import bspkrs.util.*;
import com.mojang.authlib.*;
import net.minecraft.client.entity.*;
import java.util.*;

public class EntityUtils
{
    public static float getModelSize(final EntityLivingBase ent)
    {
        final Render<EntityLivingBase> render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject((EntityLivingBase)ent);
        if(render instanceof RenderLivingBase)
        {
            @SuppressWarnings("unused")
            final RenderLivingBase<EntityLivingBase> entRender = (RenderLivingBase<EntityLivingBase>)render;
        }
        return 1.8f;
    }

    public static void drawEntityOnScreen(final int posX, final int posY, final float scale, final float mouseX, final float mouseY, final EntityLivingBase ent)
    {
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.translate((float)posX, (float)posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f2 = ent.renderYawOffset;
        final float f3 = ent.rotationYaw;
        final float f4 = ent.rotationPitch;
        final float f5 = ent.prevRotationYawHead;
        final float f6 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -(float)Math.atan(mouseY / 40.0f) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        try
        {
            final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setPlayerViewY(180.0f);
            rendermanager.setRenderShadow(false);
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
            rendermanager.setRenderShadow(true);
        }
        finally
        {
            ent.renderYawOffset = f2;
            ent.rotationYaw = f3;
            ent.rotationPitch = f4;
            ent.prevRotationYawHead = f5;
            ent.rotationYawHead = f6;
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.translate(0.0f, 0.0f, 20.0f);
        }
    }

    public static float getEntityScale(final EntityLivingBase ent, final float baseScale, final float targetHeight)
    {
        return targetHeight / Math.max(ent.width, ent.height) * baseScale;
    }

    public static EntityLivingBase getRandomLivingEntity(final World world)
    {
        return getRandomLivingEntity(world, null, 5, null);
    }

    public static EntityLivingBase getRandomLivingEntity(final World world, final List<?> blacklist, final int numberOfAttempts, final List<AbstractMap.SimpleEntry<UUID, String>> fallbackPlayerNames)
    {
        final Random random = new Random();
        final Set<?> entities = new TreeSet<Object>(EntityList.getEntityNameList());
        if(blacklist != null)
        {
            entities.removeAll(blacklist);
        }
        final Object[] entStrings = entities.toArray(new Object[0]);
        int tries = 0;
        Class<?> clazz;
        int id;
        do
        {
            id = random.nextInt(entStrings.length);
            clazz = EntityList.getClassFromID(id);
        }
        while(!EntityLivingBase.class.isAssignableFrom(clazz) && ++tries <= numberOfAttempts);
        if(EntityLivingBase.class.isAssignableFrom(clazz))
        {
            if(bspkrsCoreMod.INSTANCE.allowDebugOutput)
            {
                BSLog.info(entStrings[id].toString(), new Object[0]);
            }
            return (EntityLivingBase)EntityList.createEntityByIDFromName(new ResourceLocation("Chicken"), world);
        }
        if(fallbackPlayerNames != null)
        {
            final AbstractMap.SimpleEntry<UUID, String> entry = fallbackPlayerNames.get(random.nextInt(fallbackPlayerNames.size()));
            return (EntityLivingBase)new EntityOtherPlayerMP(world, Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile((UUID)entry.getKey(), (String)entry.getValue()), true));
        }
        return (EntityLivingBase)EntityList.createEntityByID(id, world);
    }
}
