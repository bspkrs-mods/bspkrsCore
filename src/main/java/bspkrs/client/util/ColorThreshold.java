package bspkrs.client.util;

import java.util.List;

public class ColorThreshold implements Comparable<ColorThreshold>
{
    public int    threshold;
    public String colorCode;

    public ColorThreshold(int t, String c)
    {
        threshold = t;
        colorCode = c;
    }

    @Override
    public String toString()
    {
        return String.valueOf(threshold) + ", " + colorCode;
    }

    @Override
    public int compareTo(ColorThreshold o)
    {
        if (this.threshold > o.threshold)
            return 1;
        else if (this.threshold < o.threshold)
            return -1;
        else
            return 0;
    }

    /**
     * Returns the colorCode attached to the first threshold in the list that is >= value. Expects that the list has been sorted by
     * threshold ascending.
     */
    public static String getColorCode(List<ColorThreshold> colorList, int value)
    {
        for (ColorThreshold ct : colorList)
            if (value <= ct.threshold)
                return ct.colorCode;

        return "f";
    }
}
