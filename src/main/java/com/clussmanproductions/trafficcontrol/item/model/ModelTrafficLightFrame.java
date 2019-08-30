package com.clussmanproductions.trafficcontrol.item.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class ModelTrafficLightFrame implements IModel {

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new BakedModelTrafficLightFrame(state, format, bakedTextureGetter);
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.singleton(new ModelResourceLocation("trafficcontrol:item/traffic_light_frame_base"));
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		HashSet<ResourceLocation> textures = new HashSet<ResourceLocation>();
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/green.png"));
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/yellow_solid.png"));
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/red.png"));
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/green_arrow_left.png"));
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/yellow_arrow_left.png"));
		textures.add(new ResourceLocation("trafficcontrol:textures/blocks/red_arrow_left.png"));
		
		return textures;
	}
}
