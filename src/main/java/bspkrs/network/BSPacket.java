package bspkrs.network;

import net.minecraft.network.*;

public interface BSPacket
{
    void readBytes(final PacketBuffer p0);

    void writeBytes(final PacketBuffer p0);
}
