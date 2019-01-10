package com.clussmanproductions.trafficcontrol.blocks.model;

import java.util.Collection;
import java.util.function.Function;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class ModelStreetLightSingle implements IModel {

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new BakedModelStreetLightSingle(format);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.<ResourceLocation>builder()
				.add(new ResourceLocation(ModTrafficControl.MODID + ":blocks/generic"))
				.add(new ResourceLocation(ModTrafficControl.MODID + ":blocks/yellow"))
				.build();
	}
}
