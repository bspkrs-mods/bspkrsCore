/**
 * Copyright (C) 2014 bspkrs <bspkrs@gmail.com>
 * 
 * This Java package (bspkrs.helpers.**.*) is free software. It comes
 * without any warranty, to the extent permitted by applicable law.
 * You can redistribute it and/or modify it under the terms of the
 * Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package bspkrs.helpers.network;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class PacketBufferHelper
{
    public static void writeNBTTagCompound(PacketBuffer data, NBTTagCompound ntc) throws IOException
    {
        data.writeNBTTagCompoundToBuffer(ntc);
    }

    public static NBTTagCompound readNBTTagCompound(PacketBuffer data) throws IOException
    {
        return data.readNBTTagCompoundFromBuffer();
    }
}
