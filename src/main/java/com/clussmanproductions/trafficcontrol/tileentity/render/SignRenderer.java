package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.blocks.BlockSign;
import com.clussmanproductions.trafficcontrol.signs.Sign;
import com.clussmanproductions.trafficcontrol.signs.SignHorizontalAlignment;
import com.clussmanproductions.trafficcontrol.signs.SignVerticalAlignment;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SignRenderer extends TileEntitySpecialRenderer<SignTileEntity> {
	@Override
	public void render(SignTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		Sign sign = te.getSign();
		if (sign == null)
		{
			return;
		}
		
		TextureManager texManager = Minecraft.getMinecraft().getRenderManager().renderEngine;
				
		IBlockState block = te.getWorld().getBlockState(te.getPos());
		if (!(block.getBlock() instanceof BlockSign))
		{
			return;
		}
		float rotation = block.getValue(BlockSign.ROTATION) * -22.5F;
		
		GlStateManager.pushMatrix();
		texManager.bindTexture(sign.getFrontImageResourceLocation());
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
		
		// === Draw text ===
		if (sign.getTextLines().size() > 0)
		{
			// Scale to sign
			FontRenderer fontRenderer = getFontRenderer();
			GlStateManager.scale(1F / fontRenderer.FONT_HEIGHT, -1F / fontRenderer.FONT_HEIGHT, 1);
			GlStateManager.translate(0, -9, 0.01);
			GlStateManager.scale(1 / 16F, 1 / 16F, 1);
			
			for(int i = 0; i < sign.getTextLines().size(); i++)
			{
				Sign.TextLine textLine = sign.getTextLines().get(i);
				
				GlStateManager.translate(textLine.getX() * fontRenderer.FONT_HEIGHT, textLine.getY() * fontRenderer.FONT_HEIGHT, 0);
				GlStateManager.scale(textLine.getXScale(), textLine.getYScale(), 1);
				if (textLine.getvAlign() == SignVerticalAlignment.Center)
				{
					GlStateManager.translate(0, -fontRenderer.FONT_HEIGHT / 2.0, 0);
				}
				else if (textLine.getvAlign() == SignVerticalAlignment.Bottom)
				{
					GlStateManager.translate(0,  -fontRenderer.FONT_HEIGHT, 0);
				}
				
				if (textLine.gethAlign() == SignHorizontalAlignment.Center)
				{
					GlStateManager.translate(-(textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT) / 2.0, 0, 0);
				}
				else if (textLine.gethAlign() == SignHorizontalAlignment.Right)
				{
					GlStateManager.translate(-textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT, 0, 0);
				}
				
				int textWidth = fontRenderer.getStringWidth(te.getTextLine(i));
				if (textWidth > 0)
				{
					double widthScaling = ((textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT) / (textWidth));
					if (widthScaling > 1)
					{
						widthScaling = 1;
					}
					
					GlStateManager.scale(widthScaling, 1, 1);
					int textX = 0;
					if (textLine.gethAlign() == SignHorizontalAlignment.Center && widthScaling == 1)
					{
						textX = (int)((textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT) / 2) - (textWidth / 2);
					}
					else if (textLine.gethAlign() == SignHorizontalAlignment.Right)
					{
						textX = (int)(textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT) - textWidth;
					}
					fontRenderer.drawString(te.getTextLine(i), textX + 1, 1, textLine.getColor());
					GlStateManager.scale(1 / widthScaling, 1, 1);
				}
				
				if (textLine.gethAlign() == SignHorizontalAlignment.Center)
				{
					GlStateManager.translate((textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT) / 2.0, 0, 0);
				}
				else if (textLine.gethAlign() == SignHorizontalAlignment.Right)
				{
					GlStateManager.translate(textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT, 0, 0);
				}
				
				if (textLine.getvAlign() == SignVerticalAlignment.Center)
				{
					GlStateManager.translate(0, fontRenderer.FONT_HEIGHT / 2.0, 0);
				}
				else if (textLine.getvAlign() == SignVerticalAlignment.Bottom)
				{
					GlStateManager.translate(0,  fontRenderer.FONT_HEIGHT, 0);
				}
				GlStateManager.scale(1 / textLine.getXScale(), 1 / textLine.getYScale(), 1);
				GlStateManager.translate(-textLine.getX() * fontRenderer.FONT_HEIGHT, -textLine.getY() * fontRenderer.FONT_HEIGHT, 0);
			}
			
			// Reverse scale to sign
			GlStateManager.scale(16F, 16F, 1);
			GlStateManager.translate(0, 9, -0.0001);
			GlStateManager.scale(fontRenderer.FONT_HEIGHT, -fontRenderer.FONT_HEIGHT, 1);
		}
		
		// Draw back
		GlStateManager.translate(0, 0, -0.01);
		GlStateManager.color(1, 1, 1);
		texManager.bindTexture(sign.getBackImageResourceLocation());
		
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
