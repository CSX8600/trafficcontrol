package com.clussmanproductions.trafficcontrol.network;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.proxy.ClientProxy;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSideSoundPacket implements IMessage
{
	public BlockPos pos;
	public String modID = ModTrafficControl.MODID;
	public String soundName;
	public float volume = 1F;
	public float pitch = 1F;
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		pos = BlockPos.fromLong(buf.readLong());
		modID = ByteBufUtils.readUTF8String(buf);
		soundName = ByteBufUtils.readUTF8String(buf);
		volume = buf.readFloat();
		pitch = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeUTF8String(buf, modID);
		ByteBufUtils.writeUTF8String(buf, soundName);
		buf.writeFloat(volume);
		buf.writeFloat(pitch);
	}
	
	public static class Handler implements IMessageHandler<ServerSideSoundPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ServerSideSoundPacket message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		private void handle(ServerSideSoundPacket message, MessageContext ctx)
		{
			try
			{
				ClientProxy.playSoundHandler(message, ctx);
			}
			catch(NullPointerException ex)
			{
				ModTrafficControl.logger.error("[" + ModTrafficControl.MODID + "] An error occurred in " + Handler.class.getName());
				ModTrafficControl.logger.error(ex);
				ModTrafficControl.logger.error("[" + ModTrafficControl.MODID + "] Please report this error to us.");
			}
		}
	}
}
