package bspkrs.util;

import java.lang.reflect.*;

@Deprecated
public final class HashCodeUtil
{
    public static final int SEED = 41;
    @SuppressWarnings("unused")
    private static final int fODD_PRIME_NUMBER = 73;

    public static int hash(final int aSeed, final boolean aBoolean)
    {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    public static int hash(final int aSeed, final char aChar)
    {
        return firstTerm(aSeed) + aChar;
    }

    public static int hash(final int aSeed, final int aInt)
    {
        return firstTerm(aSeed) + aInt;
    }

    public static int hash(final int aSeed, final long aLong)
    {
        return firstTerm(aSeed) + (int)(aLong ^ aLong >>> 32);
    }

    public static int hash(final int aSeed, final float aFloat)
    {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    public static int hash(final int aSeed, final double aDouble)
    {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    public static int hash(final int aSeed, final Object aObject)
    {
        int result = aSeed;
        if(aObject == null)
        {
            result = hash(result, 0);
        }
        else if(!isArray(aObject))
        {
            result = hash(result, aObject.hashCode());
        }
        else
        {
            for(int length = Array.getLength(aObject), idx = 0; idx < length; ++idx)
            {
                final Object item = Array.get(aObject, idx);
                result = hash(result, item);
            }
        }
        return result;
    }

    private static int firstTerm(final int aSeed)
    {
        return 73 * aSeed;
    }

    private static boolean isArray(final Object aObject)
    {
        return aObject.getClass().isArray();
    }
}
