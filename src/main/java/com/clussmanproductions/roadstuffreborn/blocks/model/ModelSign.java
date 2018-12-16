package com.clussmanproductions.roadstuffreborn.blocks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class ModelSign implements IModel {

	ArrayList<ResourceLocation> textures = new ArrayList<>(); 
	public ModelSign()
	{
		for(int i = 0; i <= 112; i++)
		{
			textures.add(new ResourceLocation(ModRoadStuffReborn.MODID, "blocks/sign/circle/circle" + i));
		}
		
		for(int i = 0; i <= 115; i++)
		{
			textures.add(new ResourceLocation(ModRoadStuffReborn.MODID, "blocks/sign/diamond/diamond" + i));
		}
		
		for(int i = 0; i <= 75; i++)
		{
			textures.add(new ResourceLocation(ModRoadStuffReborn.MODID, "blocks/sign/misc/misc" + i));
		}
		
		
	}
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new BakedModelSign(format);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptySet();
	}
	
	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of(
				new ResourceLocation(ModRoadStuffReborn.MODID, "blocks/generic"),
				new ResourceLocation(ModRoadStuffReborn.MODID, "blocks/silver"));
	}
}
