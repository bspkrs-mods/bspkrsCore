package bspkrs.util;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;

/*
 * @Authors: DaftPVF, bspkrs
 * @Desc: Helpers from original Treecapitator, ConnectedDestruction, and FloatingRuins mod code and new utility methods
 */

public final class CommonUtils
{
    public static boolean isStringAllLettersOrDigits(CharSequence s)
    {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isLetterOrDigit(s.charAt(i)))
                return false;
        return true;
    }

    public static boolean isStringAllLettersOrDigitsOr(CharSequence s, String chrs)
    {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isLetterOrDigit(s.charAt(i)) && !chrs.contains(s.subSequence(i, i + 1)))
                return false;
        return true;
    }

    /*
     * List parser helpers
     */

    public static boolean isItemInList(int id, int md, String list)
    {
        if (list.trim().length() != 0)
        {
            String[] items = list.split(";");
            for (String item : items)
            {
                String[] values = item.split(",");
                if (parseInt(values[0]) == id)
                {
                    if (values.length == 1)
                        return true;
                    else if ((values.length == 2) && (parseInt(values[1]) == md))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean isIDInList(String id, int md, String list)
    {
        String[] itemArray = list.split(";");
        for (int i = 0; i < itemArray.length; i++)
        {
            String[] values = itemArray[i].split(",");

            if ((values.length > 1) && values[0].trim().equals(id) && (parseInt(values[1], -1) == md))
                return true;
            else if (values[0].trim().equals(id))
                return true;
        }
        return false;
    }

    public static boolean isIDInList(int id, int md, String list)
    {
        String[] itemArray = list.split(";");
        for (int i = 0; i < itemArray.length; i++)
        {
            String[] values = itemArray[i].split(",");
            int tempID = parseInt(values[0], Integer.MAX_VALUE);

            if (tempID == Integer.MAX_VALUE)
                continue;

            if ((values.length > 1) && (tempID == id) && (parseInt(values[1], -1) == md))
                return true;
            else if (tempID == id)
                return true;
        }
        return false;
    }

    public static boolean isIDInList(int id, String list)
    {
        return isIDInList(id, -1, list);
    }

    public static int[][][] stringToGroups(String string)
    {
        List<int[][]> groupList = new ArrayList<int[][]>();
        String[] groups = string.split(";");
        for (String group : groups)
        {
            groupList.add(stringToGroup(group));
        }
        int[][][] res = new int[groupList.size()][][];
        for (int i = 0; i < groupList.size(); i++)
        {
            res[i] = groupList.get(i);
        }
        return res;
    }

    public static int[][] stringToGroup(String string)
    {
        List<int[]> blockList = new ArrayList<int[]>();
        String[] blocks = string.split(">");
        for (String block : blocks)
        {
            blockList.add(stringToBlock(block));
        }
        int[][] res = new int[blockList.size()][];
        for (int i = 0; i < blockList.size(); i++)
        {
            res[i] = blockList.get(i);
        }
        return res;
    }

    public static int[] stringToBlock(String string)
    {
        int[] values = new int[] { 0, -1 };
        String[] src = string.split(",");
        if (src.length < 1)
            return values;
        values[0] = parseInt(src[0]);
        if (src.length < 2)
            return values;
        values[1] = parseInt(src[1]);
        return values;
    }

    public static boolean isBlockInGroups(int[] block, int[][][] groups)
    {
        for (int[][] group : groups)
        {
            if (indexOfBlock(block, group) > -1)
                return true;
        }
        return false;
    }

    public static int indexOfBlock(int[] block, int[][] group)
    {
        for (int i = 0; i < group.length; i++)
        {
            if (block[0] == group[i][0])
            {
                if ((group[i][1] == -1) || (block[1] == group[i][1]))
                    return i;
            }
        }
        return -1;
    }

    public static int[][] getRelatedBlocks(int[] block, int[][][] groups)
    {
        List<int[]> blockList = new ArrayList<int[]>();
        for (int[][] group : groups)
        {
            if (indexOfBlock(block, group) > -1)
            {
                for (int i = 0; i < groups.length; i++)
                {
                    if (blockList.contains(group[i]))
                        continue;
                    blockList.add(i, group[i]);
                }
            }
        }
        int[][] secondary = new int[blockList.size()][];
        for (int i = 0; i < blockList.size(); i++)
        {
            secondary[i] = blockList.get(i);
        }
        return secondary;
    }

    public static int smallerBlockIndex(int[] block, int[][][] groups)
    {
        int min = Integer.MAX_VALUE;
        for (int[][] group : groups)
        {
            int i = indexOfBlock(block, group);
            if ((i > -1) && (i < min))
                min = i;
        }
        if (min == Integer.MAX_VALUE)
            min = -1;
        return min;
    }

    public static boolean isMetadataNull(int id, int[][][] groups)
    {
        for (int[][] group : groups)
        {
            for (int[] block : group)
            {
                if ((block[0] == id) && (block[1] == -1))
                    return true;
            }
        }
        return false;
    }

    /*
     * Math helpers
     */

    public static int sqr(int value)
    {
        return value * value;
    }

    public static float sqr(float value)
    {
        return value * value;
    }

    public static int parseInt(String string)
    {
        return parseInt(string, 0);
    }

    public static int parseInt(String string, int defaultValue)
    {
        try
        {
            return Integer.parseInt(string.trim());
        }
        catch (NumberFormatException ex)
        {
            return defaultValue;
        }
    }

    /*
     * General Minecraft methods
     */

    public void playAtPitch(int i, World world, EntityPlayer entityplayer)
    {
        float f = (float) Math.pow(2D, (i - 12) / 12D);
        world.playSound(entityplayer, entityplayer.getPosition(), SoundEvents.BLOCK_NOTE_PLING, SoundCategory.getByName("block"), 0.5F, f);
    }

    public static boolean moveBlock(World world, BlockPos src, BlockPos tgt, boolean allowBlockReplacement)
    {
        return moveBlock(world, src, tgt, allowBlockReplacement, BlockNotifyType.ALL);
    }

    public static boolean moveBlock(World world, BlockPos src, BlockPos tgt, boolean allowBlockReplacement, int notifyFlag)
    {
        if (!world.isRemote && !world.isAirBlock(src) && (world.isAirBlock(tgt) || allowBlockReplacement))
        {
            IBlockState state = world.getBlockState(src);
    
            world.setBlockState(tgt, state, notifyFlag);
    
            TileEntity te = world.getTileEntity(src);
            if (te != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
    
                nbt.setInteger("x", tgt.getX());
                nbt.setInteger("y", tgt.getY());
                nbt.setInteger("z", tgt.getZ());
    
                te = world.getTileEntity(tgt);
                if (te != null)
                    te.readFromNBT(nbt);
    
                world.removeTileEntity(src);
            }
    
            world.setBlockToAir(src);
            return true;
        }
        return false;
    }

    public static int getHighestGroundBlock(World world, BlockPos pos)
    {
        while ((pos.getY() > 0) && (world.isAirBlock(pos) || !world.isBlockNormalCube(pos, true)
                || world.getBlockState(pos).getBlock().isWood(world, pos)))
        {
            pos = pos.down();
        }
        return pos.getY();
    }

    public static int getFirstNonAirBlockFromTop(World world, BlockPos pos)
    {
        int y;
        int x = pos.getX();
        int z = pos.getZ();
        for (y = world.getActualHeight(); world.isAirBlock(new BlockPos(x, y - 1, z)) && (y > 0); y--)
        {}
        return y;
    }

    public static int getSphericalDistance(BlockPos startPos, BlockPos endPos)
    {
        return (int) Math.round(Math.sqrt(CommonUtils.sqr(endPos.getX() - startPos.getX())
                + CommonUtils.sqr(endPos.getZ() - startPos.getZ()) + CommonUtils.sqr(endPos.getY() - startPos.getY())));
    }

    public static int getCubicDistance(BlockPos startPos, BlockPos endPos)
    {
        return Math.abs(endPos.getX() - startPos.getX()) + Math.abs(endPos.getY() - startPos.getY()) + Math.abs(endPos.getZ() - startPos.getZ());
    }

    public static int getHorSquaredDistance(BlockPos pos, BlockPos pos2)
    {
        return Math.abs(pos2.getX() - pos.getX()) + Math.abs(pos2.getZ() - pos.getZ());
    }

    public static int getVerDistance(BlockPos startPos, BlockPos endPos)
    {
        return Math.abs(endPos.getY() - startPos.getY());
    }

    public static double getDistanceRatioToCenter(int point1, int point2, int pos)
    {
        double radius = Math.abs(point2 - point1) / 2D;
        double dar = Math.abs(Math.abs(pos - point1) - radius);
        return radius != 0.0D ? dar / radius : 0.0D;
    }

    public static void setHugeMushroom(World world, Random random, BlockPos pos, IBlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte w = 3;
        byte cw = 4;
        byte h = 4;
        fillWithBlocks(world, new BlockPos((x - w) + 1, y, z - w), new BlockPos((x + w) - 1, (y + h) - 1, z - w), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos((x - w) + 1, y, z + w), new BlockPos((x + w) - 1, (y + h) - 1, z + w), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos(x - w, y, (z - w) + 1), new BlockPos(x - w, (y + h) - 1, (z + w) - 1), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos(x + w, y, (z - w) + 1), new BlockPos(x + w, (y + h) - 1, (z + w) - 1), state.getBlock().getStateFromMeta(10));
        fillWithBlocksRounded(world, new BlockPos(x - cw, y + h, z - cw), new BlockPos(x + cw, y + h, z + cw), state.getBlock().getStateFromMeta(14));
    }

    public static void fillWithBlocks(World world, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        for (int x = pos1.getX(); x <= pos2.getX(); x++)
            for (int y = pos1.getY(); y <= pos2.getY(); y++)
                for (int z = pos1.getZ(); z <= pos2.getZ(); z++)
                    world.setBlockState(new BlockPos(x, y, z), state, 3);
    }

    public static void fillWithBlocksRounded(World world, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        for (int x = pos1.getX(); x <= pos2.getX(); x++)
            for (int y = pos1.getY(); y <= pos2.getY(); y++)
                for (int z = pos1.getZ(); z <= pos2.getZ(); z++)
                {
                    double xd = getDistanceRatioToCenter(pos1.getX(), pos2.getX(), x);
                    double yd = getDistanceRatioToCenter(pos1.getY(), pos2.getY(), y);
                    double zd = getDistanceRatioToCenter(pos1.getZ(), pos2.getZ(), z);
                    if (((xd * xd) + (yd * yd) + (zd * zd)) <= 1.5D)
                        world.setBlockState(new BlockPos(x, y, z), state, 3);
                }
    }

    public static boolean isGamePaused(Minecraft mc)
    {
        return (mc.currentScreen != null) && (mc.currentScreen.doesGuiPauseGame() || (mc.currentScreen instanceof GuiMainMenu));
    }

    public static String getMCTimeString(long worldTime, long fmt)
    {
        long HH = (int) (((worldTime / 1000L) + 6) % 24L);
        int MM = (int) (((worldTime % 1000L) / 1000.0D) * 60.0D);
        boolean am = HH < 12;
        HH = HH % fmt;
        String hour = "";

        if (fmt == 24L)
            hour = (HH < 10 ? "0" : "") + String.valueOf(HH);
        else
            hour = String.valueOf(HH == 0 ? 12 : HH);

        String min = (MM < 10 ? "0" : "") + String.valueOf(MM);
        return hour + ":" + min + (fmt == 12L ? (am ? "AM" : "PM") : "");
    }

    public static String ticksToTimeString(long ticks)
    {
        long secs = ticks / 20L;
        long mins = secs / 60L;
        long hrs = mins / 60L;
        long days = hrs / 24L;

        String time = "";

        if (days > 0L)
            time = days + ":";

        if (hrs > 0L)
            time = time + (((hrs % 24L) < 10L) && (days > 0L) ? "0" : "") + (hrs % 24L) + ":";

        if (mins > 0L)
            time = time + (((mins % 60L) < 10L) && (hrs > 0L) ? "0" : "") + (mins % 60L) + ":";
        else
            time = time + "0:";

        time = time + ((secs % 60L) < 10L ? "0" : "") + (secs % 60L);
        return time;
    }

    public static String stringArrayToString(String[] sa)
    {
        return stringArrayToString(sa, "#");
    }

    public static String stringArrayToString(String[] sa, String separator)
    {
        String ret = "";
        for (String s : sa)
            ret += separator + " " + s;

        return ret.replaceFirst(separator + " ", "");
    }

    public static String[] loadTextFromURL(URL url, Logger logger)
    {
        return loadTextFromURL(url, logger, new String[] { "" }, 0);
    }

    public static String[] loadTextFromURL(URL url, Logger logger, int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] { "" }, timeoutMS);
    }

    public static String[] loadTextFromURL(URL url, Logger logger, String defaultValue)
    {
        return loadTextFromURL(url, logger, new String[] { defaultValue }, 0);
    }

    public static String[] loadTextFromURL(URL url, Logger logger, String defaultValue, int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] { defaultValue }, timeoutMS);
    }

    public static String[] loadTextFromURL(URL url, Logger logger, String[] defaultValue)
    {
        return loadTextFromURL(url, logger, defaultValue, 0);
    }

    public static String[] loadTextFromURL(URL url, Logger logger, String[] defaultValue, int timeoutMS)
    {
        List<String> arraylist = new ArrayList<String>();
        Scanner scanner = null;
        try
        {
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(timeoutMS);
            uc.setConnectTimeout(timeoutMS);
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            scanner = new Scanner(uc.getInputStream(), "UTF-8");
        }
        catch (Throwable e)
        {
            logger.log(Level.WARN, String.format("Error retrieving remote string value at URL %s! Defaulting to %s", url.toString(), stringArrayToString(defaultValue)));
            if (bspkrsCoreMod.instance.allowDebugOutput)
                e.printStackTrace();
            if (scanner != null)
                scanner.close();
            return defaultValue;
        }

        while (scanner.hasNextLine())
        {
            arraylist.add(scanner.nextLine());
        }
        scanner.close();
        return arraylist.toArray(new String[arraylist.size()]);
    }

    public static String getLogFileName()
    {
        try
        {
            Minecraft.getMinecraft();
            return "ForgeModLoader-client-0.log";
        }
        catch (Throwable e)
        {
            return "ForgeModLoader-server-0.log";
        }
    }

    public static String getMinecraftDir()
    {
        try
        {
            return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
        }
        catch (NoClassDefFoundError e)
        {
//TODO: find an alternative static way to get a handle on MinecraftServer 
// this is not a static reference anymore
//            return MinecraftServer.getServer().getFile("").getAbsolutePath();
// just throw it
        	throw e;
        }
    }

    public static String getConfigDir()
    {
        File configDir = new File(getMinecraftDir(), "config");
        return configDir.getAbsolutePath();
    }

    public static boolean isObfuscatedEnv()
    {
        try
        {
            Blocks.class.getField("air");
            return false;
        }
        catch (Throwable e)
        {
            return true;
        }
    }

    public static RayTraceResult getPlayerLookingSpot(EntityPlayer player, boolean restrict)
    {
        float scale = 1.0F;
        float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * scale);
        float yaw = player.prevRotationYaw + ((player.rotationYaw - player.prevRotationYaw) * scale);
        double x = player.prevPosX + ((player.posX - player.prevPosX) * scale);
        double y = (player.prevPosY + ((player.posY - player.prevPosY) * scale) + 1.62D);
        double z = player.prevPosZ + ((player.posZ - player.prevPosZ) * scale);
        Vec3d vector1 = new Vec3d(x, y, z);
        float cosYaw = MathHelper.cos((-yaw * 0.017453292F) - (float) Math.PI);
        float sinYaw = MathHelper.sin((-yaw * 0.017453292F) - (float) Math.PI);
        float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
        float sinPitch = MathHelper.sin(-pitch * 0.017453292F);
        float pitchAdjustedSinYaw = sinYaw * cosPitch;
        float pitchAdjustedCosYaw = cosYaw * cosPitch;
        double distance = 500D;
        if ((player instanceof EntityPlayerMP) && restrict)
        {
            distance = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }
        Vec3d vector2 = vector1.addVector(pitchAdjustedSinYaw * distance, sinPitch * distance, pitchAdjustedCosYaw * distance);
        return player.worldObj.rayTraceBlocks(vector1, vector2);
    }

    public static void spawnExplosionParticleAtEntity(Entity entity)
    {
        for (int i = 0; i < 20; ++i)
        {
            double d0 = entity.worldObj.rand.nextGaussian() * 0.02D;
            double d1 = entity.worldObj.rand.nextGaussian() * 0.02D;
            double d2 = entity.worldObj.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            entity.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (entity.posX + (entity.worldObj.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d0 * d3), (entity.posY + (entity.worldObj.rand.nextFloat() * entity.height)) - (d1 * d3), (entity.posZ + (entity.worldObj.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d2 * d3), d0, d1, d2);
        }
    }
}
