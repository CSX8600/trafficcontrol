package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.tileentity.StreetSign;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSignTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class StreetSignRenderer extends TileEntitySpecialRenderer<StreetSignTileEntity> {
	@Override
	public void render(StreetSignTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.scale(0.0625, 0.0625, 0.0625);
		
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			StreetSign sign = te.getStreetSign(i);
			if (sign != null)
			{
				renderStreetSign(sign, i);
				GlStateManager.color(1, 1, 1);
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	private void renderStreetSign(StreetSign sign, int signIndex)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("trafficcontrol:textures/blocks/street_sign.png"));
		GlStateManager.translate(8, 8, 8);
		GlStateManager.rotate(sign.getRotation() * -22.5F, 0, 1, 0);
		GlStateManager.translate(-8F, -8F, -8F);
		
		double xOffset = 1 * (sign.getColor().getCol() - 1);
		double yOffset = 0.25 * (sign.getColor().getRow() - 1);
		int yRenderOffset = 4 * signIndex;
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(16, yRenderOffset + 4, 8).tex(xOffset + 1, yOffset).endVertex();
		builder.pos(16, yRenderOffset, 8).tex(xOffset + 1, yOffset + 0.25).endVertex();
		builder.pos(0, yRenderOffset, 8).tex(xOffset, yOffset + 0.25).endVertex();
		builder.pos(0, yRenderOffset + 4, 8).tex(xOffset, yOffset).endVertex();
		
		tess.draw();
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(0, yRenderOffset + 4, 8).tex(xOffset + 1, yOffset).endVertex();
		builder.pos(0, yRenderOffset, 8).tex(xOffset + 1, yOffset + 0.25).endVertex();
		builder.pos(16, yRenderOffset, 8).tex(xOffset, yOffset + 0.25).endVertex();
		builder.pos(16, yRenderOffset + 4, 8).tex(xOffset, yOffset).endVertex();
		
		tess.draw();
		
		GlStateManager.scale(-1, -1, 1);
		GlStateManager.translate(-8, -2.8 - yRenderOffset, 7.99);
		int width = getFontRenderer().getStringWidth(sign.getText());
		double scaleFactor = (double)15 / (double)width;
		
		if (scaleFactor > 0.25)
		{
			scaleFactor = 0.25;
		}
		
		GlStateManager.scale(scaleFactor, 0.25, 1);
		getFontRenderer().drawString(sign.getText(), -(width / 2), 0, sign.getTextColor());
		GlStateManager.scale(1/scaleFactor, 4, 1);
		GlStateManager.translate(8, 2.8 + yRenderOffset, -7.99);
		GlStateManager.scale(-1, -1, 1);
		
		GlStateManager.rotate(180, 0, 1, 0);
		
		GlStateManager.scale(-1, -1, 1);
		GlStateManager.translate(8, -2.8 - yRenderOffset, -8.01);
		GlStateManager.scale(scaleFactor, 0.25, 1);		
		getFontRenderer().drawString(sign.getText(), -(width / 2), 0, sign.getTextColor());
		GlStateManager.scale(1/scaleFactor, 4, 1);
		GlStateManager.translate(-8, 2.8 + yRenderOffset, 8.01);
		GlStateManager.scale(-1, -1, 1);
		
		GlStateManager.rotate(-180, 0, 1, 0);
		
		GlStateManager.translate(8, 8, 8);
		GlStateManager.rotate(sign.getRotation() * 22.5F, 0, 1, 0);
		GlStateManager.translate(-8F, -8F, -8F);
	}
}
