package bspkrs.bspkrscore.fml;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.input.Mouse;

import bspkrs.client.util.EntityUtils;
import bspkrs.util.BSLog;
import bspkrs.util.FakeWorld;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BSMainMenuRenderTicker
{
    private Minecraft          mcClient;
    private static boolean     isRegistered = false;
    private World              world;
    private EntityLivingBase   player;
    private EntityLivingBase   randMob;
    private GuiScreen          savedScreen;
    private static List        entityBlacklist;
    private static String[]    fallBackPlayerNames;
    private static ItemStack[] playerItems;
    private static ItemStack[] zombieItems;
    private static ItemStack[] skelItems;
    private static Random      random       = new Random();
    
    private static Set         entities;
    private static Object[]    entStrings;
    private static int         id;
    
    static
    {
        entityBlacklist = new ArrayList();
        entityBlacklist.add("Mob");
        entityBlacklist.add("Monster");
        entityBlacklist.add("EnderDragon");
        entityBlacklist.add("Squid");
        entityBlacklist.add("Ghast");
        entityBlacklist.add("Bat");
        entityBlacklist.add("CaveSpider");
        entityBlacklist.add("Giant");
        entityBlacklist.add("BiomesOPlenty.Phantom");
        entityBlacklist.add("Forestry.butterflyGE");
        entityBlacklist.add("TConstruct.Crystal");
        entityBlacklist.add("Thaumcraft.Firebat");
        entityBlacklist.add("Thaumcraft.TaintSpore");
        entityBlacklist.add("Thaumcraft.TaintSwarm");
        entityBlacklist.add("Thaumcraft.Taintacle");
        entityBlacklist.add("Thaumcraft.TaintacleTiny");
        entityBlacklist.add("Thaumcraft.Wisp");
        entityBlacklist.add("TwilightForest.Boggard");
        entityBlacklist.add("TwilightForest.Firefly");
        entityBlacklist.add("TwilightForest.Helmet Crab");
        entityBlacklist.add("TwilightForest.Hydra");
        entityBlacklist.add("TwilightForest.HydraHead");
        entityBlacklist.add("TwilightForest.Lower Goblin Knight");
        entityBlacklist.add("TwilightForest.Mist Wolf");
        entityBlacklist.add("TwilightForest.Mosquito Swarm");
        entityBlacklist.add("TwilightForest.Upper Goblin Knight");
        
        fallBackPlayerNames = new String[] {
            "bspkrs", "lorddusk", "Arkember", "TTFTCUTS", "WayofFlowingTime", "Grumm", "Sacheverell", "Quetzz", "Pahimar", "ZeldoKavira",
            "sfPlayer1", "jadedcat", "RWTema", "Scottwears", "neptunepink", "Aureylian", "direwolf20", "Krystal_Raven", "notch", "Dinnerbone",
            "Adubbz", "AlgorithmX2", "Cloudhunter", "Lunatrius"
        };
        
        playerItems = new ItemStack[] {
            new ItemStack(Items.iron_sword), new ItemStack(Items.diamond_sword), new ItemStack(Items.golden_sword),
            new ItemStack(Items.diamond_pickaxe), new ItemStack(Items.iron_pickaxe), new ItemStack(Items.iron_axe)
        };
        
        zombieItems = new ItemStack[] {
            new ItemStack(Items.iron_sword), new ItemStack(Items.diamond_sword), new ItemStack(Items.golden_sword), new ItemStack(Items.iron_axe)
        };
        
        skelItems = new ItemStack[] {
            new ItemStack(Items.bow), new ItemStack(Items.golden_sword), new ItemStack(Items.bow),
            new ItemStack(Items.bow), new ItemStack(Items.bow), new ItemStack(Items.bow)
        };
        
        entities = EntityList.stringToClassMapping.keySet();
        entities.removeAll(entityBlacklist);
        entStrings = entities.toArray(new Object[] {});
        id = -1;
    }
    
    public BSMainMenuRenderTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
        init();
    }
    
    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (bspkrsCoreMod.instance.showMainMenuMobs && (world == null || player == null || randMob == null))
            init();
        
        if (bspkrsCoreMod.instance.showMainMenuMobs && mcClient.currentScreen instanceof GuiMainMenu && world != null && player != null && randMob != null)
        {
            ScaledResolution sr = new ScaledResolution(mcClient.gameSettings, mcClient.displayWidth, mcClient.displayHeight);
            final int mouseX = Mouse.getX() * sr.getScaledWidth() / mcClient.displayWidth;
            final int mouseY = sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / mcClient.displayHeight - 1;
            int distanceToSide = ((mcClient.currentScreen.width / 2) - 98) / 2;
            float targetHeight = (float) (sr.getScaledHeight_double() / 5.0F) / 1.8F;
            float scale = EntityUtils.getEntityScale(randMob, targetHeight, 1.8F);
            EntityUtils.drawEntityOnScreen(
                    distanceToSide,
                    (int) (sr.getScaledHeight() / 2 + (randMob.height * scale)),
                    scale,
                    distanceToSide - mouseX,
                    (sr.getScaledHeight() / 2 + (randMob.height * scale)) - (randMob.height * scale * (randMob.getEyeHeight() / randMob.height)) - mouseY,
                    randMob);
            EntityUtils.drawEntityOnScreen(
                    sr.getScaledWidth() - distanceToSide,
                    (int) (sr.getScaledHeight() / 2 + (player.height * targetHeight)),
                    targetHeight,
                    sr.getScaledWidth() - distanceToSide - mouseX,
                    (sr.getScaledHeight() / 2 + (player.height * targetHeight)) - (player.height * targetHeight * (player.getEyeHeight() / player.height)) - mouseY,
                    player);
        }
        else if (world != null && (savedScreen == null || !savedScreen.equals(mcClient.currentScreen)))
        {
            if (bspkrsCoreMod.instance.allowDebugOutput)
                randMob = getNextEntity(world);
            else
                randMob = EntityUtils.getRandomLivingEntity(world, entityBlacklist, 4, fallBackPlayerNames);
            setRandomMobItem(player);
            setRandomMobItem(randMob);
            savedScreen = mcClient.currentScreen;
        }
    }
    
    private void init()
    {
        try
        {
            world = new FakeWorld();
            player = new EntityOtherPlayerMP(world, new GameProfile("", mcClient.getSession().getUsername()));
            setRandomMobItem(player);
            if (bspkrsCoreMod.instance.allowDebugOutput)
                randMob = getNextEntity(world);
            else
                randMob = EntityUtils.getRandomLivingEntity(world, entityBlacklist, 4, fallBackPlayerNames);
            setRandomMobItem(randMob);
            RenderManager.instance.cacheActiveRenderInfo(world, mcClient.renderEngine, mcClient.fontRenderer, player, player, mcClient.gameSettings, 0.0F);
            savedScreen = mcClient.currentScreen;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            world = null;
            player = null;
        }
    }
    
    private static EntityLivingBase getNextEntity(World world)
    {
        Class clazz;
        int tries = 0;
        do
        {
            if (++id >= entStrings.length)
                id = 0;
            clazz = (Class) EntityList.stringToClassMapping.get(entStrings[id]);
        }
        while (!EntityLivingBase.class.isAssignableFrom(clazz) && ++tries <= 5);
        
        if (!EntityLivingBase.class.isAssignableFrom(clazz))
            return new EntityOtherPlayerMP(world, new GameProfile("", fallBackPlayerNames[random.nextInt(fallBackPlayerNames.length)]));
        
        if (bspkrsCoreMod.instance.allowDebugOutput)
            BSLog.info(entStrings[id].toString());
        
        return (EntityLivingBase) EntityList.createEntityByName((String) entStrings[id], world);
    }
    
    private static void setRandomMobItem(EntityLivingBase ent)
    {
        if (ent instanceof EntityOtherPlayerMP)
            ent.setCurrentItemOrArmor(0, playerItems[random.nextInt(playerItems.length)]);
        else if (ent instanceof EntityZombie)
            ent.setCurrentItemOrArmor(0, zombieItems[random.nextInt(zombieItems.length)]);
        else if (ent instanceof EntitySkeleton)
        {
            if (random.nextBoolean())
            {
                ((EntitySkeleton) ent).setSkeletonType(1);
                ent.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            }
            else
                ent.setCurrentItemOrArmor(0, skelItems[random.nextInt(skelItems.length)]);
        }
        else if (ent instanceof EntityPigZombie)
            ent.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
        else if (ent instanceof EntityEnderman)
            ((EntityEnderman) ent).func_146081_a(Blocks.grass);
    }
    
    public void register()
    {
        if (!isRegistered)
        {
            FMLCommonHandler.instance().bus().register(this);
            isRegistered = true;
        }
    }
    
    public void unRegister()
    {
        if (isRegistered)
        {
            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }
    
    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
