package com.clussmanproductions.trafficcontrol.network;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.google.common.collect.ImmutableMap;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSignPackCheck implements IMessage {

	public HashMap<UUID, String> signPacks = new HashMap<>();
	@Override
	public void fromBytes(ByteBuf buf) {
		int packs = buf.readInt();
		signPacks = new HashMap<>(packs);
		for(int i = 0; i < packs; i++)
		{
			long most = buf.readLong();
			long least = buf.readLong();
			String name = ByteBufUtils.readUTF8String(buf);
			
			UUID packID = new UUID(most, least);
			signPacks.put(packID, name);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(signPacks.size());
		for(Entry<UUID, String> entry : signPacks.entrySet())
		{
			buf.writeLong(entry.getKey().getMostSignificantBits());
			buf.writeLong(entry.getKey().getLeastSignificantBits());
			ByteBufUtils.writeUTF8String(buf, entry.getValue());
		}
	}

	public static class Handler implements IMessageHandler<PacketSignPackCheck, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSignPackCheck message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		private void handle(PacketSignPackCheck message, MessageContext ctx)
		{			
			ImmutableMap<UUID, String> packs = ModTrafficControl.instance.signRepo.getPacksByID();
			for(Entry<UUID, String> serverPack : message.signPacks.entrySet())
			{
				if (!packs.containsKey(serverPack.getKey()))
				{
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString("You are missing Traffic Control signpack " + serverPack.getValue() + "! Some signs may display as ERROR."));
				}
			}
		}
	}
}
