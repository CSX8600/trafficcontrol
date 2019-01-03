package com.clussmanproductions.roadstuffreborn.blocks.model;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public abstract class BaseBakedModel implements IBakedModel {
	VertexFormat format;
	public BaseBakedModel(VertexFormat format)
	{
		this.format = format;
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
	
	protected BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, UVMapping uvMapping)
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
	
	protected ArrayList<BakedQuad> createBox(double lowerX, double lowerY, double lowerZ, double width, double height, double depth, BoxTextureCollection collection)
	{
		ArrayList<BakedQuad> retVal = new ArrayList<BakedQuad>();
		
		retVal.add(createQuad(v(lowerX + width, lowerY + height, lowerZ), v(lowerX + width, lowerY, lowerZ), v(lowerX, lowerY, lowerZ), v(lowerX, lowerY + height, lowerZ), collection.south, collection.southMapping));
		retVal.add(createQuad(v(lowerX + width, lowerY + height, lowerZ + depth), v(lowerX + width, lowerY, lowerZ + depth), v(lowerX + width, lowerY, lowerZ), v(lowerX + width, lowerY + height, lowerZ), collection.west, collection.westMapping));
		retVal.add(createQuad(v(lowerX, lowerY + height, lowerZ + depth), v(lowerX, lowerY, lowerZ + depth), v(lowerX + width, lowerY, lowerZ + depth), v(lowerX + width, lowerY + height, lowerZ + depth), collection.north, collection.northMapping));
		retVal.add(createQuad(v(lowerX, lowerY + height, lowerZ), v(lowerX, lowerY, lowerZ), v(lowerX, lowerY, lowerZ + depth), v(lowerX, lowerY + height, lowerZ + depth), collection.east, collection.eastMapping));
		retVal.add(createQuad(v(lowerX + width, lowerY + height, lowerZ + depth), v(lowerX + width, lowerY + height, lowerZ), v(lowerX, lowerY + height, lowerZ), v(lowerX, lowerY + height, lowerZ + depth), collection.up, collection.upMapping));
		retVal.add(createQuad(v(lowerX + width, lowerY, lowerZ), v(lowerX + width, lowerY, lowerZ + depth), v(lowerX, lowerY, lowerZ + depth), v(lowerX, lowerY, lowerZ), collection.down, collection.downMapping));
		
		return retVal;
	}

	protected ArrayList<BakedQuad> createBoxWithXRotation(double lowerX, double lowerY, double lowerZ, double width, double height, int depth, int rotation, BoxTextureCollection collection)
	{
		ArrayList<BakedQuad> retVal = new ArrayList<BakedQuad>();
		
		double rads = Math.toRadians(rotation);
		double oppositeRads = Math.toRadians(rotation + 90);
		
		double x1 = lowerX;
		double y1 = lowerY;
		
		double x2 = width * Math.cos(rads) + x1;
		double y2 = width * Math.sin(rads) + y1;		
		
		double x3 = height * Math.cos(oppositeRads) + x2;
		double y3 = height * Math.sin(oppositeRads) + y2;
		
		double x4 = height * Math.cos(oppositeRads) + x1;
		double y4 = height * Math.sin(oppositeRads) + y1;
		
		retVal.add(createQuad(v(x3, y3, lowerZ), v(x2, y2, lowerZ), v(x1, y1, lowerZ), v(x4, y4, lowerZ), collection.south, collection.southMapping));
		retVal.add(createQuad(v(x3, y3, lowerZ + depth), v(x2, y2, lowerZ + depth), v(x2, y2, lowerZ), v(x3, y3, lowerZ), collection.west, collection.westMapping));
		retVal.add(createQuad(v(x4, y4, lowerZ + depth), v(x1, y1, lowerZ + depth), v(x2, y2, lowerZ + depth), v(x3, y3, lowerZ + depth), collection.north, collection.northMapping));
		retVal.add(createQuad(v(x4, y4, lowerZ), v(x1, y1, lowerZ), v(x1, y1, lowerZ + depth), v(x4, y4, lowerZ + depth), collection.east, collection.eastMapping));
		retVal.add(createQuad(v(x3, y3, lowerZ + depth), v(x3, y3, lowerZ), v(x4, y4, lowerZ), v(x4, y4, lowerZ + depth), collection.up, collection.upMapping));
		retVal.add(createQuad(v(x2, y2, lowerZ), v(x2, y2, lowerZ + depth), v(x1, y1, lowerZ + depth), v(x1, y1, lowerZ), collection.down, collection.downMapping));
		
		return retVal;
	}
	
	protected ArrayList<BakedQuad> createBoxWithZRotation(double lowerX, double lowerY, double lowerZ, double width, double height, int depth, int rotation, BoxTextureCollection collection)
	{
		ArrayList<BakedQuad> retVal = new ArrayList<BakedQuad>();
		
		double rads = Math.toRadians(rotation);
		double oppositeRads = Math.toRadians(rotation + 90);
		
		double z1 = lowerZ;
		double y1 = lowerY;
		
		double z2 = depth * Math.cos(rads) + z1;
		double y2 = depth * Math.sin(rads) + y1;		
		
		double z3 = height * Math.cos(oppositeRads) + z2;
		double y3 = height * Math.sin(oppositeRads) + y2;
		
		double z4 = height * Math.cos(oppositeRads) + z1;
		double y4 = height * Math.sin(oppositeRads) + y1;
		
		retVal.add(createQuad(v(lowerX + width, y3, z3), v(lowerX + width, y2, z2), v(lowerX + width, y1, z1), v(lowerX + width, y4, z4), collection.south, collection.southMapping));
		retVal.add(createQuad(v(lowerX, y3, z3), v(lowerX, y2, z2), v(lowerX + width, y2, z2), v(lowerX + width, y3, z3), collection.west, collection.westMapping));
		retVal.add(createQuad(v(lowerX, y4, z4), v(lowerX, y1, z1), v(lowerX, y2, z2), v(lowerX, y3, z3), collection.north, collection.northMapping));
		retVal.add(createQuad(v(lowerX + width, y4, z4), v(lowerX + width, y1, z1), v(lowerX, y1, z1), v(lowerX, y4, z4), collection.east, collection.eastMapping));
		retVal.add(createQuad(v(lowerX, y3, z3), v(lowerX + width, y3, z3), v(lowerX + width, y4, z4), v(lowerX, y4, z4), collection.up, collection.upMapping));
		retVal.add(createQuad(v(lowerX + width, y2, z2), v(lowerX, y2, z2), v(lowerX, y1, z1), v(lowerX + width, y1, z1), collection.down, collection.downMapping));
		
		return retVal;
	}
	
 	protected static Vec3d v(double x, double y, double z) {
		return new Vec3d(x / 16, y / 16, z / 16);
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
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
	
	public class UVMapping
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

	public class BoxTextureCollection
	{
		TextureAtlasSprite north;
		TextureAtlasSprite east;
		TextureAtlasSprite south;
		TextureAtlasSprite west;
		TextureAtlasSprite up;
		TextureAtlasSprite down;
		UVMapping northMapping;
		UVMapping eastMapping;
		UVMapping southMapping;
		UVMapping westMapping;
		UVMapping upMapping;
		UVMapping downMapping;
		
		public BoxTextureCollection(TextureAtlasSprite north, TextureAtlasSprite east, TextureAtlasSprite south, TextureAtlasSprite west,
				TextureAtlasSprite up, TextureAtlasSprite down, UVMapping northMapping, UVMapping eastMapping, UVMapping southMapping, UVMapping westMapping,
				UVMapping upMapping, UVMapping downMapping)
		{
			this.north = north;
			this.east = east;
			this.south = south;
			this.west = west;
			this.up = up;
			this.down = down;
			this.northMapping = northMapping;
			this.eastMapping = eastMapping;
			this.southMapping = southMapping;
			this.westMapping = westMapping;
			this.upMapping = upMapping;
			this.downMapping = downMapping;
		}
	}
}
