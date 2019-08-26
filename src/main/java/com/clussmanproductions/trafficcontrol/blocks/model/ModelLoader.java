package com.clussmanproductions.trafficcontrol.blocks.model;

import java.util.Set;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader {
	private final Set<String> HANDLEABLE_NAMES = ImmutableSet.of(
			"sign",
			"street_light_single",
			"street_light_double");
	private final Set<String> HANDLEABLE_INVENTORY_MODELS = ImmutableSet.of(
			"sign"
			/*"traffic_light_bulb"*/);

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (!(modelLocation instanceof ModelResourceLocation) || !modelLocation.getResourceDomain().equals(ModTrafficControl.MODID))
		{
			return false;
		}
		
		ModelResourceLocation modelResourceLocation = (ModelResourceLocation)modelLocation;
		if (modelResourceLocation.getVariant().equals("inventory"))
		{
			return HANDLEABLE_INVENTORY_MODELS.contains(modelResourceLocation.getResourcePath());
		}
		
		return HANDLEABLE_NAMES.contains(modelResourceLocation.getResourcePath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		ModelResourceLocation resourceLocation = (ModelResourceLocation)modelLocation;
		switch(resourceLocation.getResourcePath())
		{
			case "sign":
				return new ModelSign();
			case "street_light_single":
				return new ModelStreetLightSingle();
			case "street_light_double":
				return new ModelStreetLightDouble();
		}
		
		throw new Exception("Model not found");
	}

}
