package bspkrs.util;

import java.util.*;

public class ListUtils
{
    public static String getListAsDelimitedString(final List<?> list, final String delimiter)
    {
        String r = "";
        for(final Object o : list)
        {
            r = r + delimiter + o.toString();
        }
        return r.replaceFirst(delimiter, "");
    }

    public static String getListAsUniqueDelimitedString(final List<?> list, final String delimiter)
    {
        String r = "";
        final List<Object> seen = new ArrayList<Object>();
        for(final Object o : list)
        {
            if(!seen.contains(o))
            {
                r = r + delimiter + o.toString();
                seen.add(o);
            }
        }
        return r.replaceFirst(delimiter, "");
    }

    public static boolean doesListAContainAllUniqueListBValues(final List<?> listA, final List<?> listB)
    {
        for(final Object o : listB)
        {
            if(!listA.contains(o))
            {
                return false;
            }
        }
        return true;
    }

    public static List<BlockID> getDelimitedStringAsBlockIDList(final String dList, final String delimiter)
    {
        final List<BlockID> list = new ArrayList<BlockID>();
        for(final String format : dList.split(delimiter))
        {
            if(!format.trim().isEmpty())
            {
                list.add(BlockID.parse(format));
            }
        }
        return list;
    }

    public static List<ItemID> getDelimitedStringAsItemIDList(final String dList, final String delimiter)
    {
        final List<ItemID> list = new ArrayList<ItemID>();
        for(final String format : dList.split(delimiter))
        {
            if(!format.trim().isEmpty())
            {
                list.add(new ItemID(format, ","));
            }
        }
        return list;
    }
}
