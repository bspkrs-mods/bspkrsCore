package bspkrs.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import java.io.*;
import java.text.*;
import java.util.*;

public class UniqueNameListGenerator
{
    private static UniqueNameListGenerator instance;

    public static UniqueNameListGenerator instance()
    {
        if(UniqueNameListGenerator.instance == null)
        {
            UniqueNameListGenerator.instance = new UniqueNameListGenerator();
        }
        return UniqueNameListGenerator.instance;
    }

    public void run()
    {
        final File listFile = new File(new File(CommonUtils.getConfigDir()), "UniqueNames.txt");
        try
        {
            final ArrayList<String> blockList = new ArrayList<String>();
            final ArrayList<String> itemList = new ArrayList<String>();
            for(final Object obj : Block.REGISTRY.getKeys())
            {
                blockList.add(obj.toString());
            }
            for(final Object obj : Item.REGISTRY.getKeys())
            {
                itemList.add(obj.toString());
            }
            Collections.sort(blockList);
            Collections.sort(itemList);
            if(listFile.exists())
            {
                listFile.delete();
            }
            listFile.createNewFile();
            final PrintWriter out = new PrintWriter(new FileWriter(listFile));
            out.println("# generated by bspkrsCore " + new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
            out.println();
            out.println("**********************************************");
            out.println("*  ####   #       ###    ###   #   #   ####  *");
            out.println("*  #   #  #      #   #  #   #  #  #   #      *");
            out.println("*  ####   #      #   #  #      ###     ###   *");
            out.println("*  #   #  #      #   #  #      #  #       #  *");
            out.println("*  ####   #####   ###    ####  #   #  ####   *");
            out.println("**********************************************");
            out.println();
            out.println();
            for(final String s : blockList)
            {
                out.println(s);
            }
            out.println();
            out.println();
            out.println("***************************************");
            out.println("*  #####  #####  #####  #   #   ####  *");
            out.println("*    #      #    #      ## ##  #      *");
            out.println("*    #      #    ###    # # #   ###   *");
            out.println("*    #      #    #      #   #      #  *");
            out.println("*  #####    #    #####  #   #  ####   *");
            out.println("***************************************");
            out.println();
            out.println();
            for(final String s : itemList)
            {
                out.println(s);
            }
            out.println();
            out.println();
            out.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
