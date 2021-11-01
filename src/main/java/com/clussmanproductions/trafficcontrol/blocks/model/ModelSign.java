package com.clussmanproductions.trafficcontrol.blocks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity.Sign;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSign implements IModel {

	ArrayList<ResourceLocation> textures = new ArrayList<>(); 
	public ModelSign()
	{
		SignTileEntity.initializeSigns();
		for(Sign sign : SignTileEntity.SIGNS_BY_TYPE_VARIANT.values())
		{
			String path = sign.getImageResourceLocation().getResourcePath();
			path = path.substring(path.indexOf('/') + 1);
			path = path.substring(0, path.lastIndexOf('.'));
			textures.add(getRL(path));
		}
		
		textures.add(getRL("blocks/sign/misc/misc0B"));
		textures.add(getRL("blocks/sign/misc/misc1B"));
		textures.add(getRL("blocks/sign/misc/misc2B"));
		textures.add(getRL("blocks/sign/misc/misc3B"));
		textures.add(getRL("blocks/sign/misc/misc4B"));
		textures.add(getRL("blocks/sign/misc/misc5B"));
		textures.add(getRL("blocks/sign/misc/misc6B"));
		textures.add(getRL("blocks/sign/misc/misc7B"));
		textures.add(getRL("blocks/sign/misc/misc8B"));
//		
//		for(int i = 0; i <= 89; i++)
//		{
//			textures.add(getRL("blocks/sign/rectangle/rectangle" + i));
//		}
//		
//		for(int i = 0; i <= 166; i++)
//		{
//			textures.add(getRL("blocks/sign/square/square" + i));
//		}
//		
//		for(int i = 0; i <= 95; i++)
//		{
//			textures.add(getRL("blocks/sign/triangle/triangle" + i));
//		}
	}
	
	private ResourceLocation getRL(String name)
	{
		return new ResourceLocation(ModTrafficControl.MODID, name);
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
		return textures;
	}
}
