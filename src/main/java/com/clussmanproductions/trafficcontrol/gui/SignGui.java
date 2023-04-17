package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.network.PacketUpdateSign;
import com.clussmanproductions.trafficcontrol.signs.Sign;
import com.clussmanproductions.trafficcontrol.signs.SignHorizontalAlignment;
import com.clussmanproductions.trafficcontrol.signs.SignVerticalAlignment;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class SignGui extends GuiScreen {

	SignTileEntity sign;
	GuiImageList list;
	GuiTextField search;
	GuiButtonExt textFieldButton;
	boolean textEditMode;
	int currentTextLine;
	
	public SignGui(SignTileEntity sign)
	{
		this.sign = sign;
	}
	
	@Override
	public void initGui() {
		list = new GuiImageList(width - 128,
				18,
				112,
				height - 68,
				img -> onImageClicked(img));
		
		search = new GuiTextField(0, fontRenderer, width - 128, height - 40, 112, 20);
		search.setText("\u00a73\u00a7oSearch...");
		
		int leftPanelWidth = width - 134;
		int leftPanelHorizontalCenter = leftPanelWidth / 2;
		
		textFieldButton = new GuiButtonExt(1, leftPanelHorizontalCenter - (203 / 2) + 3, 20, "Text Editor (T)");
		textFieldButton.enabled = false;
		if (sign.getSign() != null)
		{
			textFieldButton.enabled = sign.getSign().getTextLines().size() > 0;
		}
		
		if (textFieldButton.x + textFieldButton.width > leftPanelWidth)
		{
			textFieldButton.width = leftPanelWidth - textFieldButton.x;
		}
		
		buttonList.add(textFieldButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		search.drawTextBox();
		
		int leftPanelWidth = width - 134;
		int leftPanelHorizontalCenter = leftPanelWidth / 2;
		
		Sign currentSign = sign.getSign();
		if (currentSign != null)
		{
			int textWidth = fontRenderer.getStringWidth("Name: " + currentSign.getName());
			fontRenderer.drawString("Name: " + currentSign.getName(), leftPanelHorizontalCenter - (textWidth / 2), 43, 0xFFFF00);
			
			if (currentSign.getNote() != null && !currentSign.getNote().equals(""))
			{
				String note = "Note: " + currentSign.getNote();
				textWidth = fontRenderer.getStringWidth(note);
				if (textWidth > leftPanelWidth)
				{
					textWidth = leftPanelWidth;
				}
	
				int x = leftPanelHorizontalCenter - (textWidth / 2);
				int y = height - 35;
				fontRenderer.drawSplitString(note, x, y, textWidth, 0xFFFFFF);
				
				if (fontRenderer.listFormattedStringToWidth(note, textWidth).size() > 3 && mouseX > x && mouseX < x + textWidth && mouseY > y && mouseY < height)
				{
					drawHoveringText(note, mouseX, mouseY);
					GlStateManager.color(255, 255, 255);
					GlStateManager.disableLighting();
				}
			}
			
			mc.renderEngine.bindTexture(currentSign.getFrontImageResourceLocation());
			
			int previewWidthHeight = leftPanelWidth;
			int maxPreviewHeight = height - 105;
			if (previewWidthHeight > maxPreviewHeight)
			{
				previewWidthHeight = maxPreviewHeight;
			}
			
			int previewX = leftPanelHorizontalCenter - (previewWidthHeight / 2);
			
			GlStateManager.color(255, 255, 255);
			
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder builder = tess.getBuffer();
			
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			
			builder.pos(previewX, 53, 1).tex(0, 0).endVertex();
			builder.pos(previewX, 53 + previewWidthHeight, 1).tex(0, 1).endVertex();
			builder.pos(previewX + previewWidthHeight, 53 + previewWidthHeight, 1).tex(1, 1).endVertex();
			builder.pos(previewX + previewWidthHeight, 53, 1).tex(1, 0).endVertex();
			
			tess.draw();
			
			if (sign.getSign().getTextLines().size() > 0)
			{
				double fullScale = ((double)previewWidthHeight / (fontRenderer.FONT_HEIGHT)) / 16;
				double downScale = 1 / fullScale;
				
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.translate(previewX, 53, 2); // Upper Left Corner
				GlStateManager.scale(fullScale, fullScale, 1);
				for(int i = 0; i < currentSign.getTextLines().size(); i++)
				{
					Sign.TextLine textLine = currentSign.getTextLines().get(i);
					
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
					
					if (textEditMode)
					{
						int labelColor = 0;
						if (currentTextLine == i)
						{
							GlStateManager.color(0F, 1F, 0F, 2 / (float)3);
							labelColor = 0x00FF00;
						}
						else
						{
							GlStateManager.color(1F, 0F, 0F, 2 / (float)3);
							labelColor = 0xFF0000;
						}
						
						GlStateManager.disableTexture2D();
						builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
						builder.pos(0, 0, 0).endVertex();
						builder.pos(0, fontRenderer.FONT_HEIGHT, 0).endVertex();
						builder.pos(textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT, fontRenderer.FONT_HEIGHT, 0).endVertex();
						builder.pos(textLine.getScaleAdjustedWidth() * fontRenderer.FONT_HEIGHT, 0, 0).endVertex();
						tess.draw();
						GlStateManager.enableTexture2D();
						
						GlStateManager.scale(0.5, 0.5, 1);
						fontRenderer.drawString(textLine.getLabel(), 0, -fontRenderer.FONT_HEIGHT, labelColor);
						GlStateManager.scale(2, 2, 1);
					}
					
					textWidth = fontRenderer.getStringWidth(sign.getTextLine(i));
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
						fontRenderer.drawString(sign.getTextLine(i), textX + 1, 1, textLine.getColor());
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
				GlStateManager.scale(downScale, downScale, 1);
				GlStateManager.translate(-previewX, -53, -2);
			}
		}
		
		list.draw(mouseX, mouseY, fontRenderer, text -> x -> y -> 
		{
			drawHoveringText(text, x, y);
			GlStateManager.color(255, 255, 255);
			GlStateManager.disableLighting();
		});
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		list.onMouseClick(mouseX, mouseY);
		if (search.mouseClicked(mouseX, mouseY, mouseButton) && search.getText().equals("\u00a73\u00a7oSearch..."))
		{
			search.setText("");
		}
		else if (search.getText().equals(""))
		{
			search.setText("\u00a73\u00a7oSearch...");
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		if (textEditMode)
		{
			String currentText = sign.getTextLine(currentTextLine);
			if (currentText == null)
			{
				currentText = "";
			}
			
			if (keyCode == Keyboard.KEY_BACK && !currentText.isEmpty())
			{
				currentText = currentText.substring(0, currentText.length() - 1);
				sign.setTextLine(currentTextLine, currentText);
			}
			else if (keyCode == Keyboard.KEY_DOWN)
			{
				int maxTextLines = sign.getSign().getTextLines().size();
				if (currentTextLine + 1 < maxTextLines)
				{
					currentTextLine++;
				}
				else
				{
					currentTextLine = 0;
				}
			}
			else if (keyCode == Keyboard.KEY_UP)
			{
				int maxTextLines = sign.getSign().getTextLines().size();
				if (currentTextLine <= 0)
				{
					currentTextLine = maxTextLines - 1;
				}
				else
				{
					currentTextLine--;
				}
			}
			else if ((keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER))
			{
				if (currentTextLine == sign.getSign().getTextLines().size() - 1) // Last text line
				{
					actionPerformed(textFieldButton);
				}
				else
				{
					currentTextLine++;
				}
			}
			else if (keyCode == Keyboard.KEY_ESCAPE)
			{
				actionPerformed(textFieldButton);
			}
			else if (keyCode != Keyboard.KEY_BACK)
			{
				Sign.TextLine textLine = sign.getSign().getTextLines().get(currentTextLine);
				if (textLine.getMaxLength() == currentText.length() || typedChar == 0 || (Character.isWhitespace(typedChar) && keyCode != Keyboard.KEY_SPACE))
				{
					return;
				}
				
				currentText += typedChar;
				sign.setTextLine(currentTextLine, currentText);
			}
		}
		else if (keyCode == Keyboard.KEY_T && !textEditMode && !search.isFocused())
		{
			textFieldButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
			actionPerformed(textFieldButton);
		}
		else if (search.textboxKeyTyped(typedChar, keyCode))
		{
			list.filter(search.getText());
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		
		list.onMouseRelease();
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		list.scroll(Integer.signum(Mouse.getDWheel()));
	}
	
	private void onImageClicked(Sign image)
	{
		sign.setTypeLegacy(SignTileEntity.getSignTypeNumber(image.getType()));
		sign.setVariantLegacy(image.getVariant());
		sign.setID(image.getID());
		sign.clearTextLines();
		
		textFieldButton.enabled = image.getTextLines().size() > 0;
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		BlockPos pos = sign.getPos();
		PacketUpdateSign updateSign = new PacketUpdateSign();
		updateSign.x = pos.getX();
		updateSign.y = pos.getY();
		updateSign.z = pos.getZ();
		updateSign.type = sign.getTypeLegacy();
		updateSign.variant = sign.getVariantLegacy();
		updateSign.id = sign.getID();
		updateSign.textLines = new ArrayList<>();
		for(int i = 0; i < sign.getSign().getTextLines().size(); i++)
		{
			updateSign.textLines.add(sign.getTextLine(i));
		}
		
		PacketHandler.INSTANCE.sendToServer(updateSign);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == textFieldButton)
		{			
			Sign sign = this.sign.getSign();
			if (sign == null)
			{
				return;
			}
			
			if (sign.getTextLines().size() <= 0)
			{
				textEditMode = false;
				search.setEnabled(true);
				return;
			}
			
			textEditMode = !textEditMode;
			search.setEnabled(!textEditMode);
			textFieldButton.displayString = textEditMode ? "Finish Editing" : "Text Editor (T)";
			
			if (!textEditMode)
			{
				currentTextLine = 0;
			}
		}
	}
}
