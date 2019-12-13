package com.clussmanproductions.trafficcontrol.proxy;

import java.io.File;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModSounds;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockChannelizer;
import com.clussmanproductions.trafficcontrol.blocks.BlockCone;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateCrossbuck;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateGate;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGatePole;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayNE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayNW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelaySE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelaySW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopNE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopNW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopSE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopSW;
import com.clussmanproductions.trafficcontrol.blocks.BlockDrum;
import com.clussmanproductions.trafficcontrol.blocks.BlockLightSource;
import com.clussmanproductions.trafficcontrol.blocks.BlockOverhead;
import com.clussmanproductions.trafficcontrol.blocks.BlockOverheadCrossbuck;
import com.clussmanproductions.trafficcontrol.blocks.BlockOverheadLamps;
import com.clussmanproductions.trafficcontrol.blocks.BlockOverheadPole;
import com.clussmanproductions.trafficcontrol.blocks.BlockSafetranMechanical;
import com.clussmanproductions.trafficcontrol.blocks.BlockSafetranType3;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntBorder;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntIsland;
import com.clussmanproductions.trafficcontrol.blocks.BlockSign;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightDouble;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightSingle;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockType3Barrier;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLightControlBox;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayBox;
import com.clussmanproductions.trafficcontrol.item.ItemCrossingRelayTuner;
import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightBulb;
import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.RelayTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SafetranMechanicalTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SafetranType3TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.ShuntBorderTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.ShuntIslandTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightDoubleTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightSingleTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.WigWagTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class CommonProxy {
	public static Configuration config;
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(new BlockCrossingGateBase());
		e.getRegistry().register(new BlockCrossingGateGate());
		e.getRegistry().register(new BlockCrossingGateLamps());
		e.getRegistry().register(new BlockCrossingGatePole());
		e.getRegistry().register(new BlockCrossingGateCrossbuck());
		e.getRegistry().register(new BlockSafetranType3());
		e.getRegistry().register(new BlockCrossingRelaySE());
		e.getRegistry().register(new BlockCrossingRelaySW());
		e.getRegistry().register(new BlockCrossingRelayNW());
		e.getRegistry().register(new BlockCrossingRelayNE());
		e.getRegistry().register(new BlockCrossingRelayTopSE());
		e.getRegistry().register(new BlockCrossingRelayTopSW());
		e.getRegistry().register(new BlockCrossingRelayTopNW());
		e.getRegistry().register(new BlockCrossingRelayTopNE());
		e.getRegistry().register(new BlockOverheadPole());
		e.getRegistry().register(new BlockOverhead());
		e.getRegistry().register(new BlockOverheadLamps());
		e.getRegistry().register(new BlockOverheadCrossbuck());
		e.getRegistry().register(new BlockSafetranMechanical());
		e.getRegistry().register(new BlockSign());
		e.getRegistry().register(new BlockCone());
		e.getRegistry().register(new BlockChannelizer());
		e.getRegistry().register(new BlockDrum());
		e.getRegistry().register(new BlockStreetLightSingle());
		e.getRegistry().register(new BlockLightSource());
		e.getRegistry().register(new BlockStreetLightDouble());
		e.getRegistry().register(new BlockTrafficLight());
		e.getRegistry().register(new BlockTrafficLightControlBox());
		e.getRegistry().register(new BlockWigWag());
		e.getRegistry().register(new BlockShuntBorder());
		e.getRegistry().register(new BlockShuntIsland());
		e.getRegistry().register(new BlockType3Barrier());

		GameRegistry.registerTileEntity(CrossingGateGateTileEntity.class, ModTrafficControl.MODID + "_crossinggategate");
		GameRegistry.registerTileEntity(SafetranType3TileEntity.class, ModTrafficControl.MODID + "_safetrantyp3");
		GameRegistry.registerTileEntity(RelayTileEntity.class, ModTrafficControl.MODID + "_relay");
		GameRegistry.registerTileEntity(SafetranMechanicalTileEntity.class, ModTrafficControl.MODID + "_safetranmechanical");
		GameRegistry.registerTileEntity(SignTileEntity.class, ModTrafficControl.MODID + "_sign");
		GameRegistry.registerTileEntity(StreetLightSingleTileEntity.class, ModTrafficControl.MODID + "_streetsignsingle");
		GameRegistry.registerTileEntity(StreetLightDoubleTileEntity.class, ModTrafficControl.MODID + "_streetlightdouble");
		GameRegistry.registerTileEntity(TrafficLightTileEntity.class, ModTrafficControl.MODID + "_trafficlight");
		GameRegistry.registerTileEntity(TrafficLightControlBoxTileEntity.class, ModTrafficControl.MODID + "_trafficlightcontrolbox");
		GameRegistry.registerTileEntity(WigWagTileEntity.class, ModTrafficControl.MODID + "_wigwag");
		GameRegistry.registerTileEntity(ShuntBorderTileEntity.class, ModTrafficControl.MODID + "_shuntborder");
		GameRegistry.registerTileEntity(ShuntIslandTileEntity.class, ModTrafficControl.MODID + "_shuntisland");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().register(new ItemCrossingRelayBox());
		e.getRegistry().register(new ItemCrossingRelayTuner());
		e.getRegistry().register(new ItemTrafficLightBulb());
		e.getRegistry().register(new ItemTrafficLightFrame());

		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_base).setRegistryName(ModBlocks.crossing_gate_base.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_gate).setRegistryName(ModBlocks.crossing_gate_gate.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_lamps).setRegistryName(ModBlocks.crossing_gate_lamps.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_pole).setRegistryName(ModBlocks.crossing_gate_pole.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_crossbuck).setRegistryName(ModBlocks.crossing_gate_crossbuck.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.safetran_type_3).setRegistryName(ModBlocks.safetran_type_3.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.overhead_pole).setRegistryName(ModBlocks.overhead_pole.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.overhead).setRegistryName(ModBlocks.overhead.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.overhead_lamps).setRegistryName(ModBlocks.overhead_lamps.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.overhead_crossbuck).setRegistryName(ModBlocks.overhead_crossbuck.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.safetran_mechanical).setRegistryName(ModBlocks.safetran_mechanical.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.sign).setRegistryName(ModBlocks.sign.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.cone).setRegistryName(ModBlocks.cone.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.channelizer).setRegistryName(ModBlocks.channelizer.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.drum).setRegistryName(ModBlocks.drum.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.street_light_single).setRegistryName(ModBlocks.street_light_single.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.street_light_double).setRegistryName(ModBlocks.street_light_double.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.traffic_light_control_box).setRegistryName(ModBlocks.traffic_light_control_box.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.wig_wag).setRegistryName(ModBlocks.wig_wag.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.shunt_border).setRegistryName(ModBlocks.shunt_border.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.shunt_island).setRegistryName(ModBlocks.shunt_island.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.type_3_barrier).setRegistryName(ModBlocks.type_3_barrier.getRegistryName()));
	}

	public static void registerSounds(RegistryEvent.Register<SoundEvent> e)
	{
		e.getRegistry().register(ModSounds.gateEvent);
		e.getRegistry().register(ModSounds.safetranType3Event);
		e.getRegistry().register(ModSounds.safetranMechanicalEvent);
	}

	public void preInit(FMLPreInitializationEvent e)
	{
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "trafficcontrol.cfg"));
		Config.readConfig();

		ModSounds.initSounds();
		PacketHandler.registerMessages("trafficcontrol");
	}

	public void init(FMLInitializationEvent e)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(ModTrafficControl.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e)
	{

	}
}
