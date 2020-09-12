package com.clussmanproductions.trafficcontrol.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	private static int packetId = 0;
	
	public static SimpleNetworkWrapper INSTANCE = null;
	
	public PacketHandler() {}
	
	public static int nextID()
	{
		return packetId++;
	}
	
	public static void registerMessages(String channelName)
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}
	
	private static void registerMessages()
	{
		INSTANCE.registerMessage(PacketUpdateSign.Handler.class, PacketUpdateSign.class, nextID(), Side.SERVER);
		INSTANCE.registerMessage(PacketSyncableTileEntity.Handler.class, PacketSyncableTileEntity.class, nextID(), Side.SERVER);
		INSTANCE.registerMessage(PacketTrafficLightFrameGuiUpdate.Handler.class, PacketTrafficLightFrameGuiUpdate.class, nextID(), Side.SERVER);
	}
}
