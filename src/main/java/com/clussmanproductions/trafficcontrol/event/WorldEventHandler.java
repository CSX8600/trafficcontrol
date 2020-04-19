package com.clussmanproductions.trafficcontrol.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.scanner.ScannerThread;
import com.clussmanproductions.trafficcontrol.util.MultithreadCapableEntityTracker;

import net.minecraft.entity.EntityTracker;
import net.minecraft.world.WorldServer;
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
		
		try
		{
			ModTrafficControl.logger.info("Getting entity tracker for world");
			Field field;
			try
			{
				field = WorldServer.class.getDeclaredField("entityTracker");
			}
			catch(Exception ex)
			{
				field = WorldServer.class.getDeclaredField("field_73062_L");
			}
			
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			
			EntityTracker entityTracker = (EntityTracker)field.get(e.getWorld());
			
			if (entityTracker.getClass().getName().equals(EntityTracker.class.getName()))
			{
				ModTrafficControl.logger.info("Entity Tracker has not yet been wrapped...wrapping");
				MultithreadCapableEntityTracker wrappedEntityTracker = new MultithreadCapableEntityTracker(entityTracker, (WorldServer)e.getWorld());
				field.set(e.getWorld(), wrappedEntityTracker);
			}
			else
			{
				ModTrafficControl.logger.info("Entity Tracker has already been wrapped, assuming it's ready for multithreaded use");
			}
			
			ScannerThread thread = new ScannerThread(e.getWorld());
			ScannerThread.ThreadsByWorld.put(e.getWorld(), thread);
			thread.start();
		}
		catch(Exception ex)
		{
			ModTrafficControl.logger.error("Could not start Scanner Thread!  Could not replace Entity Tracker: " + ex.toString());
		}
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
