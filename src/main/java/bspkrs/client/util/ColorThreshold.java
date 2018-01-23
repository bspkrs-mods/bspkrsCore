package bspkrs.client.util;

import java.util.*;

public class ColorThreshold implements Comparable<ColorThreshold>
{
    public int threshold;
    public String colorCode;

    public ColorThreshold(final int t, final String c)
    {
        this.threshold = t;
        this.colorCode = c;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.threshold) + ", " + this.colorCode;
    }

    @Override
    public int compareTo(final ColorThreshold o)
    {
        if(this.threshold > o.threshold)
        {
            return 1;
        }
        if(this.threshold < o.threshold)
        {
            return -1;
        }
        return 0;
    }

    public static String getColorCode(final List<ColorThreshold> colorList, final int value)
    {
        for(final ColorThreshold ct : colorList)
        {
            if(value <= ct.threshold)
            {
                return ct.colorCode;
            }
        }
        return "f";
    }
}
