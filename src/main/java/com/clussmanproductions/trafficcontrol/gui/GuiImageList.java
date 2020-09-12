package com.clussmanproductions.trafficcontrol.gui;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiImageList {
	public int x;
	public int y;
	public int width;
	public int height;
	public String sort;
	public ResourceLocation imageListSource;
	
	private boolean didLoadFail = false;
	private String loadFailedMessage = null;
	private HashMap<String, Image> imagesByName;
	private HashMap<Integer, HashMap<Integer, Image>> imagesByVariantByType;
	Image currentImage;
	Image[][] slots;
	
 	public GuiImageList(int x, int y, int width, int height, String sort, ResourceLocation imageListSource, int currentTypeID, int currentVariant)
	{
		this.x = x + (width % 16);
		this.y = y + (height % 16);
		this.width = width - (width % 16);
		this.height = height - (height % 16);
		this.sort = sort;
		this.imageListSource = imageListSource;
		
		try
		{
			InputStream jsonStream = Minecraft.getMinecraft().getResourceManager().getResource(this.imageListSource).getInputStream();
			InputStreamReader streamReader= new InputStreamReader(jsonStream);
			
			JsonArray jsonArray = new JsonParser().parse(streamReader).getAsJsonArray();
			Iterator<JsonElement> arrayIterator = jsonArray.iterator();
			while(arrayIterator.hasNext())
			{
				JsonElement element = arrayIterator.next();
				if (!element.isJsonObject())
				{
					continue;
				}
				
				JsonObject obj = element.getAsJsonObject();
				String name = obj.get("name").getAsString();
				String type = obj.get("type").getAsString();
				int typeID = SignTileEntity.getSignTypeByName(type);
				int variant = obj.get("variant").getAsInt();
				String note = null;
				
				if (obj.has("note"))
				{
					note = obj.get("note").getAsString();
				}
				
				if (imagesByName == null)
				{
					imagesByName = new HashMap<>();
				}
				
				if (imagesByVariantByType == null)
				{
					imagesByVariantByType = new HashMap<>();
				}
				
				Image newImage = new Image(
						new ResourceLocation("trafficcontrol:textures/blocks/sign/" + type + "/" + type + variant + ".png"),
						name,
						typeID,
						variant,
						note);
				
				if (currentTypeID == typeID && variant == currentVariant)
				{
					currentImage = newImage;
				}
				
				if (imagesByName.containsKey(name))
				{
					int counter = 0;
					do
					{
						counter++;
					}
					while(imagesByName.containsKey(name + " (" + counter + ")"));
					
					name = name + " (" + counter + ")";
				}
				
				imagesByName.put(name, newImage);
				if (!imagesByVariantByType.containsKey(typeID))
				{
					imagesByVariantByType.put(typeID, new HashMap<>());
				}
				
				imagesByVariantByType.get(typeID).put(variant, newImage);
				
			}
		}
		catch(Exception ex)
		{
			didLoadFail = true;
			loadFailedMessage = "Could not load image list: " + ex.getMessage();
			
			return;
		}
		
		int slotWidth = (width / 16) - 2;
		
		if (slotWidth == 0)
		{
			didLoadFail = true;
			loadFailedMessage = "Too narrow";
			
			return;
		}
		
		int rows = ((imagesByName.values().size() + 1) / slotWidth) + 1;
		
		slots = new Image[slotWidth][rows];
		
		// The first slot is always the selected sign
		slots[0][0] = currentImage;
		
		int currentRow = 0;
		int currentCol = 1;
		
		if (slotWidth == 1)
		{
			currentRow = 1;
			currentCol = 0;
		}
		
		for (int typeId : imagesByVariantByType.keySet())
		{
			for (int variant : imagesByVariantByType.get(typeId).keySet())
			{
				Image image = imagesByVariantByType.get(typeId).get(variant);
				
				slots[currentCol][currentRow] = image;
				
				currentCol++;
				if (currentCol >= slotWidth)
				{
					currentCol = 0;
					currentRow++;
				}
			}
		}
	}
	
	public GuiImageList(int x, int y, int width, int height, ResourceLocation imageListSource, int currentTypeID, int currentVariant)
	{
		this(x, y, width, height, null, imageListSource, currentTypeID, currentVariant);
	}
	
	public GuiImageList(int x, int y, int width, int height)
	{
		this(x, y, width, height, null, null, 0, 0);
	}
	
	public void draw(int mouseX, int mouseY, FontRenderer renderer, Function<List<String>, Function<Integer, Consumer<Integer>>> drawHoveringTextCallback)
	{
		if (didLoadFail)
		{
			int length = renderer.getStringWidth(loadFailedMessage);
			int left = x + ((width / 2) - (length / 2));
			renderer.drawString(loadFailedMessage, left, y + (height / 2), 0xFFFFFF);
			
			return;
		}
		
		GlStateManager.disableTexture2D();
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		
		builder.pos(x + width - 32, y + height, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x + width - 32, y, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x, y, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x, y + height, 0).color(255, 255, 255, 255).endVertex();
		
		builder.pos(x + 16, y + 16, 0).color(0, 128, 0, 255).endVertex();
		builder.pos(x + 16, y, 0).color(0, 128, 0, 255).endVertex();
		builder.pos(x, y, 0).color(0, 128, 0, 255).endVertex();
		builder.pos(x, y + 16, 0).color(0, 128, 0, 255).endVertex();
		
		int renderableRows = height / 16;
		int maxRows = (imagesByName.keySet().size() / 16) + 1;
		
		Image hoveringImage = null;
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
		{
			int hoverCol = (mouseX - x) / 16;
			int hoverRow = (mouseY - y) / 16;
			
			if (hoverRow < renderableRows && slots.length > hoverCol && slots[hoverCol].length > hoverRow && slots[hoverCol][hoverRow] != null)
			{
				hoveringImage = slots[hoverCol][hoverRow];
				
				int hoverColX = x + (hoverCol * 16);
				int hoverRowY = y + (hoverRow * 16);
				 
				builder.pos(hoverColX + 16, hoverRowY + 16, 0).color(0, 0, 255, 255).endVertex();
				builder.pos(hoverColX + 16, hoverRowY, 0).color(0, 0, 255, 255).endVertex();
				builder.pos(hoverColX, hoverRowY, 0).color(0, 0, 255, 255).endVertex();
				builder.pos(hoverColX, hoverRowY + 16, 0).color(0, 0, 255, 255).endVertex();
			}
		}
		
		drawScrollbar(mouseX, mouseY, renderableRows, maxRows);
		
		tess.draw();
		
		GlStateManager.enableTexture2D();
		
		for(int col = 0; col < slots.length; col++)
		{
			int rowLimit = (slots[col].length < renderableRows) ? slots[col].length : renderableRows;
			for(int row = 0; row < rowLimit; row++)
			{
				Image image = slots[col][row];
				
				if (image == null)
				{
					continue;
				}
				
				Minecraft.getMinecraft().renderEngine.bindTexture(image.getImageResourceLocation());
				
				int imageX = x + (col * 16);
				int imageY = y + (row * 16);
				
				builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				
				builder.pos(imageX + 16, imageY + 16, 0).tex(1, 1).endVertex();
				builder.pos(imageX + 16, imageY, 0).tex(1, 0).endVertex();
				builder.pos(imageX, imageY, 0).tex(0, 0).endVertex();
				builder.pos(imageX, imageY + 16, 0).tex(0, 1).endVertex();
				
				tess.draw();
			}
		}
		
		if (hoveringImage != null)
		{
			ArrayList<String> hoverText = new ArrayList<>();
			hoverText.add("§e" + hoveringImage.name);
			
			if (hoveringImage.note != null && hoveringImage.note != "")
			{
				hoverText.add(hoveringImage.note);
			}
			
			drawHoveringTextCallback.apply(hoverText).apply(mouseX).accept(mouseY);
		}
	}

	public void drawScrollbar(int mouseX, int mouseY, int renderableRows, int maxRows)
	{
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		builder.pos(x + width - 32, y, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(x + width - 32, y + height, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(x + width, y + height, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(x + width, y, 1).color(32, 32, 32, 255).endVertex();
		
		double percentageOfShownRows = (double)renderableRows / (double)maxRows;
		int scrollBarHeight = (int)((double)height * percentageOfShownRows);
		
		builder.pos(x + width - 32, y, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(x + width - 32, y + scrollBarHeight, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(x + width, y + scrollBarHeight, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(x + width, y, 1).color(128, 128, 128, 255).endVertex();
	}
	
	private class Image
	{
		private ResourceLocation imageResourceLocation;
		private String name;
		private int type;
		private int variant;
		private String note;
		
		public Image(ResourceLocation imageRL, String name, int type, int variant, String note)
		{
			imageResourceLocation = imageRL;
			this.name = name;
			this.type = type;
			this.variant = variant;
			this.note = note;
		}
		
		public ResourceLocation getImageResourceLocation() { return imageResourceLocation; }
	}
}
