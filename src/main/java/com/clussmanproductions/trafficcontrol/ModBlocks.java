package com.clussmanproductions.trafficcontrol;

import com.clussmanproductions.trafficcontrol.blocks.*;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder("trafficcontrol")
public class ModBlocks {
	@ObjectHolder("crossing_gate_base")
	public static BlockCrossingGateBase crossing_gate_base;
	@ObjectHolder("crossing_gate_gate")
	public static BlockCrossingGateGate crossing_gate_gate;
	@ObjectHolder("crossing_gate_lamps")
	public static BlockCrossingGateLamps crossing_gate_lamps;
	@ObjectHolder("crossing_gate_pole")
	public static BlockCrossingGatePole crossing_gate_pole;
	@ObjectHolder("crossing_gate_crossbuck")
	public static BlockCrossingGateCrossbuck crossing_gate_crossbuck;
	@ObjectHolder("safetran_type_3")
	public static BlockSafetranType3 safetran_type_3;
	@ObjectHolder("crossing_relay_se")
	public static BlockCrossingRelaySE crossing_relay_se;
	@ObjectHolder("crossing_relay_sw")
	public static BlockCrossingRelaySW crossing_relay_sw;
	@ObjectHolder("crossing_relay_nw")
	public static BlockCrossingRelayNW crossing_relay_nw;
	@ObjectHolder("crossing_relay_ne")
	public static BlockCrossingRelayNE crossing_relay_ne;
	@ObjectHolder("crossing_relay_top_sw")
	public static BlockCrossingRelayTopSW crossing_relay_top_sw;
	@ObjectHolder("crossing_relay_top_se")
	public static BlockCrossingRelayTopSE crossing_relay_top_se;
	@ObjectHolder("crossing_relay_top_nw")
	public static BlockCrossingRelayTopNW crossing_relay_top_nw;
	@ObjectHolder("crossing_relay_top_ne")
	public static BlockCrossingRelayTopNE crossing_relay_top_ne;
	@ObjectHolder("overhead_pole")
	public static BlockOverheadPole overhead_pole;
	@ObjectHolder("overhead")
	public static BlockOverhead overhead;
	@ObjectHolder("overhead_lamps")
	public static BlockOverheadLamps overhead_lamps;
	@ObjectHolder("overhead_crossbuck")
	public static BlockOverheadCrossbuck overhead_crossbuck;
	@ObjectHolder("safetran_mechanical")
	public static BlockSafetranMechanical safetran_mechanical;
	@ObjectHolder("sign")
	public static BlockSign sign;
	@ObjectHolder("cone")
	public static BlockCone cone;
	@ObjectHolder("channelizer")
	public static BlockChannelizer channelizer;
	@ObjectHolder("drum")
	public static BlockDrum drum;
	@ObjectHolder("street_light_single")
	public static BlockStreetLightSingle street_light_single;
	@ObjectHolder("light_source")
	public static BlockLightSource light_source;
	@ObjectHolder("street_light_double")
	public static BlockStreetLightDouble street_light_double;
	@ObjectHolder("traffic_light")
	public static BlockTrafficLight traffic_light;
	@ObjectHolder("traffic_light_control_box")
	public static BlockTrafficLightControlBox traffic_light_control_box;
	@ObjectHolder("wig_wag")
	public static BlockWigWag wig_wag;
	@ObjectHolder("shunt_border")
	public static BlockShuntBorder shunt_border;
	@ObjectHolder("shunt_island")
	public static BlockShuntIsland shunt_island;
	@ObjectHolder("type_3_barrier")
	public static BlockType3Barrier type_3_barrier;
	@ObjectHolder("type_3_barrier_right")
	public static BlockType3BarrierRight type_3_barrier_right;
	@ObjectHolder("traffic_rail")
	public static BlockTrafficRail traffic_rail;
	@ObjectHolder("concrete_barrier")
	public static BlockConcreteBarrier concrete_barrier;
	@ObjectHolder("horizontal_pole")
	public static BlockHorizontalPole horizontal_pole;
	@ObjectHolder("wch_bell")
	public static BlockWCHBell wch_bell;
	@ObjectHolder("traffic_sensor_left")
	public static BlockTrafficSensorLeft traffic_sensor_left;
	@ObjectHolder("traffic_sensor_straight")
	public static BlockTrafficSensorStraight traffic_sensor_straight;
	@ObjectHolder("street_sign")
	public static BlockStreetSign street_sign;
	@ObjectHolder("traffic_light_5")
	public static BlockTrafficLight5 traffic_light_5;
	@ObjectHolder("traffic_light_5_upper")
	public static BlockTrafficLight5Upper traffic_light_5_upper;
	@ObjectHolder("traffic_light_doghouse")
	public static BlockTrafficLightDoghouse traffic_light_doghouse;
	@ObjectHolder("traffic_light_1")
	public static BlockTrafficLight1 traffic_light_1;
	@ObjectHolder("traffic_light_2")
	public static BlockTrafficLight2 traffic_light_2;
	@ObjectHolder("traffic_light_4")
	public static BlockTrafficLight4 traffic_light_4;
	@ObjectHolder("traffic_light_6")
	public static BlockTrafficLight6 traffic_light_6;
	@ObjectHolder("traffic_light_7")
	public static BlockTrafficLight7 traffic_light_7;
	
	@ObjectHolder("pedestrian_button")
	public static BlockPedestrianButton pedestrian_button;
	@ObjectHolder("traffic_sensor_right")
	public static BlockTrafficSensorRight traffic_sensor_right;
	

	@SideOnly(Side.CLIENT)
	public static void initModels(ModelRegistryEvent e)
	{
		crossing_gate_base.initModel();
		crossing_gate_gate.initModel();
		crossing_gate_lamps.initModel();
		crossing_gate_pole.initModel();
		crossing_gate_crossbuck.initModel();
		safetran_type_3.initModel();
		overhead_pole.initModel();
		overhead.initModel();
		overhead_lamps.initModel();
		overhead_crossbuck.initModel();
		safetran_mechanical.initModel();
		sign.initModel();
		cone.initModel();
		channelizer.initModel();
		drum.initModel();
		street_light_single.initModel();
		street_light_double.initModel();
		traffic_light.initModel();
		traffic_light_control_box.initModel();
		wig_wag.initModel();
		shunt_border.initModel();
		shunt_island.initModel();
		type_3_barrier.initModel();
		type_3_barrier_right.initModel();
		traffic_rail.initModel();
		concrete_barrier.initModel();
		horizontal_pole.initModel();
		wch_bell.initModel();
		traffic_sensor_left.initModel();
		traffic_sensor_straight.initModel();
		street_sign.initModel();
		traffic_light_5.initModel();
		traffic_light_doghouse.initModel();
		traffic_light_1.initModel();
		traffic_light_2.initModel();
		traffic_light_4.initModel();
		pedestrian_button.initModel();
		traffic_sensor_right.initModel();
		traffic_light_6.initModel();
		traffic_light_7.initModel();
		
	}
}
