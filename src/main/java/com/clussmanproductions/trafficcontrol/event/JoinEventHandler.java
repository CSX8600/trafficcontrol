package com.clussmanproductions.trafficcontrol.event;

import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.network.PacketSignPackCheck;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber
public class JoinEventHandler {
	@SubscribeEvent
	public static void join(PlayerLoggedInEvent e)
	{
		PacketSignPackCheck packCheck = new PacketSignPackCheck();
		packCheck.signPacks = new HashMap<>(ModTrafficControl.instance.signRepo.getPacksByID());
		PacketHandler.INSTANCE.sendTo(packCheck, (EntityPlayerMP)e.player);
	}
}
