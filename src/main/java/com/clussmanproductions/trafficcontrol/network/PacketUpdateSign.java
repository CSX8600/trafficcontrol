package com.clussmanproductions.trafficcontrol.network;

import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateSign implements IMessage {

	public int x;
	public int y;
	public int z;
	public int type;
	public int variant;
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		type = buf.readInt();
		variant = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(type);
		buf.writeInt(variant);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateSign, IMessage>
	{
		@Override
		public IMessage onMessage(PacketUpdateSign message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		private void handle(PacketUpdateSign message, MessageContext ctx)
		{
			BlockPos pos = new BlockPos(message.x, message.y, message.z);
			World world = ctx.getServerHandler().player.world;
			TileEntity te = world.getTileEntity(pos);
			
			if (te instanceof SignTileEntity)
			{
				SignTileEntity sign = (SignTileEntity)te;
				sign.setType(message.type);
				sign.setVariant(message.variant);
				sign.markDirty();
				IBlockState blockState = world.getBlockState(pos);
				
				world.notifyBlockUpdate(pos, blockState, blockState, 0);
				world.scheduleBlockUpdate(pos, blockState.getBlock(), 0, 1);
			}
		}
	}
}
