package com.clussmanproductions.trafficcontrol.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiButtonExtSelectableImage extends GuiButtonExtSelectable {

	private ResourceLocation image;
	private double u;
	private double v;
	private double texWidth;
	private double texHeight;
	
	public GuiButtonExtSelectableImage(int id, int xPos, int yPos, int width, int height, ResourceLocation image, double u, double v, double texWidth, double texHeight) {
		super(id, xPos, yPos, width, height, "");
		
		this.image = image;
		this.u = u;
		this.v = v;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		super.drawButton(mc, mouseX, mouseY, partial);
		
		mc.renderEngine.bindTexture(image);
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(x + width - 5, y + height - 5, zLevel).tex(u + texWidth, v).endVertex();
		builder.pos(x + width - 5, y + 5, zLevel).tex(u + texWidth, v + texHeight).endVertex();
		builder.pos(x + 5, y + 5, zLevel).tex(u, v + texHeight).endVertex();
		builder.pos(x + 5, y + height - 5, zLevel).tex(u, v).endVertex();
		
		tess.draw();
	}
}
