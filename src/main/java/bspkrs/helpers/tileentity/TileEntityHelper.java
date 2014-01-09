/**
 * Copyright © 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
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
