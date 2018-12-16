package com.clussmanproductions.roadstuffreborn.blocks.model;

import java.util.Set;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader {
	private final Set<String> HANDLEABLE_NAMES = ImmutableSet.of("sign");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (!(modelLocation instanceof ModelResourceLocation) || !modelLocation.getResourceDomain().equals(ModRoadStuffReborn.MODID))
		{
			return false;
		}
		
		ModelResourceLocation modelResourceLocation = (ModelResourceLocation)modelLocation;
		if (modelResourceLocation.getVariant() == "inventory")
		{
			return false;
		}
		
		return HANDLEABLE_NAMES.contains(modelResourceLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return new ModelSign();
	}

}
