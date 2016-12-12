package bspkrs.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

public class BSMessageToMessageCodec extends FMLIndexedMessageToMessageCodec<BSPacket>
{
	public BSMessageToMessageCodec ()
	{
	}

	@Override
	public void encodeInto (ChannelHandlerContext ctx, BSPacket source, ByteBuf target) throws Exception
	{
		target = new PacketBuffer(target);
		source.writeBytes((PacketBuffer) target);
	}

	@Override
	public void decodeInto (ChannelHandlerContext ctx, ByteBuf source, BSPacket target)
	{
		source = new PacketBuffer(source);
		target.readBytes((PacketBuffer) source);
	}
}