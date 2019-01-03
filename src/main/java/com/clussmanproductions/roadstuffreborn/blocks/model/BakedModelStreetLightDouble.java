package com.clussmanproductions.roadstuffreborn.blocks.model;

import java.util.ArrayList;
import java.util.List;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.blocks.BlockStreetLightSingle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class BakedModelStreetLightDouble extends BaseBakedModel {

	TextureAtlasSprite generic;
	TextureAtlasSprite lamp;
	BoxTextureCollection postThickBoxTextureCollection;
	BoxTextureCollection postThinBoxTextureCollection;
	BoxTextureCollection armBoxTextureCollection;
	BoxTextureCollection lampBoxTextureCollection;
	public BakedModelStreetLightDouble(VertexFormat format) {
		super(format);
		
		generic = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModRoadStuffReborn.MODID + ":blocks/generic");
		lamp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModRoadStuffReborn.MODID + ":blocks/yellow");
		UVMapping postThickSide = new UVMapping(
				4, 0,
				4, 16,
				0, 16,
				0, 0);
		UVMapping postThickEnd = new UVMapping(
				4, 0,
				4, 4,
				0, 4,
				0, 0);
		UVMapping postThinSide = new UVMapping(
				2, 0,
				2, 16,
				0, 16,
				0, 0);
		UVMapping postThinEnd = new UVMapping(
				2, 0,
				2, 2,
				0, 2,
				0, 0);
		UVMapping armSide = new UVMapping(
				16, 0,
				16, 2,
				0, 2,
				0, 0);
		UVMapping lampSide = new UVMapping(
				2, 0,
				2, 13,
				0, 13,
				0, 0);
		UVMapping lampEnd = new UVMapping(
				2, 0,
				2, 1,
				0, 1,
				0, 0);
		
		postThickBoxTextureCollection = new BoxTextureCollection(generic, generic, generic, generic, generic, generic, postThickSide, postThickSide, postThickSide, postThickSide, postThickEnd, postThickEnd);
		postThinBoxTextureCollection = new BoxTextureCollection(generic, generic, generic, generic, generic, generic, postThinSide, postThinSide, postThinSide, postThinSide, postThinEnd, postThinEnd);
		armBoxTextureCollection = new BoxTextureCollection(generic, generic, generic, generic, generic, generic, postThinEnd, armSide, postThinEnd, armSide, armSide, armSide);
		lampBoxTextureCollection = new BoxTextureCollection(lamp, lamp, lamp, lamp, lamp, lamp, lampEnd, lampSide, lampEnd, lampSide, lampSide, lampSide);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		ArrayList<BakedQuad> quads = new ArrayList<BakedQuad>();
		

		quads.addAll(createBox(6, 0, 6, 4, 16, 4, postThickBoxTextureCollection));
		quads.addAll(createBox(6, 16, 6, 4, 16, 4, postThickBoxTextureCollection));
		quads.addAll(createBox(7, 32, 7, 2, 16, 2, postThinBoxTextureCollection));
		quads.addAll(createBox(7, 48, 7, 2, 16, 2, postThinBoxTextureCollection));
		
		EnumFacing facing = (state != null) ? state.getValue(BlockStreetLightSingle.FACING) : EnumFacing.NORTH;
		
		switch(facing)
		{
			case NORTH:
			case SOUTH:
				// North Head
				quads.addAll(createBoxWithZRotation(7, 60, 9, 2, 2, 16, 20, armBoxTextureCollection)); // rotated arm
				quads.addAll(createBox(7, 65.35, 23.2, 2, 2, 16, armBoxTextureCollection)); // main arm
				quads.addAll(createBox(5, 64.35, 25.2, 1, 1, 14, armBoxTextureCollection)); // side
				quads.addAll(createBox(10, 64.35, 25.2, 1, 1, 14, armBoxTextureCollection)); // side
				quads.addAll(createBox(6, 64.35, 25.2, 4, 1, 1, armBoxTextureCollection)); // outside
				quads.addAll(createBox(6, 64.35, 38.2, 4, 1, 1, armBoxTextureCollection)); // inside
				quads.addAll(createBox(6, 65.34, 25.2, 4, 0, 14, armBoxTextureCollection)); // top cover
				quads.addAll(createBox(7, 64.83, 26.2, 2, 0.5, 12, lampBoxTextureCollection)); // lamp

				// South head
				quads.addAll(createBoxWithZRotation(7, 65.5, -8, 2, 2, 16, -20, armBoxTextureCollection)); // rotated arm
				quads.addAll(createBox(7, 65.35, -23.2, 2, 2, 16, armBoxTextureCollection)); // main arm
				quads.addAll(createBox(5, 64.35, -23.2, 1, 1, 14, armBoxTextureCollection)); // side
				quads.addAll(createBox(10, 64.35, -23.2, 1, 1, 14, armBoxTextureCollection)); // side
				quads.addAll(createBox(6, 64.35, -23.2, 4, 1, 1, armBoxTextureCollection)); // outside
				quads.addAll(createBox(6, 64.35, -10.2, 4, 1, 1, armBoxTextureCollection)); // inside
				quads.addAll(createBox(6, 65.34, -23.2, 4, 0, 14, armBoxTextureCollection)); // top cover
				quads.addAll(createBox(7, 64.83, -22.2, 2, 0.5, 12, lampBoxTextureCollection)); // lamp
				break;
			case WEST:
			case EAST:
				// West Head
				quads.addAll(createBoxWithXRotation(9, 60, 7, 16, 2, 2, 20, armBoxTextureCollection)); // rotated arm
				quads.addAll(createBox(23.2, 65.35, 7, 16, 2, 2, armBoxTextureCollection)); // main arm
				quads.addAll(createBox(25.2, 64.35, 5, 14, 1, 1, armBoxTextureCollection)); // side
				quads.addAll(createBox(25.2, 64.35, 10, 14, 1, 1, armBoxTextureCollection)); // side
				quads.addAll(createBox(25.2, 64.36, 6, 1, 1, 4, armBoxTextureCollection)); // inside
				quads.addAll(createBox(38.2, 64.36, 6, 1, 1, 4, armBoxTextureCollection)); // outside
				quads.addAll(createBox(25.2, 65.34, 6, 14, 0, 4, armBoxTextureCollection)); // cover
				quads.addAll(createBox(26.2, 64.83, 7, 12, 0.5, 2, lampBoxTextureCollection)); // lamp
				
				// East head
				quads.addAll(createBoxWithXRotation(-8, 65.5, 7, 16, 2, 2, -20, armBoxTextureCollection)); // rotated arm
				quads.addAll(createBox(-23.2, 65.35, 7, 16, 2, 2, armBoxTextureCollection)); // main arm
				quads.addAll(createBox(-23.2, 64.35, 5, 14, 1, 1, armBoxTextureCollection)); // side
				quads.addAll(createBox(-23.2, 64.35, 10, 14, 1, 1, armBoxTextureCollection)); // side
				quads.addAll(createBox(-10.2, 64.36, 6, 1, 1, 4, armBoxTextureCollection)); // inside
				quads.addAll(createBox(-23.2, 64.36, 6, 1, 1, 4, armBoxTextureCollection)); // outside
				quads.addAll(createBox(-23.2, 65.34, 6, 14, 0, 4, armBoxTextureCollection)); // cover
				quads.addAll(createBox(-22.2, 64.83, 7, 12, 0.5, 2, lampBoxTextureCollection)); // lamp
				break;
		}
		
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return generic;
	}
}
