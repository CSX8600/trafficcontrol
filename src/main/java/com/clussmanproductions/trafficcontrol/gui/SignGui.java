package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.network.PacketUpdateSign;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity.Sign;
import com.clussmanproductions.trafficcontrol.util.Tuple;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class SignGui extends GuiScreen {

	SignTileEntity sign;
	GuiImageList list;
	GuiTextField search;
	GuiButtonExt typeButton;
	GuiTextField variant;
	
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
		search.setText("§3§oSearch...");
		
		int leftPanelWidth = width - 134;
		int leftPanelHorizontalCenter = leftPanelWidth / 2;
		
		int textWidth = fontRenderer.getStringWidth("Type:");
		
		typeButton = new GuiButtonExt(1, leftPanelHorizontalCenter - ((203 + textWidth) / 2) + textWidth + 3, 20, sign.getFriendlySignName());
		
		if (typeButton.x + typeButton.width > leftPanelWidth)
		{
			typeButton.width = leftPanelWidth - typeButton.x;
		}
		
		textWidth = fontRenderer.getStringWidth("Variant:");
		variant = new GuiTextField(2, fontRenderer, leftPanelHorizontalCenter - ((33 + textWidth) / 2) + textWidth + 3, 50, 30, 20);
		variant.setText(String.valueOf(sign.getVariant()));
		variant.setMaxStringLength(3);
		
		buttonList.add(typeButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		search.drawTextBox();
		variant.drawTextBox();
		
		int leftPanelWidth = width - 134;
		int leftPanelHorizontalCenter = leftPanelWidth / 2;
		
		int textWidth = fontRenderer.getStringWidth("Type:");
		fontRenderer.drawString("Type:", leftPanelHorizontalCenter - ((typeButton.width + textWidth + 3) / 2), 27, 0xFFFFFF);
		
		textWidth = fontRenderer.getStringWidth("Variant:");
		fontRenderer.drawString("Variant:", leftPanelHorizontalCenter - ((variant.width + textWidth + 3) / 2), 57, 0xFFFFFF);
		
		Sign currentSign = SignTileEntity.SIGNS_BY_TYPE_VARIANT.get(new Tuple<Integer, Integer>(sign.getType(), sign.getVariant()));
		
		textWidth = fontRenderer.getStringWidth("Name: " + currentSign.getName());
		fontRenderer.drawString("Name: " + currentSign.getName(), leftPanelHorizontalCenter - (textWidth / 2), 73, 0xFFFF00);
		
		if (currentSign.getNote() != null && !currentSign.getNote().equals(""))
		{
			String note = "Note: " + currentSign.getNote();
			textWidth = fontRenderer.getStringWidth(note);
			if (textWidth > leftPanelWidth)
			{
				textWidth = leftPanelWidth;
			}

			fontRenderer.drawSplitString(note, leftPanelHorizontalCenter - (textWidth / 2), height - 20, textWidth, 0xFFFFFF);
		}
		
		mc.renderEngine.bindTexture(currentSign.getImageResourceLocation());
		
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
		
		builder.pos(previewX, 83, 1).tex(0, 0).endVertex();
		builder.pos(previewX, 83 + previewWidthHeight, 1).tex(0, 1).endVertex();
		builder.pos(previewX + previewWidthHeight, 83 + previewWidthHeight, 1).tex(1, 1).endVertex();
		builder.pos(previewX + previewWidthHeight, 83, 1).tex(1, 0).endVertex();
		
		tess.draw();
		
		list.draw(mouseX, mouseY, fontRenderer, text -> x -> y -> drawHoveringText(text, x, y));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		list.onMouseClick(mouseX, mouseY);
		if (search.mouseClicked(mouseX, mouseY, mouseButton) && search.getText().equals("§3§oSearch..."))
		{
			search.setText("");
		}
		else if (search.getText().equals(""))
		{
			search.setText("§3§oSearch...");
		}
		variant.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		if (search.textboxKeyTyped(typedChar, keyCode))
		{
			list.filter(search.getText());
		}
		
		if (variant.textboxKeyTyped(typedChar, keyCode))
		{
			int newVariant = 0;
			try
			{
				newVariant = Integer.parseInt(variant.getText());
			}
			catch(Exception ex){}
			
			sign.setVariant(newVariant);
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
		sign.setType(image.getType());
		sign.setVariant(image.getVariant());
		
		typeButton.displayString = sign.getFriendlySignName();
		variant.setText(String.valueOf(image.getVariant()));
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		BlockPos pos = sign.getPos();
		PacketUpdateSign updateSign = new PacketUpdateSign();
		updateSign.x = pos.getX();
		updateSign.y = pos.getY();
		updateSign.z = pos.getZ();
		updateSign.type = sign.getType();
		updateSign.variant = sign.getVariant();
		
		PacketHandler.INSTANCE.sendToServer(updateSign);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == typeButton)
		{
			sign.nextType();
			
			typeButton.displayString = sign.getFriendlySignName();
			variant.setText(String.valueOf(sign.getVariant()));
		}
	}
}
