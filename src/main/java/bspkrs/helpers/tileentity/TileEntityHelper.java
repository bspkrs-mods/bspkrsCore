package bspkrs.helpers.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHelper
{
    public static void readFromNBT(TileEntity te, NBTTagCompound ntc)
    {
        te.func_145839_a(ntc);
    }
    
    public static void writeToNBT(TileEntity te, NBTTagCompound ntc)
    {
        te.func_145841_b(ntc);
    }
}
