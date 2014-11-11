package bspkrs.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils
{
    public static String getListAsDelimitedString(List<?> list, String delimiter)
    {
        String r = "";

        for (Object o : list)
            r += delimiter + o.toString();

        return r.replaceFirst(delimiter, "");
    }

    @SuppressWarnings("rawtypes")
    public static String getListAsUniqueDelimitedString(List<?> list, String delimiter)
    {
        String r = "";

        List seen = new ArrayList();

        for (Object o : list)
            if (!seen.contains(o))
            {
                r += delimiter + o.toString();
                seen.add(o);
            }

        return r.replaceFirst(delimiter, "");
    }

    public static boolean doesListAContainAllUniqueListBValues(List<?> listA, List<?> listB)
    {
        for (Object o : listB)
            if (!listA.contains(o))
                return false;

        return true;
    }

    public static List<BlockID> getDelimitedStringAsBlockIDList(String dList, String delimiter)
    {
        List<BlockID> list = new ArrayList<BlockID>();

        for (String format : dList.split(delimiter))
            if (!format.trim().isEmpty())
                list.add(BlockID.parse(format));

        return list;
    }

    public static List<ItemID> getDelimitedStringAsItemIDList(String dList, String delimiter)
    {
        List<ItemID> list = new ArrayList<ItemID>();

        for (String format : dList.split(delimiter))
            if (!format.trim().isEmpty())
                list.add(new ItemID(format, ","));

        return list;
    }
}
