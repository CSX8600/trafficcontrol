package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.function.Consumer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;

public class RenderBoxHelper {
	public static class Box
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
		
		public void render(BufferBuilder builder, Consumer<ResourceLocation> bindTexture)
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
					bindTexture.accept(lastResourceLocation);
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
	
	public static class TextureInfoCollection
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
	
	public static class TextureInfo
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
