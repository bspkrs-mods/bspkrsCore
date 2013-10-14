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
    
    public static List<BlockID> getDelimitedStringAsBlockIDList(String dList, String delimiter)
    {
        List<BlockID> list = new ArrayList<BlockID>();
        
        for (String format : dList.split(delimiter))
            list.add(new BlockID(format));
        
        return list;
    }
    
    public static List<ItemID> getDelimitedStringAsItemIDList(String dList, String delimiter)
    {
        List<ItemID> list = new ArrayList<ItemID>();
        
        for (String format : dList.split(delimiter))
            list.add(new ItemID(format));
        
        return list;
    }
}
