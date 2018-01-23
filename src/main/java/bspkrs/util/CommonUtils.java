package bspkrs.util;

import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import java.util.*;
import org.apache.logging.log4j.*;
import bspkrs.bspkrscore.fml.*;
import java.net.*;
import java.io.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public final class CommonUtils
{
    public static boolean isStringAllLettersOrDigits(final CharSequence s)
    {
        for(int i = 0; i < s.length(); ++i)
        {
            if(!Character.isLetterOrDigit(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isStringAllLettersOrDigitsOr(final CharSequence s, final String chrs)
    {
        for(int i = 0; i < s.length(); ++i)
        {
            if(!Character.isLetterOrDigit(s.charAt(i)) && !chrs.contains(s.subSequence(i, i + 1)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isItemInList(final int id, final int md, final String list)
    {
        if(list.trim().length() != 0)
        {
            final String[] items = list.split(";");
            for(final String item : items)
            {
                final String[] values = item.split(",");
                if(parseInt(values[0]) == id)
                {
                    if(values.length == 1)
                    {
                        return true;
                    }
                    if(values.length == 2 && parseInt(values[1]) == md)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isIDInList(final String id, final int md, final String list)
    {
        final String[] itemArray = list.split(";");
        for(int i = 0; i < itemArray.length; ++i)
        {
            final String[] values = itemArray[i].split(",");
            if(values.length > 1 && values[0].trim().equals(id) && parseInt(values[1], -1) == md)
            {
                return true;
            }
            if(values[0].trim().equals(id))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isIDInList(final int id, final int md, final String list)
    {
        final String[] itemArray = list.split(";");
        for(int i = 0; i < itemArray.length; ++i)
        {
            final String[] values = itemArray[i].split(",");
            final int tempID = parseInt(values[0], Integer.MAX_VALUE);
            if(tempID != Integer.MAX_VALUE)
            {
                if(values.length > 1 && tempID == id && parseInt(values[1], -1) == md)
                {
                    return true;
                }
                if(tempID == id)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isIDInList(final int id, final String list)
    {
        return isIDInList(id, -1, list);
    }

    public static int[][][] stringToGroups(final String string)
    {
        final List<int[][]> groupList = new ArrayList<int[][]>();
        final String[] groups = string.split(";");
        for(final String group : groups)
        {
            groupList.add(stringToGroup(group));
        }
        final int[][][] res = new int[groupList.size()][][];
        for(int i = 0; i < groupList.size(); ++i)
        {
            res[i] = groupList.get(i);
        }
        return res;
    }

    public static int[][] stringToGroup(final String string)
    {
        final List<int[]> blockList = new ArrayList<int[]>();
        final String[] blocks = string.split(">");
        for(final String block : blocks)
        {
            blockList.add(stringToBlock(block));
        }
        final int[][] res = new int[blockList.size()][];
        for(int i = 0; i < blockList.size(); ++i)
        {
            res[i] = blockList.get(i);
        }
        return res;
    }

    public static int[] stringToBlock(final String string)
    {
        final int[] values = {0, -1};
        final String[] src = string.split(",");
        if(src.length < 1)
        {
            return values;
        }
        values[0] = parseInt(src[0]);
        if(src.length < 2)
        {
            return values;
        }
        values[1] = parseInt(src[1]);
        return values;
    }

    public static boolean isBlockInGroups(final int[] block, final int[][][] groups)
    {
        for(final int[][] group : groups)
        {
            if(indexOfBlock(block, group) > -1)
            {
                return true;
            }
        }
        return false;
    }

    public static int indexOfBlock(final int[] block, final int[][] group)
    {
        for(int i = 0; i < group.length; ++i)
        {
            if(block[0] == group[i][0] && (group[i][1] == -1 || block[1] == group[i][1]))
            {
                return i;
            }
        }
        return -1;
    }

    public static int[][] getRelatedBlocks(final int[] block, final int[][][] groups)
    {
        final List<int[]> blockList = new ArrayList<int[]>();
        for(final int[][] group : groups)
        {
            if(indexOfBlock(block, group) > -1)
            {
                for(int i = 0; i < groups.length; ++i)
                {
                    if(!blockList.contains(group[i]))
                    {
                        blockList.add(i, group[i]);
                    }
                }
            }
        }
        final int[][] secondary = new int[blockList.size()][];
        for(int j = 0; j < blockList.size(); ++j)
        {
            secondary[j] = blockList.get(j);
        }
        return secondary;
    }

    public static int smallerBlockIndex(final int[] block, final int[][][] groups)
    {
        int min = Integer.MAX_VALUE;
        for(final int[][] group : groups)
        {
            final int i = indexOfBlock(block, group);
            if(i > -1 && i < min)
            {
                min = i;
            }
        }
        if(min == Integer.MAX_VALUE)
        {
            min = -1;
        }
        return min;
    }

    public static boolean isMetadataNull(final int id, final int[][][] groups)
    {
        for(final int[][] group : groups)
        {
            for(final int[] block : group)
            {
                if(block[0] == id && block[1] == -1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static int sqr(final int value)
    {
        return value * value;
    }

    public static float sqr(final float value)
    {
        return value * value;
    }

    public static int parseInt(final String string)
    {
        return parseInt(string, 0);
    }

    public static int parseInt(final String string, final int defaultValue)
    {
        try
        {
            return Integer.parseInt(string.trim());
        }
        catch(NumberFormatException ex)
        {
            return defaultValue;
        }
    }

    public void playAtPitch(final int i, final World world, final EntityPlayer entityplayer)
    {
        final float f = (float)Math.pow(2.0, (i - 12) / 12.0);
        world.playSound(entityplayer, entityplayer.getPosition(), new SoundEvent(new ResourceLocation("block.note.pling")), SoundCategory.AMBIENT, 0.5f, f);
    }

    public static boolean moveBlock(final World world, final BlockPos src, final BlockPos tgt, final boolean allowBlockReplacement)
    {
        return moveBlock(world, src, tgt, allowBlockReplacement, 3);
    }

    public static boolean moveBlock(final World world, final BlockPos src, final BlockPos tgt, final boolean allowBlockReplacement, final int notifyFlag)
    {
        if(!world.isRemote && !world.isAirBlock(src) && (world.isAirBlock(tgt) || allowBlockReplacement))
        {
            final IBlockState state = world.getBlockState(src);
            world.setBlockState(tgt, state, notifyFlag);
            TileEntity te = world.getTileEntity(src);
            if(te != null)
            {
                final NBTTagCompound nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
                nbt.setInteger("x", tgt.getX());
                nbt.setInteger("y", tgt.getY());
                nbt.setInteger("z", tgt.getZ());
                te = world.getTileEntity(tgt);
                if(te != null)
                {
                    te.readFromNBT(nbt);
                }
                world.removeTileEntity(src);
            }
            world.setBlockToAir(src);
            return true;
        }
        return false;
    }

    public static int getHighestGroundBlock(final World world, BlockPos pos)
    {
        while(pos.getY() > 0 && (world.isAirBlock(pos) || !world.isBlockNormalCube(pos, true) || world.getBlockState(pos).getBlock().isWood((IBlockAccess)world, pos)))
        {
            pos = pos.down();
        }
        return pos.getY();
    }

    public static int getFirstNonAirBlockFromTop(final World world, final BlockPos pos)
    {
        int x;
        int z;
        int y;
        for(x = pos.getX(), z = pos.getZ(), y = world.getActualHeight(); world.isAirBlock(new BlockPos(x, y - 1, z)) && y > 0; --y)
        {}
        return y;
    }

    public static int getSphericalDistance(final BlockPos startPos, final BlockPos endPos)
    {
        return (int)Math.round(Math.sqrt(sqr(endPos.getX() - startPos.getX()) + sqr(endPos.getZ() - startPos.getZ()) + sqr(endPos.getY() - startPos.getY())));
    }

    public static int getCubicDistance(final BlockPos startPos, final BlockPos endPos)
    {
        return Math.abs(endPos.getX() - startPos.getX()) + Math.abs(endPos.getY() - startPos.getY()) + Math.abs(endPos.getZ() - startPos.getZ());
    }

    public static int getHorSquaredDistance(final BlockPos pos, final BlockPos pos2)
    {
        return Math.abs(pos2.getX() - pos.getX()) + Math.abs(pos2.getZ() - pos.getZ());
    }

    public static int getVerDistance(final BlockPos startPos, final BlockPos endPos)
    {
        return Math.abs(endPos.getY() - startPos.getY());
    }

    public static double getDistanceRatioToCenter(final int point1, final int point2, final int pos)
    {
        final double radius = Math.abs(point2 - point1) / 2.0;
        final double dar = Math.abs(Math.abs(pos - point1) - radius);
        return (radius != 0.0) ? (dar / radius) : 0.0;
    }

    @SuppressWarnings("deprecation")
    public static void setHugeMushroom(final World world, final Random random, final BlockPos pos, final IBlockState state)
    {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        final byte w = 3;
        final byte cw = 4;
        final byte h = 4;
        fillWithBlocks(world, new BlockPos(x - w + 1, y, z - w), new BlockPos(x + w - 1, y + h - 1, z - w), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos(x - w + 1, y, z + w), new BlockPos(x + w - 1, y + h - 1, z + w), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos(x - w, y, z - w + 1), new BlockPos(x - w, y + h - 1, z + w - 1), state.getBlock().getStateFromMeta(10));
        fillWithBlocks(world, new BlockPos(x + w, y, z - w + 1), new BlockPos(x + w, y + h - 1, z + w - 1), state.getBlock().getStateFromMeta(10));
        fillWithBlocksRounded(world, new BlockPos(x - cw, y + h, z - cw), new BlockPos(x + cw, y + h, z + cw), state.getBlock().getStateFromMeta(14));
    }

    public static void fillWithBlocks(final World world, final BlockPos pos1, final BlockPos pos2, final IBlockState state)
    {
        for(int x = pos1.getX(); x <= pos2.getX(); ++x)
        {
            for(int y = pos1.getY(); y <= pos2.getY(); ++y)
            {
                for(int z = pos1.getZ(); z <= pos2.getZ(); ++z)
                {
                    world.setBlockState(new BlockPos(x, y, z), state, 3);
                }
            }
        }
    }

    public static void fillWithBlocksRounded(final World world, final BlockPos pos1, final BlockPos pos2, final IBlockState state)
    {
        for(int x = pos1.getX(); x <= pos2.getX(); ++x)
        {
            for(int y = pos1.getY(); y <= pos2.getY(); ++y)
            {
                for(int z = pos1.getZ(); z <= pos2.getZ(); ++z)
                {
                    final double xd = getDistanceRatioToCenter(pos1.getX(), pos2.getX(), x);
                    final double yd = getDistanceRatioToCenter(pos1.getY(), pos2.getY(), y);
                    final double zd = getDistanceRatioToCenter(pos1.getZ(), pos2.getZ(), z);
                    if(xd * xd + yd * yd + zd * zd <= 1.5)
                    {
                        world.setBlockState(new BlockPos(x, y, z), state, 3);
                    }
                }
            }
        }
    }

    public static boolean isGamePaused(final Minecraft mc)
    {
        return mc.currentScreen != null && (mc.currentScreen.doesGuiPauseGame() || mc.currentScreen instanceof GuiMainMenu);
    }

    public static String getMCTimeString(final long worldTime, final long fmt)
    {
        long HH = (int)((worldTime / 1000L + 6L) % 24L);
        final int MM = (int)(worldTime % 1000L / 1000.0 * 60.0);
        final boolean am = HH < 12L;
        HH %= fmt;
        String hour = "";
        if(fmt == 24L)
        {
            hour = ((HH < 10L) ? "0" : "") + String.valueOf(HH);
        }
        else
        {
            hour = String.valueOf((HH == 0L) ? 12L : HH);
        }
        final String min = ((MM < 10) ? "0" : "") + String.valueOf(MM);
        return hour + ":" + min + ((fmt == 12L) ? (am ? "AM" : "PM") : "");
    }

    public static String ticksToTimeString(final long ticks)
    {
        final long secs = ticks / 20L;
        final long mins = secs / 60L;
        final long hrs = mins / 60L;
        final long days = hrs / 24L;
        String time = "";
        if(days > 0L)
        {
            time = days + ":";
        }
        if(hrs > 0L)
        {
            time = time + ((hrs % 24L < 10L && days > 0L) ? "0" : "") + hrs % 24L + ":";
        }
        if(mins > 0L)
        {
            time = time + ((mins % 60L < 10L && hrs > 0L) ? "0" : "") + mins % 60L + ":";
        }
        else
        {
            time += "0:";
        }
        time = time + ((secs % 60L < 10L) ? "0" : "") + secs % 60L;
        return time;
    }

    public static String stringArrayToString(final String[] sa)
    {
        return stringArrayToString(sa, "#");
    }

    public static String stringArrayToString(final String[] sa, final String separator)
    {
        String ret = "";
        for(final String s : sa)
        {
            ret = ret + separator + " " + s;
        }
        return ret.replaceFirst(separator + " ", "");
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger)
    {
        return loadTextFromURL(url, logger, new String[] {""}, 0);
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger, final int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] {""}, timeoutMS);
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger, final String defaultValue)
    {
        return loadTextFromURL(url, logger, new String[] {defaultValue}, 0);
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger, final String defaultValue, final int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] {defaultValue}, timeoutMS);
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger, final String[] defaultValue)
    {
        return loadTextFromURL(url, logger, defaultValue, 0);
    }

    public static String[] loadTextFromURL(final URL url, final Logger logger, final String[] defaultValue, final int timeoutMS)
    {
        final List<String> arraylist = new ArrayList<String>();
        Scanner scanner = null;
        try
        {
            final URLConnection uc = url.openConnection();
            uc.setReadTimeout(timeoutMS);
            uc.setConnectTimeout(timeoutMS);
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
            scanner = new Scanner(uc.getInputStream(), "UTF-8");
        }
        catch(Throwable e)
        {
            logger.log(Level.WARN, String.format("Error retrieving remote string value at URL %s! Defaulting to %s", url.toString(), stringArrayToString(defaultValue)));
            if(bspkrsCoreMod.INSTANCE.allowDebugOutput)
            {
                e.printStackTrace();
            }
            if(scanner != null)
            {
                scanner.close();
            }
            return defaultValue;
        }
        while(scanner.hasNextLine())
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
        catch(Throwable e)
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
        catch(NoClassDefFoundError e)
        {
            return FMLCommonHandler.instance().getMinecraftServerInstance().getFile("").getAbsolutePath();
        }
    }

    public static String getConfigDir()
    {
        final File configDir = new File(getMinecraftDir(), "config");
        return configDir.getAbsolutePath();
    }

    public static boolean isObfuscatedEnv()
    {
        try
        {
            Blocks.class.getField("air");
            return false;
        }
        catch(Throwable e)
        {
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    public static RayTraceResult getPlayerLookingSpot(final EntityPlayer player, final boolean restrict)
    {
        final float scale = 1.0f;
        final float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * scale;
        final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * scale;
        final double x = player.prevPosX + (player.posX - player.prevPosX) * scale;
        final double y = player.prevPosY + (player.posY - player.prevPosY) * scale + 1.62;
        final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * scale;
        final Vec3d vector1 = new Vec3d(x, y, z);
        final float cosYaw = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float sinYaw = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float cosPitch = -MathHelper.cos(-pitch * 0.017453292f);
        final float sinPitch = MathHelper.sin(-pitch * 0.017453292f);
        final float pitchAdjustedSinYaw = sinYaw * cosPitch;
        final float pitchAdjustedCosYaw = cosYaw * cosPitch;
        double distance = 500.0;
        if(player instanceof EntityPlayerMP && restrict)
        {
            distance = ((EntityPlayerMP)player).interactionManager.getBlockReachDistance();
        }
        final Vec3d vector2 = vector1.addVector(pitchAdjustedSinYaw * distance, sinPitch * distance, pitchAdjustedCosYaw * distance);
        return player.world.rayTraceBlocks(vector1, vector2);
    }

    public static void spawnExplosionParticleAtEntity(final Entity entity)
    {
        for(int i = 0; i < 20; ++i)
        {
            final double d0 = entity.world.rand.nextGaussian() * 0.02;
            final double d2 = entity.world.rand.nextGaussian() * 0.02;
            final double d3 = entity.world.rand.nextGaussian() * 0.02;
            final double d4 = 10.0;
            entity.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX + entity.world.rand.nextFloat() * entity.width * 2.0f - entity.width - d0 * d4, entity.posY + entity.world.rand.nextFloat() * entity.height - d2 * d4, entity.posZ + entity.world.rand.nextFloat() * entity.width * 2.0f - entity.width - d3 * d4, d0, d2, d3, new int[0]);
        }
    }
}
