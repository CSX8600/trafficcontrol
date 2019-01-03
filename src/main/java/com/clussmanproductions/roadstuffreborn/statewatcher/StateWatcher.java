package com.clussmanproductions.roadstuffreborn.statewatcher;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Tuple2;

@EventBusSubscriber
public class StateWatcher {
	private static StateWatcherData data;
	private static ArrayList<Tuple2<BlockPos, IStateWatchable>> stateWatchablesToRemove = new ArrayList<Tuple2<BlockPos, IStateWatchable>>();
	
	@SubscribeEvent
	public static void blockPlace(BlockEvent.PlaceEvent e)
	{
		if (e.getWorld().isRemote)
		{
			return;
		}
		
		setupData(e.getWorld());
		doRemoveStateWatchables();
		
		for(IStateWatchable watchable : data.getStateWatchersForBlockPos(e.getPos()))
		{
			watchable.onBlockPlace(e);
		}
		
		doRemoveStateWatchables();
	}
	
	@SubscribeEvent
	public static void blockRemove(BlockEvent.BreakEvent e)
	{
		if (e.getWorld().isRemote)
		{
			return;
		}
		
		setupData(e.getWorld());
		doRemoveStateWatchables();
		
		for(IStateWatchable watchable : data.getStateWatchersForBlockPos(e.getPos()))
		{
			watchable.onBlockBreak(e);
		}
		
		doRemoveStateWatchables();
	}
	
	private static void doRemoveStateWatchables()
	{
		if (!stateWatchablesToRemove.isEmpty())
		{
			data.removeStateWatchers(stateWatchablesToRemove);
		}
		
		stateWatchablesToRemove.clear();
	}

	public static void removeStateWatcherAtBlockPos(BlockPos pos, IStateWatchable watchable, World world)
	{
		if (pos == null)
		{
			return;
		}
		
		if (data == null)
		{
			data = StateWatcherData.get(world);
		}
		stateWatchablesToRemove.add(new Tuple2<BlockPos, IStateWatchable>(pos, watchable));
	}
	
	public static void addStateWatcher(BlockPos pos, IStateWatchable watchable, World world)
	{
		if (pos == null)
		{
			return;
		}
		
		if (data == null)
		{
			data = StateWatcherData.get(world);
		}
		
		data.addStateWatcher(pos, watchable);
	}

	private static void setupData(World world)
	{
		if (data == null)
		{
			data = StateWatcherData.get(world);
		}
	}
}
