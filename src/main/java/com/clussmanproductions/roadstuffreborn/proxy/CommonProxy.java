package com.clussmanproductions.roadstuffreborn.proxy;

import com.clussmanproductions.roadstuffreborn.ModBlocks;
import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.ModSounds;
import com.clussmanproductions.roadstuffreborn.blocks.BlockChannelizer;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCone;
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
import com.clussmanproductions.roadstuffreborn.blocks.BlockDrum;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverhead;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadCrossbuck;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadLamps;
import com.clussmanproductions.roadstuffreborn.blocks.BlockOverheadPole;
import com.clussmanproductions.roadstuffreborn.blocks.BlockSafetranMechanical;
import com.clussmanproductions.roadstuffreborn.blocks.BlockSafetranType3;
import com.clussmanproductions.roadstuffreborn.blocks.BlockSign;
import com.clussmanproductions.roadstuffreborn.blocks.BlockStreetLightSingle;
import com.clussmanproductions.roadstuffreborn.gui.GuiProxy;
import com.clussmanproductions.roadstuffreborn.item.ItemCrossingRelayBox;
import com.clussmanproductions.roadstuffreborn.item.ItemCrossingRelayTuner;
import com.clussmanproductions.roadstuffreborn.network.PacketHandler;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.RelayTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.SafetranMechanicalTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.SafetranType3TileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.SignTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundEvent;
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
		
		GameRegistry.registerTileEntity(CrossingGateGateTileEntity.class, ModRoadStuffReborn.MODID + "_crossinggategate");
		GameRegistry.registerTileEntity(SafetranType3TileEntity.class, ModRoadStuffReborn.MODID + "_safetrantyp3");
		GameRegistry.registerTileEntity(RelayTileEntity.class, ModRoadStuffReborn.MODID + "_relay");
		GameRegistry.registerTileEntity(SafetranMechanicalTileEntity.class, ModRoadStuffReborn.MODID + "_safetranmechanical");
		GameRegistry.registerTileEntity(SignTileEntity.class, ModRoadStuffReborn.MODID + "_sign");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().register(new ItemCrossingRelayBox());
		e.getRegistry().register(new ItemCrossingRelayTuner());
		
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
	}
	
	public static void registerSounds(RegistryEvent.Register<SoundEvent> e)
	{
		e.getRegistry().register(ModSounds.gateEvent);
		e.getRegistry().register(ModSounds.safetranType3Event);
		e.getRegistry().register(ModSounds.safetranMechanicalEvent);
	}
	
	public void preInit(FMLPreInitializationEvent e)
	{
		ModSounds.initSounds();
		PacketHandler.registerMessages("roadstuffreborn");
	}
	
	public void init(FMLInitializationEvent e)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(ModRoadStuffReborn.instance, new GuiProxy());
	}
	
	public void postInit(FMLPostInitializationEvent e)
	{
		
	}
}
