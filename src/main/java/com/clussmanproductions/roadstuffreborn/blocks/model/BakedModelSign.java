package com.clussmanproductions.roadstuffreborn.blocks.model;

import java.util.ArrayList;
import java.util.List;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class BakedModelSign implements IBakedModel {
	private VertexFormat format;
	private TextureAtlasSprite generic;
	
	private TextureAtlasSprite getGeneric()
	{
		if (generic == null)
		{
			generic = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModRoadStuffReborn.MODID + ":blocks/generic");
		}
		
		return generic;
	}
	
	public BakedModelSign(VertexFormat format)
	{
		this.format = format;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> retval = new ArrayList<BakedQuad>();
		
		UVMapping twoBy16Generic = new UVMapping(
				2, 0,
				2, 16,
				0, 16,
				0, 0);
		
		UVMapping twoBy2Generic = new UVMapping(
				2, 0,
				2, 2,
				0, 2,
				0, 0);
				
		
		retval.add(createQuad(v(0.5625, 1, 0.4375), v(0.5625, 0, 0.4375), v(0.4375, 0, 0.4375), v(0.4375, 1, 0.4375), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(0.5625, 1, 0.5625), v(0.5625, 0, 0.5625), v(0.5625, 0, 0.4375), v(0.5625, 1, 0.4375), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(0.4375, 1, 0.5625), v(0.4375, 0, 0.5625), v(0.5625, 0, 0.5625), v(0.5625, 1, 0.5625), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(0.4375, 1, 0.4375), v(0.4375, 0, 0.4375), v(0.4375, 0, 0.5625), v(0.4375, 1, 0.5625), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(0.5625, 1, 0.5625), v(0.5625, 1, 0.4375), v(0.4375, 1, 0.4375), v(0.4375, 1, 0.5625), getGeneric(), twoBy2Generic));
		retval.add(createQuad(v(0.5625, 0, 0.4375), v(0.5625, 0, 0.5625), v(0.4375, 0, 0.5625), v(0.4375, 0, 0.4375), getGeneric(), twoBy2Generic));
		
		
		
		return retval;		
	}
	
    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal,
            double x, double y, double z, float u, float v, TextureAtlasSprite sprite)
    {
		for (int e = 0; e < format.getElementCount(); e++) 
		{		
			switch (format.getElement(e).getUsage()) 
			{		
				 case POSITION:
				     builder.put(e, (float)x, (float)y, (float)z, 1.0f);
				     break;
				 case UV:
				     if (format.getElement(e).getIndex() == 0) {
				         u = sprite.getInterpolatedU(u);
				         v = sprite.getInterpolatedV(v);
				         builder.put(e, u, v, 0f, 1f);
				         break;
				     }
				 case COLOR:
					 builder.put(e, 1f, 1f, 1f, 1f);
					 break;
				 case NORMAL:
				     builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
				     break;
				 default:
				     builder.put(e);
				     break;
			}
		}
	}
	
	private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, UVMapping uvMapping)
	{
		Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
		
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setTexture(sprite);
		putVertex(builder, normal, v1.x, v1.y, v1.z, uvMapping.v1u, uvMapping.v1v, sprite);
		putVertex(builder, normal, v2.x, v2.y, v2.z, uvMapping.v2u, uvMapping.v2v, sprite);
		putVertex(builder, normal, v3.x, v3.y, v3.z, uvMapping.v3u, uvMapping.v3v, sprite);
		putVertex(builder, normal, v4.x, v4.y, v4.z, uvMapping.v4u, uvMapping.v4v, sprite);
		return builder.build();
	}
	
	private static Vec3d v(double x, double y, double z) {
		return new Vec3d(x, y, z);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModRoadStuffReborn.MODID + ":blocks/generic");
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
	
	private class UVMapping
	{
		public int v1u;
		public int v1v;
		public int v2u;
		public int v2v;
		public int v3u;
		public int v3v;
		public int v4u;
		public int v4v;
		
		public UVMapping(int v1u, int v1v, int v2u, int v2v, int v3u, int v3v, int v4u, int v4v)
		{
			this.v1u = v1u;
			this.v1v = v1v;
			this.v2u = v2u;
			this.v2v = v2v;
			this.v3u = v3u;
			this.v3v = v3v;
			this.v4u = v4u;
			this.v4v = v4v;
		}
	}
}
