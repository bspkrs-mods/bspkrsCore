package bspkrs.network;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

public interface BSPacket
{
    public void readBytes(PacketBuffer bytes) throws IOException;
    
    public void writeBytes(PacketBuffer bytes) throws IOException;
}
