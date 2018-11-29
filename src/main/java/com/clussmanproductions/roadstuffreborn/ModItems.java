package com.clussmanproductions.roadstuffreborn;

import com.clussmanproductions.roadstuffreborn.item.ItemCrossingRelayBox;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("roadstuffreborn")
public class ModItems {
	@ObjectHolder("crossing_relay_box")
	public static ItemCrossingRelayBox crossing_relay_box;
	
	public static void initModels(ModelRegistryEvent e)
	{
		crossing_relay_box.initModel();
	}
}
