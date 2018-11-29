package com.clussmanproductions.roadstuffreborn.proxy;

import com.clussmanproductions.roadstuffreborn.ModBlocks;
import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.ModSounds;
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
import com.clussmanproductions.roadstuffreborn.blocks.BlockSafetranType3;
import com.clussmanproductions.roadstuffreborn.item.ItemCrossingRelayBox;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateLampsTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.SafetranType3TileEntity;

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
		
		GameRegistry.registerTileEntity(CrossingGateGateTileEntity.class, ModRoadStuffReborn.MODID + "_crossinggategate");
		GameRegistry.registerTileEntity(CrossingGateLampsTileEntity.class, ModRoadStuffReborn.MODID + "_crossinggatelamps");
		GameRegistry.registerTileEntity(SafetranType3TileEntity.class, ModRoadStuffReborn.MODID + "_safetrantyp3");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().register(new ItemCrossingRelayBox());
		
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_base).setRegistryName(ModBlocks.crossing_gate_base.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_gate).setRegistryName(ModBlocks.crossing_gate_gate.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_lamps).setRegistryName(ModBlocks.crossing_gate_lamps.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_pole).setRegistryName(ModBlocks.crossing_gate_pole.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.crossing_gate_crossbuck).setRegistryName(ModBlocks.crossing_gate_crossbuck.getRegistryName()));
		e.getRegistry().register(new ItemBlock(ModBlocks.safetran_type_3).setRegistryName(ModBlocks.safetran_type_3.getRegistryName()));
	}
	
	public static void registerSounds(RegistryEvent.Register<SoundEvent> e)
	{
		e.getRegistry().register(ModSounds.gateEvent);
		e.getRegistry().register(ModSounds.safetranType3Event);
	}
	
	public void preInit(FMLPreInitializationEvent e)
	{
		ModSounds.initSounds();
	}
	
	public void init(FMLInitializationEvent e)
	{
		
	}
	
	public void postInit(FMLPostInitializationEvent e)
	{
		
	}
}
