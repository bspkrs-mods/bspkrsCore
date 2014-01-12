package bspkrs.helpers.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagCompoundHelper
{
    public static NBTTagList getTagList(NBTTagCompound ntc, String name, Byte type)
    {
        return ntc.func_150295_c(name, type);
    }
}
