package com.clussmanproductions.trafficcontrol.event;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.scanner.ScannerThread;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class WorldEventHandler {
	@SubscribeEvent
	public static void onLoad(WorldEvent.Load e)
	{
		if (e.getWorld().isRemote || !ModTrafficControl.IR_INSTALLED)
		{
			return;
		}
		
		ScannerThread thread = new ScannerThread(e.getWorld());
		ScannerThread.ThreadsByWorld.put(e.getWorld(), thread);
		thread.start();
	}
	
	@SubscribeEvent
	public static void onUnload(WorldEvent.Unload e)
	{
		if (e.getWorld().isRemote || !ModTrafficControl.IR_INSTALLED)
		{
			return;
		}
		
		if (ScannerThread.ThreadsByWorld.containsKey(e.getWorld()))
		{
			ScannerThread thread = ScannerThread.ThreadsByWorld.get(e.getWorld());
			thread.requestStop();
			
			while(thread.isAlive()) {}
		}
	}
}
