package com.clussmanproductions.trafficcontrol.network;

import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsPoleBasedTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCrossingLampPoleBasedSync implements IMessage {

	public BlockPos pos;
	public NBTTagCompound syncData;
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeTag(buf, syncData);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		syncData = ByteBufUtils.readTag(buf);
	}
	
	public static class Handler implements IMessageHandler<PacketCrossingLampPoleBasedSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketCrossingLampPoleBasedSync message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketCrossingLampPoleBasedSync message, MessageContext ctx) {
			World world = ctx.getServerHandler().player.world;
			TileEntity te = world.getTileEntity(message.pos);
			if (!(te instanceof CrossingLampsPoleBasedTileEntity))
			{
				return;
			}
			
			CrossingLampsPoleBasedTileEntity lamps = (CrossingLampsPoleBasedTileEntity)te;
			lamps.onSyncFromClient(message.syncData);
		}
	}
}
