package bspkrs.helpers.network;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class PacketBufferHelper
{
    public static void writeNBTTagCompound(PacketBuffer data, NBTTagCompound ntc) throws IOException
    {
        data.func_150786_a(ntc);
    }
    
    public static NBTTagCompound readNBTTagCompound(PacketBuffer data) throws IOException
    {
        return data.func_150793_b();
    }
}
