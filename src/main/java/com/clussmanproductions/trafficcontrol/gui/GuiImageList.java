package com.clussmanproductions.trafficcontrol.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.signs.Sign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiImageList {
	private int x;
	private int y;
	private int width;
	private int height;
	private String filter;
	private Consumer<Sign> imageClickCallback;
	
	private boolean didLoadFail = false;
	private String loadFailedMessage = null;
	private HashMap<String, Sign> imagesByName;
	Sign[][] slots;
	
	// Scroll bar stuff
	private int scrollBarLeft;
	private double scrollBarHeight;
	private double scrollBarTop;
	private int currentScrollBarStep = 0;
	private int maxScrollBarSteps = 0;
	private boolean isMouseClicked = false;
	private boolean isMouseHover = false;
	private int clickY = 0;
	private int preClickScrollBarStep = 0;
	private boolean isVisible = true;
	
 	public GuiImageList(int x, int y, int width, int height, String filter, Consumer<Sign> imageClickCallback)
	{
		this.x = x + (width % 16);
		this.y = y + (height % 16);
		this.width = width - (width % 16);
		this.height = height - (height % 16);
		this.filter = filter;
		this.imageClickCallback = imageClickCallback;
		
		int slotWidth = (width / 16) - 1;
		
		if (slotWidth < 1)
		{
			didLoadFail = true;
			loadFailedMessage = "Too narrow";
			
			return;
		}
		
		imagesByName = new HashMap<>();
		for(Sign sign : ModTrafficControl.instance.signRepo.getAllSigns())
		{
			imagesByName.put(sign.getName() + " (" + sign.getVariant() + ")", sign);
		}
		
		fillSlots();
	}
	
	public GuiImageList(int x, int y, int width, int height, Consumer<Sign> imageClickCallback)
	{
		this(x, y, width, height, null, imageClickCallback);
	}
	
	public void draw(int mouseX, int mouseY, FontRenderer renderer, Function<List<String>, Function<Integer, Consumer<Integer>>> drawHoveringTextCallback)
	{
		if (!isVisible())
		{
			return;
		}
		
		GlStateManager.color(255, 255, 255);
		if (didLoadFail)
		{
			int length = renderer.getStringWidth(loadFailedMessage);
			int left = x + ((width / 2) - (length / 2));
			renderer.drawString(loadFailedMessage, left, y + (height / 2), 0xFFFFFF);
			
			return;
		}
		
		if (isMouseClicked)
		{			
			int differenceY = mouseY - clickY;
			
			currentScrollBarStep = preClickScrollBarStep + (int)(differenceY / scrollBarHeight);
			if (currentScrollBarStep > maxScrollBarSteps)
			{
				currentScrollBarStep = maxScrollBarSteps;
			}
			
			if (currentScrollBarStep < 0)
			{
				currentScrollBarStep = 0;
			}
			
			scrollBarTop = y + (scrollBarHeight * currentScrollBarStep);
		}
		
		GlStateManager.disableTexture2D();
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		
		builder.pos(x + width - 16, y + height, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x + width - 16, y, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x, y, 0).color(255, 255, 255, 255).endVertex();
		builder.pos(x, y + height, 0).color(255, 255, 255, 255).endVertex();
		
		int renderableRows = height / 16;
		int maxRows = (imagesByName.keySet().size() / slots.length) + 1;
		
		Sign hoveringImage = null;
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
		{
			isMouseHover = true;
			
			int hoverCol = (mouseX - x) / 16;
			int hoverRow = (mouseY - y) / 16;
			
			if (hoverRow < renderableRows && slots.length > hoverCol && slots[hoverCol].length - currentScrollBarStep > hoverRow && slots[hoverCol][hoverRow +  currentScrollBarStep] != null)
			{
				hoveringImage = slots[hoverCol][hoverRow + currentScrollBarStep];
				
				if (hoveringImage != null)
				{
					int hoverColX = x + (hoverCol * 16);
					int hoverRowY = y + (hoverRow * 16);
					 
					builder.pos(hoverColX + 16, hoverRowY + 16, 0).color(0, 0, 255, 255).endVertex();
					builder.pos(hoverColX + 16, hoverRowY, 0).color(0, 0, 255, 255).endVertex();
					builder.pos(hoverColX, hoverRowY, 0).color(0, 0, 255, 255).endVertex();
					builder.pos(hoverColX, hoverRowY + 16, 0).color(0, 0, 255, 255).endVertex();
				}
			}
		}
		else
		{
			isMouseHover = false;
		}
		
		drawScrollbar(mouseX, mouseY, renderableRows, maxRows);
		
		tess.draw();
		
		GlStateManager.enableTexture2D();
		
		for(int col = 0; col < slots.length; col++)
		{
			int rowLimit = (slots[col].length - currentScrollBarStep < renderableRows) ? slots[col].length - currentScrollBarStep : renderableRows;
			for(int row = 0; row < rowLimit; row++)
			{
				Sign image = slots[col][row + currentScrollBarStep];
				
				if (image == null)
				{
					continue;
				}
				
				Minecraft.getMinecraft().renderEngine.bindTexture(image.getFrontImageResourceLocation());
				
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
			hoverText.add("\u00a7e" + hoveringImage.getName() + " (" + ModTrafficControl.instance.signRepo.getFriendlyTypeName(hoveringImage.getType()) + ")");
			
			if (hoveringImage.getToolTip() != null && hoveringImage.getToolTip() != "")
			{
				hoverText.add(hoveringImage.getToolTip());
			}
			
			drawHoveringTextCallback.apply(hoverText).apply(mouseX).accept(mouseY);
		}
	}

	public void scroll(int direction)
	{
		if (!isMouseHover)
		{
			return;
		}
		
		if (direction == 1)
		{
			scrollUp();
		}
		else if (direction == -1)
		{
			scrollDown();
		}
	}
	
	private void scrollUp()
	{
		currentScrollBarStep -= 1;
		if (currentScrollBarStep < 0)
		{
			currentScrollBarStep = 0;
		}
		else
		{
			scrollBarTop -= scrollBarHeight;
		}
	}
	
	public void scrollDown()
	{
		currentScrollBarStep += 1;
		if (currentScrollBarStep > maxScrollBarSteps)
		{
			currentScrollBarStep = maxScrollBarSteps;
		}
		else
		{
			scrollBarTop += scrollBarHeight;
		}
	}
	
	public void drawScrollbar(int mouseX, int mouseY, int renderableRows, int maxRows)
	{
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		builder.pos(scrollBarLeft, y, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(scrollBarLeft, y + height, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(x + width, y + height, 1).color(32, 32, 32, 255).endVertex();
		builder.pos(x + width, y, 1).color(32, 32, 32, 255).endVertex();
		
		builder.pos(scrollBarLeft, scrollBarTop, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(scrollBarLeft, scrollBarTop + scrollBarHeight, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(x + width, scrollBarTop + scrollBarHeight, 1).color(128, 128, 128, 255).endVertex();
		builder.pos(x + width, scrollBarTop, 1).color(128, 128, 128, 255).endVertex();
	}
	
	public void onMouseClick(int mouseX, int mouseY)
	{
		if (!isVisible())
		{
			return;
		}
		
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
		{
			// Check to see if it's on the scroll bar
			if (mouseX > scrollBarLeft && mouseY > scrollBarTop && mouseY < scrollBarTop + scrollBarHeight)
			{
				isMouseClicked = true;
				clickY = mouseY;
				preClickScrollBarStep = currentScrollBarStep;
				
				return;
			}
			
			int hoverCol = (mouseX - x) / 16;
			int hoverRow = (mouseY - y) / 16;
			
			if (hoverCol < slots.length && hoverRow + currentScrollBarStep < slots[hoverCol].length)
			{
				Sign image = slots[hoverCol][hoverRow + currentScrollBarStep];
				if (image == null)
				{
					return;
				}
				
				if (imageClickCallback != null)
				{
					imageClickCallback.accept(image);
				}
			}
		}
	}
	
	public void onMouseRelease()
	{
		isMouseClicked = false;
	}
	
	public void filter(String filterText)
	{
		this.filter = filterText;
		
		fillSlots();
	}
	
	private void fillSlots()
	{
		int slotWidth = (width / 16) - 1;
		
		if (slotWidth < 1)
		{
			return;
		}
		
		Predicate<? super Entry<String, Sign>> filterPredicate = entry ->
		{
			if (filter == null || entry.getKey().isEmpty())
			{
				return true;
			}
			
			String typeFilter = "";
			String typeStringToExcludeFromFilter = "";
			if (filter.contains("@"))
			{
				int typeFilterStart = filter.indexOf("@");
				String filterText = filter.substring(typeFilterStart + 1);
				if (filterText.length() > 0)
				{
					if (filterText.charAt(0) == '\"' && filterText.length() > 1 && filterText.substring(1).contains("\""))
					{
						filterText = filterText.substring(1);
						typeFilter = filterText.substring(0, filterText.indexOf("\""));
						typeStringToExcludeFromFilter = "@\"" + typeFilter + "\"";
					}
					else
					{
						String[] remainingParts = filterText.split("\\s+");
						typeFilter = remainingParts[0];
						typeStringToExcludeFromFilter = "@" + typeFilter;
						
						if (remainingParts.length > 1)
						{
							typeStringToExcludeFromFilter += " ";
						}
					}
				}
			}
			
			String nameFilter = filter.replace(typeStringToExcludeFromFilter, "").trim();
			boolean showSign = entry.getKey().toLowerCase().contains(nameFilter.toLowerCase());
			if (!typeFilter.isEmpty())
			{
				showSign &= entry.getValue().getType().toLowerCase().contains(typeFilter) || 
								ModTrafficControl.instance.signRepo.getFriendlyTypeName(entry.getValue().getType()).toLowerCase().contains(typeFilter.toLowerCase());
			}
			
			return showSign;
		};
		
		Map<String, Sign> imagesToRender = imagesByName.entrySet().stream().filter(filterPredicate).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
		
		int rows = ((imagesToRender.values().size() + 1) / slotWidth) + 1;
		
		slots = new Sign[slotWidth][rows];
		
		int currentRow = 0;
		int currentCol = 0;
		
		for (Sign sign : ModTrafficControl.instance.signRepo.getAllSigns())
		{			
			if (!imagesToRender.containsValue(sign))
			{
				continue;
			}
			
			slots[currentCol][currentRow] = sign;
			
			currentCol++;
			if (currentCol >= slotWidth)
			{
				currentCol = 0;
				currentRow++;
			}
		}
		
		// Setup scroll bar values
		scrollBarLeft = this.x + this.width - 16;
		scrollBarTop = this.y;

		currentScrollBarStep = 0;
		maxScrollBarSteps = rows - (this.height / 16);
		if (maxScrollBarSteps < 0)
		{
			maxScrollBarSteps = 0;
		}

		if (maxScrollBarSteps == 0)
		{
			scrollBarHeight = this.height;
		}
		else
		{
			scrollBarHeight = (double)this.height / ((double)maxScrollBarSteps + 1);
			
			if (scrollBarHeight > this.height)
			{
				scrollBarHeight = this.height;
			}
		}
	}

	
	public boolean isVisible() {
		return isVisible;
	}

	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
