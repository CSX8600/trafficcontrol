package com.clussmanproductions.roadstuffreborn.proxy;

import com.clussmanproductions.roadstuffreborn.ModBlocks;
import com.clussmanproductions.roadstuffreborn.ModItems;
import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@SubscribeEvent
	public void preInit(FMLPreInitializationEvent e)
	{
		ModelLoaderRegistry.registerLoader(new com.clussmanproductions.roadstuffreborn.blocks.model.ModelLoader());
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e)
	{
		ModBlocks.initModels(e);
		ModItems.initModels(e);
	}
}
