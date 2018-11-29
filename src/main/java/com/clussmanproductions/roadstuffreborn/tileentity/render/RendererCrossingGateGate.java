package com.clussmanproductions.roadstuffreborn.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import scala.Tuple3;

public class RendererCrossingGateGate extends TileEntitySpecialRenderer<CrossingGateGateTileEntity> {
	@Override
	public void render(CrossingGateGateTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		
		Tuple3<Double, Double, Double> translationAmount = te.getTranslation(x, y, z);
		GlStateManager.translate(translationAmount._1(), translationAmount._2(), translationAmount._3());
		
		GlStateManager.rotate(te.getFacingRotation(), 0, 1, 0);
		
		GlStateManager.rotate(te.getGateRotation(), 0, 0, 1);
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder builder = tes.getBuffer();		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderWeightVerticies(builder);
		tes.draw();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderGateVerticies(builder);
		
		tes.draw();
		
		GlStateManager.popMatrix();
	}
	
	private void renderWeightVerticies(BufferBuilder builder)
	{
		ResourceLocation generic = new ResourceLocation(ModRoadStuffReborn.MODID + ":textures/blocks/generic.png");
		TextureInfoCollection collection = new TextureInfoCollection(
				new TextureInfo(generic, 0, 0, 10, 4),
				new TextureInfo(generic, 0, 0, 10, 1),
				new TextureInfo(generic, 0, 0, 6, 5),
				new TextureInfo(generic, 0, 0, 10, 1),
				new TextureInfo(generic, 0, 0, 1, 4),
				new TextureInfo(generic, 0, 0, 1, 4));
		Box weightBox = new Box(-4, -2, 4, 12, 4, -1, collection);
		weightBox.render(builder);
	}
	
	private void renderGateVerticies(BufferBuilder builder)
	{
		ResourceLocation gate = new ResourceLocation(ModRoadStuffReborn.MODID + ":textures/blocks/gate.png");
		TextureInfoCollection collection = new TextureInfoCollection(
				new TextureInfo(gate, 0, 0, 16, 0.7),
				new TextureInfo(gate, 0, 2, 16, 2.7),
				new TextureInfo(gate, 0, 0, 15, 0.7),
				new TextureInfo(gate, 0, 1, 16, 1.7),
				new TextureInfo(gate, 0, 2, 3, 4),
				new TextureInfo(gate, 0, 2, 3, 4));
		Box gateBox = new Box(-60, -1, 4, 56, 2, -1, collection);
		gateBox.render(builder);
	}
	
	private class Box
	{
		private double x;
		private double y;
		private double z;
		private double width;
		private double height;
		private double depth;
		private TextureInfoCollection textureInfoCollection;
		
		public Box(double x, double y, double z, double width, double height, double depth, TextureInfoCollection textureInfoCollection)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.depth = depth;
			this.textureInfoCollection = textureInfoCollection;
		}
		
		public void render(BufferBuilder builder)
		{
			double[][] vertexPoints = getVertexPoints();
			
			int index = 0;
			int count = 0;
			ResourceLocation lastResourceLocation = null;
			for(double[] vertexPoint : vertexPoints)
			{
				TextureInfo info = getTextureInfo(index);
				if (info.texture != lastResourceLocation)
				{
					lastResourceLocation = info.texture;
					bindTexture(lastResourceLocation);
				}
				
				double uvX = 0;
				double uvY = 0;
				
				switch(count)
				{
					case 0:
						uvX = info.getConvertedEndX();
						uvY = info.getConvertedEndY();
						break;
					case 1:
						uvX = info.getConvertedEndX();
						uvY = info.getConvertedStartY();
						break;
					case 2:
						uvX = info.getConvertedStartX();
						uvY = info.getConvertedStartY();
						break;
					case 3:
						uvX = info.getConvertedStartX();
						uvY = info.getConvertedEndY();
						break;
				}
				
				builder.pos(vertexPoint[0], vertexPoint[1], vertexPoint[2]).tex(uvX, uvY).endVertex();	
				count++;
				
				if (count >= 4)
				{
					index++;
					count = 0;
				}
			}
		}
		
		private TextureInfo getTextureInfo(int count)
		{
			switch(count)
			{
				case 0:
					return textureInfoCollection.northFace;
				case 1:
					return textureInfoCollection.upFace;
				case 2:
					return textureInfoCollection.southFace;
				case 3:
					return textureInfoCollection.downFace;
				case 4:
					return textureInfoCollection.eastFace;
				case 5:
					return textureInfoCollection.westFace;
				default:
					return null;
			}
		}
		
		private double[][] getVertexPoints()
		{
			double convertedX = x / 16;
			double convertedY = y / 16;
			double convertedZ = z / 16;
			double convertedWidth = width / 16;
			double convertedHeight = height / 16;
			double convertedDepth = depth / 16;
			return new double[][] {
				{convertedX + convertedWidth, convertedY, convertedZ},
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ},
				{convertedX, convertedY + convertedHeight, convertedZ},
				{convertedX, convertedY, convertedZ}, // Front
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ},
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX, convertedY + convertedHeight, convertedZ}, // Up
				{convertedX, convertedY, convertedZ + convertedDepth},
				{convertedX, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX + convertedWidth, convertedY, convertedZ + convertedDepth}, // Back
				{convertedX + convertedWidth, convertedY, convertedZ + convertedDepth},
				{convertedX + convertedWidth, convertedY, convertedZ},
				{convertedX, convertedY, convertedZ},
				{convertedX, convertedY, convertedZ + convertedDepth}, // Down
				{convertedX + convertedWidth, convertedY, convertedZ + convertedDepth},
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX + convertedWidth, convertedY + convertedHeight, convertedZ},
				{convertedX + convertedWidth, convertedY, convertedZ}, // Right
				{convertedX, convertedY, convertedZ},
				{convertedX, convertedY + convertedHeight, convertedZ},
				{convertedX, convertedY + convertedHeight, convertedZ + convertedDepth},
				{convertedX, convertedY, convertedZ + convertedDepth} // Left
			};
		}
	}
	
	private class TextureInfoCollection
	{
		TextureInfo southFace;
		TextureInfo upFace;
		TextureInfo northFace;
		TextureInfo downFace;
		TextureInfo eastFace;
		TextureInfo westFace;
		
		public TextureInfoCollection(TextureInfo southFace, TextureInfo upFace, TextureInfo northFace, TextureInfo downFace, TextureInfo eastFace, TextureInfo westFace)
		{
			this.southFace = southFace;
			this.upFace = upFace;
			this.northFace = northFace;
			this.downFace = downFace;
			this.eastFace = eastFace;
			this.westFace = westFace;
		}
		
		public TextureInfo getSouthFace() { return southFace; }
		public TextureInfo getUpFace() { return upFace; }
		public TextureInfo getNorthFace() { return northFace; }
		public TextureInfo getDownFace() { return downFace; }
		public TextureInfo getEastFace() { return eastFace; }
		public TextureInfo getWestFace() { return westFace; }
	}
	
	private class TextureInfo
	{
		ResourceLocation texture;
		double startX;
		double startY;
		double endX;
		double endY;
		
		public TextureInfo(ResourceLocation texture, double startX, double startY, double endX, double endY)
		{
			this.texture = texture;
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
		}
		
		public double getConvertedStartX() { return startX / 16; }
		public double getConvertedStartY() { return startY / 16; }
		public double getConvertedEndX() { return endX / 16; }
		public double getConvertedEndY() { return endY / 16; }
	}
}
