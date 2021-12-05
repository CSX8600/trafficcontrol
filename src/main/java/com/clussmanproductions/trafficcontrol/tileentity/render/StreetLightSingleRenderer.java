package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightSingle;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightSingleTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.RenderBoxHelper.Box;
import com.clussmanproductions.trafficcontrol.tileentity.render.RenderBoxHelper.TextureInfo;
import com.clussmanproductions.trafficcontrol.tileentity.render.RenderBoxHelper.TextureInfoCollection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class StreetLightSingleRenderer extends TileEntitySpecialRenderer<StreetLightSingleTileEntity> {
	private static final ResourceLocation generic = new ResourceLocation(ModTrafficControl.MODID, "textures/blocks/generic.png");
	private static final ResourceLocation yellow = new ResourceLocation(ModTrafficControl.MODID, "textures/blocks/yellow.png");
	
	private static final TextureInfo postThickSideInfo = new TextureInfo(generic, 0, 0, 4, 16);
	private static final TextureInfo postThickEndInfo = new TextureInfo(generic, 0, 0, 4, 4);
	private static final TextureInfo postThinSideInfo = new TextureInfo(generic, 0, 0, 2, 16);
	private static final TextureInfo postThinEndInfo = new TextureInfo(generic, 0, 0, 2, 2);
	private static final TextureInfo armSideInfo = new TextureInfo(generic, 0, 0, 16, 2);
	private static final TextureInfo lampSideInfo = new TextureInfo(yellow, 0, 0, 2, 13);
	private static final TextureInfo lampEndInfo = new TextureInfo(yellow, 0, 0, 2, 1);
	
	private static final TextureInfoCollection postThickBoxCollection = new TextureInfoCollection(postThickSideInfo, postThickEndInfo, postThickSideInfo, postThickEndInfo, postThickSideInfo, postThickSideInfo);
	private static final TextureInfoCollection postThinBoxCollection = new TextureInfoCollection(postThinSideInfo, postThinEndInfo, postThinSideInfo, postThinEndInfo, postThinSideInfo, postThinSideInfo);
	private static final TextureInfoCollection armBoxCollection = new TextureInfoCollection(postThinEndInfo, armSideInfo, postThinEndInfo, armSideInfo, armSideInfo, armSideInfo);
	private static final TextureInfoCollection lampBoxCollection = new TextureInfoCollection(lampEndInfo, lampSideInfo, lampEndInfo, lampSideInfo, lampSideInfo, lampSideInfo);
	
	public void render(StreetLightSingleTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		ArrayList<Box> boxes = new ArrayList<>();
		
		// Post
		boxes.add(new Box(6, 0, 6, 4, 16, 4, postThickBoxCollection, true));
		boxes.add(new Box(6, 16, 6, 4, 16, 4, postThickBoxCollection, true));
		boxes.add(new Box(7, 32, 7, 2, 16, 2, postThinBoxCollection, true));
		boxes.add(new Box(7, 48, 7, 2, 16, 2, postThinBoxCollection, true));
		
		// Skipping rotated arm for now because we'll be rotating GL State for it
		
		// Arm and lamp
		boxes.add(new Box(7, 65.35, 23.2, 2, 2, 16, armBoxCollection, true));
		boxes.add(new Box(5, 64.35, 25.2, 1, 1, 14, armBoxCollection, true));
		boxes.add(new Box(10, 64.35, 25.2, 1, 1, 14, armBoxCollection, true));
		boxes.add(new Box(6, 64.35, 25.2, 4, 1, 1, armBoxCollection, true));
		boxes.add(new Box(6, 64.35, 38.2, 4, 1, 1, armBoxCollection, true));
		boxes.add(new Box(6, 65.34, 25.2, 4, 0, 14, armBoxCollection, true));
		boxes.add(new Box(7, 64.83, 26.2, 2, 0.5, 12, lampBoxCollection, true));
		
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if (!(state.getBlock() instanceof BlockStreetLightSingle))
		{
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.rotate(state.getValue(BlockStreetLightSingle.ROTATION) * -22.5F, 0, 1, 0);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		final TextureManager texManager = Minecraft.getMinecraft().renderEngine;
		
		for(Box box : boxes)
		{
			box.render(builder, rl ->
			{
				boolean firstBind = builder.getVertexCount() == 0;
				
				if (!firstBind)
				{
					tess.draw();
				}
				
				texManager.bindTexture(rl);
				
				if (!firstBind)
				{
					builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				}
			});
		}
		
		if (builder.getVertexCount() > 0)
		{
			tess.draw();
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		}
		
		// Draw rotated arm bit
		GlStateManager.translate(0.4375, 3.75, 0.5625);
		GlStateManager.rotate(-20, 1, 0, 0);
		
		Box rotatedArmBox = new Box(0, 0, 0, 2, 2, 16, armBoxCollection, true);
		rotatedArmBox.render(builder, rl -> texManager.bindTexture(rl));
		tess.draw();
		
		GlStateManager.popMatrix();
	}
}
