package bspkrs.network;

import net.minecraft.network.PacketBuffer;

public interface BSPacket
{
    public void readBytes(PacketBuffer bytes);
    
    public void writeBytes(PacketBuffer bytes);
}
