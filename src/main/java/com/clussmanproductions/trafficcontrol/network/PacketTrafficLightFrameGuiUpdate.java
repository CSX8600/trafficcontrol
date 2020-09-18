package com.clussmanproductions.trafficcontrol.network;

import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTrafficLightFrameGuiUpdate implements IMessage {

	public int slotId;
	public boolean alwaysFlash;
	@Override
	public void fromBytes(ByteBuf buf) {
		slotId = buf.readInt();
		alwaysFlash = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slotId);
		buf.writeBoolean(alwaysFlash);
	}
	
	public static class Handler implements IMessageHandler<PacketTrafficLightFrameGuiUpdate, IMessage>
	{

		@Override
		public IMessage onMessage(PacketTrafficLightFrameGuiUpdate message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		private void handle(PacketTrafficLightFrameGuiUpdate packet, MessageContext ctx)
		{
			ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
			if (!(stack.getItem() instanceof BaseItemTrafficLightFrame))
			{
				return;
			}
			
			BaseItemTrafficLightFrame heldItem = (BaseItemTrafficLightFrame)stack.getItem();
			heldItem.handleGuiAlwaysUpdate(stack, packet.slotId, packet.alwaysFlash);
		}
	}

}
