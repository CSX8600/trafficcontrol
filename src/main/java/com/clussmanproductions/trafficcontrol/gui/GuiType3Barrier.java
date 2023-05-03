package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.signs.Sign;
import com.clussmanproductions.trafficcontrol.signs.SignHorizontalAlignment;
import com.clussmanproductions.trafficcontrol.signs.SignVerticalAlignment;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity.SignType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiType3Barrier extends GuiScreen {
	private Type3BarrierTileEntity tileEntity;
	private GuiCheckBox renderSign;
	private GuiButton prevSignType;
	private GuiButton nextSignType;
	private GuiCheckBox renderThisSign;
	private GuiButton selectThisSign;
	private GuiImageList imageList;
	private GuiTextField imageListFilter;
	private GuiButtonExt close;
	private GuiButton textLineEditor;
	private boolean textLineEditorActive;
	private int currentTextLine;
	
	public GuiType3Barrier(Type3BarrierTileEntity barrierTE)
	{
		tileEntity = barrierTE;
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		renderSign = new GuiCheckBox(CMPT_IDs.RENDER_SIGN, 0, 0, "Sign across barriers", tileEntity.getRenderSign());
		renderSign.setIsChecked(tileEntity.getRenderSign());
		renderSign.x = (width / 2) - renderSign.width - 4;
		renderSign.y = (height / 2) - 16 /* height of sign */ - 32;
		
		prevSignType = new GuiButton(CMPT_IDs.PREV_SIGN_TYPE, 0, 0, "<");
		prevSignType.width = 20;
		prevSignType.x = (width / 2) + 4;
		prevSignType.y = (height / 2) - 16 /* 1/2 height of sign */ - 32 - ((prevSignType.height - renderSign.height) / 2);
		prevSignType.visible = tileEntity.getRenderSign();
		
		nextSignType = new GuiButton(CMPT_IDs.NEXT_SIGN_TYPE, 0, 0, ">");
		nextSignType.width = 20;
		nextSignType.x = (width / 2) + 64 /* width of sign */ + prevSignType.width + 4 /* margin from center */ + 4 /* sign margin from prev */
				+ 4 /* margin from sign */;
		nextSignType.y = (height / 2) - 16 /* 1/2 height of sign */ - 32 - ((nextSignType.height - renderSign.height) / 2);
		nextSignType.visible = tileEntity.getRenderSign();
		
		renderThisSign = new GuiCheckBox(CMPT_IDs.RENDER_THIS_SIGN, 0, 0, "Sign on this barrier", tileEntity.getRenderThisSign());
		renderThisSign.x = (width / 2) - renderSign.width - 4;
		renderThisSign.y = (height / 2) + 8 /* 1/2 height of sign */;
		
		selectThisSign = new GuiButtonExt(CMPT_IDs.SELECT_THIS_SIGN, (width / 2) + 4 /* margin from center */ + 32 /* sign width */ + 4 /* margin from sign */, (height / 2) - 6, 75, 20, "Select Sign");
		selectThisSign.visible = tileEntity.getRenderThisSign();
		
		textLineEditor = new GuiButton(CMPT_IDs.TEXT_EDITOR, selectThisSign.x, selectThisSign.y + selectThisSign.height + 4, selectThisSign.width, selectThisSign.height, "Text Editor");
		textLineEditor.visible = tileEntity.getRenderThisSign();
		if (tileEntity.getThisSign() != null)
		{
			textLineEditor.enabled = tileEntity.getThisSign().getTextLines().size() > 0;
		}
		
		imageList = new GuiImageList(((width / 2) + 4) - 100, (height / 2) - 100, 200, 200, (sign) -> 
		{
			tileEntity.setThisSignTypeLegacy(SignTileEntity.getSignTypeNumber(sign.getType()));
			tileEntity.setThisSignVariantLegacy(sign.getVariant());
			tileEntity.setThisSignID(sign.getID());
			tileEntity.clearThisSignTextLines();
			tileEntity.performClientToServerSync();
			textLineEditor.enabled = sign.getTextLines().size() > 0;
			imageList.setVisible(false);
		});
		imageList.setVisible(false);
		
		imageListFilter = new GuiTextField(0, fontRenderer, ((width / 2) + 4) - 100, (height / 2) + 100 + 4, 200, 20);
		
		buttonList.add(renderSign);
		buttonList.add(prevSignType);
		buttonList.add(nextSignType);
		buttonList.add(renderThisSign);
		buttonList.add(selectThisSign);
		buttonList.add(textLineEditor);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		if (tileEntity.getRenderSign())
		{
			renderSign((width / 2) + 4 /* margin from center */ + 4 /* margin from prev */ + prevSignType.width, (height / 2) - 32 - 16 - (prevSignType.height / 2));
		}
		
		if (tileEntity.getRenderThisSign())
		{
			renderThisSignVariant((width / 2) + 4 /* margin from center */, height / 2);
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		
		if (imageList.isVisible())
		{
			drawDefaultBackground();
			GlStateManager.translate(0, 0, 2);
			imageList.draw(mouseX, mouseY, fontRenderer, text -> x -> y -> drawHoveringText(text, x, y));
			GlStateManager.color(1, 1, 1);
			GlStateManager.disableLighting();
			imageListFilter.drawTextBox();
			GlStateManager.enableLighting();
			GlStateManager.translate(0, 0, -2);
		}
		
		if (textLineEditorActive)
		{
			renderSignText(true);
		}
	}
	
	private void renderSignText(boolean renderForEditor)
	{
		GlStateManager.color(1, 1, 1);
		Sign sign = tileEntity.getThisSign();
		int widthHeight = height - 100;
		if (widthHeight > width - 100)
		{
			widthHeight = width - 100;
		}
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		if (renderForEditor)
		{
			drawDefaultBackground();
			
			// Also render the sign itself since the outer method does not
			GlStateManager.translate(width / 2 - (widthHeight / 2), height / 2 - (widthHeight / 2), 2);
			Minecraft.getMinecraft().getTextureManager().bindTexture(sign.getFrontImageResourceLocation());
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			
			builder.pos(0, 0, 0).tex(0, 0).endVertex();
			builder.pos(0, widthHeight, 0).tex(0, 1).endVertex();
			builder.pos(widthHeight, widthHeight, 0).tex(1, 1).endVertex();
			builder.pos(widthHeight, 0, 0).tex(1, 0).endVertex();
			
			tess.draw();
		}	
		else
		{
			widthHeight = 32;
			GlStateManager.translate((width / 2) + 4 /* margin from center */, height / 2, 1);
		}
		
		if (sign.getTextLines().size() > 0)
		{
			double fullScale = ((double)widthHeight / (fontRenderer.FONT_HEIGHT)) / 16;
			double downScale = 1 / fullScale;
			
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.scale(fullScale, fullScale, 1);
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
				
				if (renderForEditor)
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
				
				int textWidth = fontRenderer.getStringWidth(tileEntity.getThisSignTextLine(i));
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
					fontRenderer.drawString(tileEntity.getThisSignTextLine(i), textX + 1, 1, textLine.getColor());
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
		}
		
		if (renderForEditor)
		{
			GlStateManager.translate(-(width / 2 - (widthHeight / 2)), -(height / 2 - (widthHeight / 2)), -2);
		}
		else
		{
			GlStateManager.translate(-((width / 2) + 4 /* margin from center */), -(height / 2), -1);
		}
	}
	
	private void renderSign(int left, int top)
	{
		ResourceLocation signRL = new ResourceLocation("trafficcontrol:textures/blocks/road_closed_sign.png");
		float signTextureBottomY = 0.25F;
		float heightFactor = 0.25F;
		if (tileEntity.getSignType() == SignType.LaneClosed)
		{
			signTextureBottomY = 0.5F;
		}
		else if (tileEntity.getSignType() == SignType.RoadClosedThruTraffic)
		{
			signTextureBottomY = 1F;
			heightFactor = 0.5F;
		}
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signRL);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 64, top, 0).tex(0.5, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top, 0).tex(0, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top + 32, 0).tex(0, signTextureBottomY).endVertex();
		builder.pos(left + 64, top + 32, 0).tex(0.5, signTextureBottomY).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
	
	private void renderThisSignVariant(int left, int top)
	{
		Sign thisSign = tileEntity.getThisSign();
		
		if (thisSign != null)
		{
			ResourceLocation signTexture = thisSign.getFrontImageResourceLocation();
			Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signTexture);
			
			GlStateManager.pushMatrix();
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder builder = tess.getBuffer();
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			
			builder.pos(left + 32, top, 0).tex(1, 0).endVertex();
			builder.pos(left, top, 0).tex(0, 0).endVertex();
			builder.pos(left, top + 32, 0).tex(0, 1).endVertex();
			builder.pos(left + 32, top + 32, 0).tex(1, 1).endVertex();
			
			tess.draw();
			GlStateManager.popMatrix();
			
			renderSignText(false);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (imageList.isVisible() || textLineEditorActive)
		{
			return;
		}
		
		switch(button.id)
		{
			case CMPT_IDs.RENDER_SIGN:
				boolean checked = ((GuiCheckBox)button).isChecked();
				tileEntity.setRenderSign(checked);
				prevSignType.visible = checked;
				nextSignType.visible = checked;
				break;
			case CMPT_IDs.NEXT_SIGN_TYPE:
				tileEntity.nextSignType();
				break;
			case CMPT_IDs.PREV_SIGN_TYPE:
				tileEntity.prevSignType();
				break;
			case CMPT_IDs.RENDER_THIS_SIGN:
				boolean thisSignChecked = ((GuiCheckBox)button).isChecked();
				tileEntity.setRenderThisSign(thisSignChecked);
				selectThisSign.visible = thisSignChecked;
				textLineEditor.visible = thisSignChecked;
				textLineEditor.enabled = false;
				break;
			case CMPT_IDs.SELECT_THIS_SIGN:
				imageList.setVisible(true);
				imageListFilter.setVisible(true);
				break;
			case CMPT_IDs.TEXT_EDITOR:
				textLineEditorActive = true;
				break;
		}
		super.actionPerformed(button);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (imageList.isVisible())
		{
			imageList.onMouseClick(mouseX, mouseY);
			imageListFilter.mouseClicked(mouseX, mouseY, mouseButton);
		}
		else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		imageList.onMouseRelease();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode != Keyboard.KEY_ESCAPE || (!imageList.isVisible() && !textLineEditorActive))
		{
			super.keyTyped(typedChar, keyCode);
		}
		
		if (imageList.isVisible())
		{
			imageListFilter.textboxKeyTyped(typedChar, keyCode);
			imageList.filter(imageListFilter.getText());
			
			if (keyCode == Keyboard.KEY_ESCAPE)
			{
				imageList.setVisible(false);
				imageList.filter(null);
				imageListFilter.setVisible(false);
			}
		}
		
		if (textLineEditorActive)
		{
			if (keyCode == Keyboard.KEY_ESCAPE)
			{
				textLineEditorActive = false;
				currentTextLine = 0;
				return;
			}
			
			applyTextToEditor(typedChar, keyCode);
		}
	}
	
	private void applyTextToEditor(char typedChar, int keyCode) throws IOException
	{
		String currentText = tileEntity.getThisSignTextLine(currentTextLine);
		if (currentText == null)
		{
			currentText = "";
		}
		
		if (keyCode == Keyboard.KEY_BACK && !currentText.isEmpty())
		{
			currentText = currentText.substring(0, currentText.length() - 1);
			tileEntity.setThisSignTextLine(currentTextLine, currentText);
		}
		else if (keyCode == Keyboard.KEY_DOWN)
		{
			int maxTextLines = tileEntity.getThisSign().getTextLines().size();
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
			int maxTextLines = tileEntity.getThisSign().getTextLines().size();
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
			if (currentTextLine == tileEntity.getThisSign().getTextLines().size() - 1) // Last text line
			{
				keyTyped(' ', Keyboard.KEY_ESCAPE);
			}
			else
			{
				currentTextLine++;
			}
		}
		else if (keyCode != Keyboard.KEY_BACK)
		{
			Sign.TextLine textLine = tileEntity.getThisSign().getTextLines().get(currentTextLine);
			if (textLine.getMaxLength() == currentText.length() || typedChar == 0 || (Character.isWhitespace(typedChar) && keyCode != Keyboard.KEY_SPACE))
			{
				return;
			}
			
			currentText += typedChar;
			tileEntity.setThisSignTextLine(currentTextLine, currentText);
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		if (imageList.isVisible())
		{
			int scroll = Integer.signum(Mouse.getEventDWheel());
			imageList.scroll(scroll);
		}
	}
	
	@Override
	public void onGuiClosed() {
		tileEntity.syncConnectedBarriers(true);
		super.onGuiClosed();
	}
	
	public static class CMPT_IDs
	{
		public static final int RENDER_SIGN = 1;
		public static final int PREV_SIGN_TYPE = 2;
		public static final int NEXT_SIGN_TYPE = 3;
		public static final int RENDER_THIS_SIGN = 4;
		public static final int SELECT_THIS_SIGN = 5;
		public static final int TEXT_EDITOR = 6;
	}
}
