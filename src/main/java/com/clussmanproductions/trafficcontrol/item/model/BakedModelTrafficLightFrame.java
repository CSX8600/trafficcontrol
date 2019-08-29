package com.clussmanproductions.trafficcontrol.item.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

public class BakedModelTrafficLightFrame implements IBakedModel {

	private IModelState state;
	private VertexFormat format;
	private Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		IBakedModel frameModel = ModelLoaderRegistry.getModel(new ModelResourceLocation("trafficcontrol:item/traffic_light_frame")).bake(state, format, bakedTextureGetter);
		
		ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>(frameModel.getQuads(state, side, rand));
		
		return quads;							
	}

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		// TODO Auto-generated method stub
		return null;
	}

}
