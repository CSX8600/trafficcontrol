package com.clussmanproductions.trafficcontrol.statewatcher;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;

public interface IStateWatchable {
	public void onBlockPlace(BlockEvent.PlaceEvent e);
	
	public void onBlockBreak(BlockEvent.BreakEvent e);
}
