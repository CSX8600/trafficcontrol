package com.clussmanproductions.trafficcontrol;

import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayBox;
import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayTuner;
import com.clussmanproductions.trafficcontrol.item.ItemStreetSign;
import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightBulb;
import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightFrame;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("trafficcontrol")
public class ModItems {
	@ObjectHolder("crossing_relay_box")
	public static ItemCrossingRelayBox crossing_relay_box;
	@ObjectHolder("crossing_relay_tuner")
	public static ItemCrossingRelayTuner crossing_relay_tuner;
	@ObjectHolder("traffic_light_bulb")
	public static ItemTrafficLightBulb traffic_light_bulb;
	@ObjectHolder("traffic_light_frame")
	public static ItemTrafficLightFrame traffic_light_frame;
	@ObjectHolder("street_sign")
	public static ItemStreetSign street_sign;
	
	public static void initModels(ModelRegistryEvent e)
	{
		crossing_relay_box.initModel();
		crossing_relay_tuner.initModel();
		traffic_light_bulb.initModel();
		traffic_light_frame.initModel();
		street_sign.initModel();
	}
}
