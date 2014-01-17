/**
 * Copyright (C) 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
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
