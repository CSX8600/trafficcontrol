package com.clussmanproductions.trafficcontrol;

import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayBox;
import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayTuner;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("trafficcontrol")
public class ModItems {
	@ObjectHolder("crossing_relay_box")
	public static ItemCrossingRelayBox crossing_relay_box;
	@ObjectHolder("crossing_relay_tuner")
	public static ItemCrossingRelayTuner crossing_relay_tuner;
	
	public static void initModels(ModelRegistryEvent e)
	{
		crossing_relay_box.initModel();
		crossing_relay_tuner.initModel();
	}
}
