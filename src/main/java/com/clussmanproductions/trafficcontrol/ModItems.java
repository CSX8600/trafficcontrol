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
	@ObjectHolder("ytraffic_light_frame")
	public static ItemYTrafficLightFrame ytraffic_light_frame;
	@ObjectHolder("otraffic_light_frame")
	public static ItemOTrafficLightFrame otraffic_light_frame;
	@ObjectHolder("street_sign")
	public static ItemStreetSign street_sign;
	@ObjectHolder("traffic_light_5_frame")
	public static ItemTrafficLight5Frame traffic_light_5_frame;
	@ObjectHolder("ytraffic_light_5_frame")
	public static ItemYTrafficLight5Frame ytraffic_light_5_frame;
	@ObjectHolder("otraffic_light_5_frame")
	public static ItemOTrafficLight5Frame otraffic_light_5_frame;
	@ObjectHolder("traffic_light_doghouse_frame")
	public static ItemTrafficLightDoghouseFrame traffic_light_doghouse_frame;
	@ObjectHolder("ytraffic_light_doghouse_frame")
	public static ItemYTrafficLightDoghouseFrame ytraffic_light_doghouse_frame;
	@ObjectHolder("otraffic_light_doghouse_frame")
	public static ItemOTrafficLightDoghouseFrame otraffic_light_doghouse_frame;
	@ObjectHolder("traffic_light_1_frame")
	public static ItemTrafficLight1Frame traffic_light_1_frame;
	@ObjectHolder("ytraffic_light_1_frame")
	public static ItemYTrafficLight1Frame ytraffic_light_1_frame;
	@ObjectHolder("otraffic_light_1_frame")
	public static ItemOTrafficLight1Frame otraffic_light_1_frame;
	@ObjectHolder("traffic_light_2_frame")
	public static ItemTrafficLight2Frame traffic_light_2_frame;
	@ObjectHolder("ytraffic_light_2_frame")
	public static ItemYTrafficLight2Frame ytraffic_light_2_frame;
	@ObjectHolder("otraffic_light_2_frame")
	public static ItemOTrafficLight2Frame otraffic_light_2_frame;
	@ObjectHolder("traffic_light_4_frame")
	public static ItemTrafficLight4Frame traffic_light_4_frame;
	@ObjectHolder("ytraffic_light_4_frame")
	public static ItemYTrafficLight4Frame ytraffic_light_4_frame;
	@ObjectHolder("otraffic_light_4_frame")
	public static ItemOTrafficLight4Frame otraffic_light_4_frame;
	@ObjectHolder("traffic_light_6_frame")
	public static ItemTrafficLight6Frame traffic_light_6_frame;
	@ObjectHolder("ytraffic_light_6_frame")
	public static ItemYTrafficLight6Frame ytraffic_light_6_frame;
	@ObjectHolder("otraffic_light_6_frame")
	public static ItemOTrafficLight6Frame otraffic_light_6_frame;
	

	public static void initModels(ModelRegistryEvent e)
	{
		crossing_relay_box.initModel();
		crossing_relay_tuner.initModel();
		traffic_light_bulb.initModel();
		traffic_light_frame.initModel();
		ytraffic_light_frame.initModel();
		otraffic_light_frame.initModel();
		street_sign.initModel();
		traffic_light_5_frame.initModel();
		ytraffic_light_5_frame.initModel();
		otraffic_light_5_frame.initModel();
		traffic_light_doghouse_frame.initModel();
		ytraffic_light_doghouse_frame.initModel();
		otraffic_light_doghouse_frame.initModel();
		traffic_light_1_frame.initModel();
		ytraffic_light_1_frame.initModel();
		otraffic_light_1_frame.initModel();
		traffic_light_2_frame.initModel();
		ytraffic_light_2_frame.initModel();
		otraffic_light_2_frame.initModel();
		traffic_light_4_frame.initModel();
		ytraffic_light_4_frame.initModel();
		otraffic_light_4_frame.initModel();
		traffic_light_6_frame.initModel();
		ytraffic_light_6_frame.initModel();
		otraffic_light_6_frame.initModel();
	}
}
