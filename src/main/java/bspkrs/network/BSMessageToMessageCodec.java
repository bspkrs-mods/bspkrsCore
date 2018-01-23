package bspkrs.network;

import net.minecraftforge.fml.common.network.*;
import io.netty.channel.*;
import io.netty.buffer.*;
import net.minecraft.network.*;

public class BSMessageToMessageCodec extends FMLIndexedMessageToMessageCodec<BSPacket>
{
    public void encodeInto(final ChannelHandlerContext ctx, final BSPacket source, ByteBuf target) throws Exception
    {
        target = (ByteBuf)new PacketBuffer(target);
        source.writeBytes((PacketBuffer)target);
    }

    public void decodeInto(final ChannelHandlerContext ctx, ByteBuf source, final BSPacket target)
    {
        source = (ByteBuf)new PacketBuffer(source);
        target.readBytes((PacketBuffer)source);
    }
}
