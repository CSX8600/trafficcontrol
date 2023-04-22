package com.clussmanproductions.trafficcontrol;

import com.clussmanproductions.trafficcontrol.item.*;

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
	@ObjectHolder("traffic_light_5_frame")
	public static ItemTrafficLight5Frame traffic_light_5_frame;
	@ObjectHolder("traffic_light_doghouse_frame")
	public static ItemTrafficLightDoghouseFrame traffic_light_doghouse_frame;
	@ObjectHolder("traffic_light_1_frame")
	public static ItemTrafficLight1Frame traffic_light_1_frame;
	@ObjectHolder("traffic_light_2_frame")
	public static ItemTrafficLight2Frame traffic_light_2_frame;
	@ObjectHolder("traffic_light_4_frame")
	public static ItemTrafficLight4Frame traffic_light_4_frame;
	@ObjectHolder("traffic_light_6_frame")
	public static BaseItemTrafficLightFrame traffic_light_6_frame;
	

	public static void initModels(ModelRegistryEvent e)
	{
		crossing_relay_box.initModel();
		crossing_relay_tuner.initModel();
		traffic_light_bulb.initModel();
		traffic_light_frame.initModel();
		street_sign.initModel();
		traffic_light_5_frame.initModel();
		traffic_light_doghouse_frame.initModel();
		traffic_light_1_frame.initModel();
		traffic_light_2_frame.initModel();
		traffic_light_4_frame.initModel();
		traffic_light_6_frame.initModel();
		
	}
}
