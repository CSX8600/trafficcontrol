package com.clussmanproductions.roadstuffreborn;

import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateBase;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateCrossbuck;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateGate;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGatePole;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayNE;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayNW;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelaySE;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelaySW;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayTopNE;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayTopNW;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayTopSE;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingRelayTopSW;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverhead;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadCrossbuck;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadLamps;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadPole;
import com.clussmanproductions.roadstuffreborn.blocks.BlockSafetranMechanical;
import com.clussmanproductions.roadstuffreborn.blocks.BlockSafetranType3;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder("roadstuffreborn")
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
	}
}
