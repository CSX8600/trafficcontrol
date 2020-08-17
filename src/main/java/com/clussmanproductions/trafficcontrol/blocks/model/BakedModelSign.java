package com.clussmanproductions.trafficcontrol.blocks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockSign;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelSign extends BaseBakedModel {
	private TextureAtlasSprite generic;
	HashMap<String, TextureAtlasSprite> signTextures = new HashMap<>();
	
	public BakedModelSign(VertexFormat format)
	{
		super(format);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (side != null)
		{
			return Collections.emptyList();
		}
		
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
		
		UVMapping sevenBy2Generic = new UVMapping(
				7, 0,
				7, 2,
				0, 2,
				0, 0);
		
		UVMapping signBackGeneric = new UVMapping(
				0,0,
				0,16,
				16,16,
				16,0);
		

		int poleHeight = 16;
		// Sign
		if (state != null && state instanceof IExtendedBlockState)
		{
			try
			{
				IExtendedBlockState extendedState = (IExtendedBlockState)state;
				int type = extendedState.getValue(BlockSign.TYPE);
				int variant = extendedState.getValue(BlockSign.SELECTION);
				EnumFacing facing = extendedState.getValue(BlockSign.FACING);
				
				String typeName = SignTileEntity.getSignTypeName(type);
				String frontName = typeName + variant;
				String backName = SignTileEntity.getBackSignName(type, variant);
				
				if (type == 2 &&
						(variant >= 33 && variant <= 40))
				{
					poleHeight = 8;
				}
				
				switch(facing)
				{
					case SOUTH:
						retval.add(createQuad(v(16, 16, 6.9), v(16, 0, 6.9), v(0, 0, 6.9), v(0, 16, 6.9), getSign(typeName, frontName), signBackGeneric));
						retval.add(createQuad(v(0, 16, 6.9), v(0, 0, 6.9), v(16, 0, 6.9), v(16, 16, 6.9), getSign(typeName, backName), signBackGeneric));
						break;
					case NORTH:
						retval.add(createQuad(v(16, 16, 9.1), v(16, 0, 9.1), v(0, 0, 9.1), v(0, 16, 9.1), getSign(typeName, backName), signBackGeneric));
						retval.add(createQuad(v(0, 16, 9.1), v(0, 0, 9.1), v(16, 0, 9.1), v(16, 16, 9.1), getSign(typeName, frontName), signBackGeneric));
						break;
					case WEST:
						retval.add(createQuad(v(9.1, 16, 16), v(9.1, 0, 16), v(9.1, 0, 0), v(9.1, 16, 0), getSign(typeName, frontName), signBackGeneric));
						retval.add(createQuad(v(9.1, 16, 0), v(9.1, 0, 0), v(9.1, 0, 16), v(9.1, 16, 16), getSign(typeName, backName), signBackGeneric));
						break;
					case EAST:
						retval.add(createQuad(v(6.9, 16, 16), v(6.9, 0, 16), v(6.9, 0, 0), v(6.9, 16, 0), getSign(typeName, backName), signBackGeneric));
						retval.add(createQuad(v(6.9, 16, 0), v(6.9, 0, 0), v(6.9, 0, 16), v(6.9, 16, 16), getSign(typeName, frontName), signBackGeneric));
						break;
				}
				
				if (extendedState.getValue(BlockSign.VALIDHORIZONTALBAR))
				{
					switch(facing)
					{
						case NORTH:
						case SOUTH:
							retval.add(createQuad(v(16, 9, 7), v(16, 7, 7), v(9, 7, 7), v(9, 9, 7), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(16, 9, 9), v(16, 9, 7), v(9, 9, 7), v(9, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(9, 9, 9), v(9, 7, 9), v(16, 7, 9), v(16, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(16, 7, 7), v(16, 7, 9), v(9, 7, 9), v(9, 7, 7), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(16, 9, 9), v(16, 7, 9), v(16, 7, 7), v(16, 9, 7), getGeneric(), twoBy2Generic));
							retval.add(createQuad(v(9, 9, 7), v(9, 7, 7), v(9, 7, 9), v(9, 9, 9), getGeneric(), twoBy2Generic));
							
							retval.add(createQuad(v(7, 9, 7), v(7, 7, 7), v(0, 7, 7), v(0, 9, 7), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 9), v(7, 9, 7), v(0, 9, 7), v(0, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(0, 9, 9), v(0, 7, 9), v(7, 7, 9), v(7, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 7, 7), v(7, 7, 9), v(0, 7, 9), v(0, 7, 7), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 9), v(7, 7, 9), v(7, 7, 7), v(7, 9, 7), getGeneric(), twoBy2Generic));
							retval.add(createQuad(v(0, 9, 7), v(0, 7, 7), v(0, 7, 9), v(0, 9, 9), getGeneric(), twoBy2Generic));
							break;
						case WEST:
						case EAST:
							retval.add(createQuad(v(9, 9, 16), v(9, 7, 16), v(9, 7, 9), v(9, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 16), v(9, 9, 16), v(9, 9, 9), v(7, 9, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 9), v(7, 7, 9), v(7, 7, 16), v(7, 9, 16), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(9, 7, 16), v(7, 7, 16), v(7, 7, 9), v(9, 7, 9), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 16), v(7, 7, 16), v(9, 7, 16), v(9, 9, 16), getGeneric(), twoBy2Generic));
							retval.add(createQuad(v(9, 9, 9), v(9, 7, 9), v(7, 7, 9), v(7, 9, 9), getGeneric(), twoBy2Generic));
							
							retval.add(createQuad(v(9, 9, 7), v(9, 7, 7), v(9, 7, 0), v(9, 9, 0), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 7), v(9, 9, 7), v(9, 9, 0), v(7, 9, 0), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 0), v(7, 7, 0), v(7, 7, 7), v(7, 9, 7), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(9, 7, 7), v(7, 7, 7), v(7, 7, 0), v(9, 7, 0), getGeneric(), sevenBy2Generic));
							retval.add(createQuad(v(7, 9, 7), v(7, 7, 7), v(9, 7, 7), v(9, 9, 7), getGeneric(), twoBy2Generic));
							retval.add(createQuad(v(9, 9, 0), v(9, 7, 0), v(7, 7, 0), v(7, 9, 0), getGeneric(), twoBy2Generic));
							break;
					}
				}
			}
			catch (Exception e)
			{
				
			}
		}
		else
		{
			retval.add(createQuad(v(0, 16, 9.1), v(0, 0, 9.1), v(16, 0, 9.1), v(16, 16, 9.1), getSign("circle", "circle0"), signBackGeneric));
		}
				
		// Post
		retval.add(createQuad(v(9, poleHeight, 7), v(9, 0, 7), v(7, 0, 7), v(7, poleHeight, 7), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(9, poleHeight, 9), v(9, 0, 9), v(9, 0, 7), v(9, poleHeight, 7), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(7, poleHeight, 9), v(7, 0, 9), v(9, 0, 9), v(9, poleHeight, 9), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(7, poleHeight, 7), v(7, 0, 7), v(7, 0, 9), v(7, poleHeight, 9), getGeneric(), twoBy16Generic));
		retval.add(createQuad(v(9, poleHeight, 9), v(9, poleHeight, 7), v(7, poleHeight, 7), v(7, poleHeight, 9), getGeneric(), twoBy2Generic));
		retval.add(createQuad(v(9, 0, 7), v(9, 0, 9), v(7, 0, 9), v(7, 0, 7), getGeneric(), twoBy2Generic));
		
		return retval;		
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModTrafficControl.MODID + ":blocks/generic");
	}
	
	private TextureAtlasSprite getGeneric()
	{
		if (generic == null)
		{
			generic = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ModTrafficControl.MODID + ":blocks/generic");
		}
		
		return generic;
	}
	
	private TextureAtlasSprite getSign(String type, String name)
	{
		String fullName = type + "/" + name;
		
		if (!signTextures.containsKey(fullName))
		{
			String resourceName = ModTrafficControl.MODID + ":blocks/sign/" + fullName;
			
			signTextures.put(fullName, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resourceName));
		}
		
		return signTextures.get(fullName);
	}
}
