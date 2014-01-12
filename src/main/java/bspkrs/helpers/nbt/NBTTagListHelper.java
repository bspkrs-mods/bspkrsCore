package bspkrs.helpers.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagListHelper
{
    public static NBTTagCompound getCompoundTagAt(NBTTagList ntl, int i)
    {
        return ntl.func_150305_b(i);
    }
    
    public static String getStringTagAt(NBTTagList ntl, int i)
    {
        return ntl.func_150307_f(i);
    }
}
