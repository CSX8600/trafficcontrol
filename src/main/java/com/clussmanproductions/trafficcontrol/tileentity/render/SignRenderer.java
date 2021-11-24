package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockSign;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class SignRenderer extends TileEntitySpecialRenderer<SignTileEntity> {
	@Override
	public void render(SignTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		TextureManager texManager = Minecraft.getMinecraft().getRenderManager().renderEngine;
		
		String rlFormat = "%s:textures/blocks/sign/%s/%s.png";
		String typeName = SignTileEntity.getSignTypeName(te.getType());
		String resourceLocation = String.format(rlFormat, ModTrafficControl.MODID, typeName, typeName + Integer.toString(te.getVariant()));
		
		IBlockState block = te.getWorld().getBlockState(te.getPos());
		float rotation = block.getValue(BlockSign.ROTATION) * -22.5F;
		
		GlStateManager.pushMatrix();
		texManager.bindTexture(new ResourceLocation(resourceLocation));
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.rotate(rotation, 0, 1, 0);
		GlStateManager.translate(-0.5, -0.5, 0.06875);
		
		// Draw front
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(0, 1, 0).tex(0, 0).endVertex();
		builder.pos(0, 0, 0).tex(0, 1).endVertex();
		builder.pos(1, 0, 0).tex(1, 1).endVertex();
		builder.pos(1, 1, 0).tex(1, 0).endVertex();
		
		tess.draw();
		
		// Draw back
		GlStateManager.translate(0, 0, -0.01);
		resourceLocation = String.format(rlFormat, ModTrafficControl.MODID, typeName, SignTileEntity.getBackSignName(te.getType(), te.getVariant()));
		texManager.bindTexture(new ResourceLocation(resourceLocation));
		
		builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(1, 1, 0).tex(0, 0).endVertex();
		builder.pos(1, 0, 0).tex(0, 1).endVertex();
		builder.pos(0, 0, 0).tex(1, 1).endVertex();
		builder.pos(0, 1, 0).tex(1, 0).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
}
