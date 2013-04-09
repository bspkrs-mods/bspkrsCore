package bspkrs.util;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

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
    
    public static boolean isIDInList(int id, int md, String list)
    {
        String[] itemArray = list.split(";");
        for (int i = 0; i < itemArray.length; i++)
        {
            String[] values = itemArray[i].split(",");
            int tempID = parseInt(values[0], Integer.MAX_VALUE);
            
            if (tempID == Integer.MAX_VALUE)
                continue;
            
            if (values.length > 1 && parseInt(values[1], -1) == md)
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
                if (group[i][1] == -1 || block[1] == group[i][1])
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
            if (i > -1 && i < min)
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
                if (block[0] == id && block[1] == -1)
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
        world.playSoundAtEntity(entityplayer, "note.pling", 0.5F, f);
    }
    
    public static int getHighestGroundBlock(World world, int x, int y, int z)
    {
        for (; y > 0 && (world.isAirBlock(x, y, z) || !world.isBlockNormalCube(x, y, z) || world.getBlockId(x, y, z) == Block.wood.blockID); y--)
        {}
        return y;
    }
    
    public static int getFirstNonAirBlockFromTop(World world, int x, int z)
    {
        int y;
        for (y = world.getActualHeight(); world.isAirBlock(x, y - 1, z) && y > 0; y--)
        {}
        return y;
    }
    
    public static double getDistanceRatioToCenter(int point1, int point2, int pos)
    {
        double radius = Math.abs(point2 - point1) / 2D;
        double dar = Math.abs(Math.abs(pos - point1) - radius);
        return radius != 0.0D ? dar / radius : 0.0D;
    }
    
    public static void setHugeMushroom(World world, Random random, int x, int y, int z, int id)
    {
        byte w = 3;
        byte cw = 4;
        byte h = 4;
        fillWithBlocks(world, (x - w) + 1, y, z - w, (x + w) - 1, (y + h) - 1, z - w, id, 10);
        fillWithBlocks(world, (x - w) + 1, y, z + w, (x + w) - 1, (y + h) - 1, z + w, id, 10);
        fillWithBlocks(world, x - w, y, (z - w) + 1, x - w, (y + h) - 1, (z + w) - 1, id, 10);
        fillWithBlocks(world, x + w, y, (z - w) + 1, x + w, (y + h) - 1, (z + w) - 1, id, 10);
        fillWithBlocksRounded(world, x - cw, y + h, z - cw, x + cw, y + h, z + cw, id, 14);
    }
    
    public static void fillWithBlocks(World world, int x1, int y1, int z1, int x2, int y2, int z2, int id, int damage)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                    world.setBlock(x, y, z, id, damage, 3);
    }
    
    public static void fillWithBlocksRounded(World world, int x1, int y1, int z1, int x2, int y2, int z2, int id, int damage)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                {
                    double xd = getDistanceRatioToCenter(x1, x2, x);
                    double yd = getDistanceRatioToCenter(y1, y2, y);
                    double zd = getDistanceRatioToCenter(z1, z2, z);
                    if (xd * xd + yd * yd + zd * zd <= 1.5D)
                        world.setBlock(x, y, z, id, damage, 3);
                }
    }
    
    public static boolean isGamePaused(Minecraft mc)
    {
        return mc.currentScreen != null && (mc.currentScreen.doesGuiPauseGame() || mc.currentScreen instanceof GuiMainMenu);
    }
    
    public static String getMCTimeString(long worldTime, long fmt)
    {
        long HH = (int) (((worldTime / 1000L) + 6) % 24L);
        int MM = (int) (worldTime % 1000L / 1000.0D * 60.0D);
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
            time = time + ((hrs % 24L) < 10L && days > 0L ? "0" : "") + (hrs % 24L) + ":";
        
        if (mins > 0L)
            time = time + ((mins % 60L) < 10L && hrs > 0L ? "0" : "") + (mins % 60L) + ":";
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
        return loadTextFromURL(url, logger, new String[] { "" }, 1000);
    }
    
    public static String[] loadTextFromURL(URL url, Logger logger, int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] { "" }, timeoutMS);
    }
    
    public static String[] loadTextFromURL(URL url, Logger logger, String defaultValue)
    {
        return loadTextFromURL(url, logger, new String[] { defaultValue }, 1000);
    }
    
    public static String[] loadTextFromURL(URL url, Logger logger, String defaultValue, int timeoutMS)
    {
        return loadTextFromURL(url, logger, new String[] { defaultValue }, timeoutMS);
    }
    
    public static String[] loadTextFromURL(URL url, Logger logger, String[] defaultValue)
    {
        return loadTextFromURL(url, logger, defaultValue, 1000);
    }
    
    public static String[] loadTextFromURL(URL url, Logger logger, String[] defaultValue, int timeoutMS)
    {
        ArrayList arraylist = new ArrayList();
        Scanner scanner = null;
        try
        {
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(timeoutMS);
            uc.setConnectTimeout(timeoutMS);
            scanner = new Scanner(uc.getInputStream(), "UTF-8");
        }
        catch (Throwable e)
        {
            logger.log(Level.WARNING, String.format("Error retrieving remote string value! Defaulting to %s", stringArrayToString(defaultValue)));
            return defaultValue;
        }
        
        while (scanner.hasNextLine())
        {
            arraylist.add(scanner.nextLine());
        }
        scanner.close();
        return (String[]) arraylist.toArray(new String[arraylist.size()]);
    }
    
    public static String getMinecraftDir()
    {
        try
        {
            return Minecraft.getMinecraftDir().getAbsolutePath();
        }
        catch (NoClassDefFoundError e)
        {
            return MinecraftServer.getServer().getFile("").getAbsolutePath();
        }
    }
    
    public static String getConfigDir()
    {
        File configDir = new File(getMinecraftDir(), "config");
        return configDir.getAbsolutePath();
    }
}
