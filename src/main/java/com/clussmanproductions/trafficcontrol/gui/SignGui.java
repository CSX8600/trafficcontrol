package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class SignGui extends GuiScreen {

	SignTileEntity sign;
	GuiImageList list;
	
	public SignGui(SignTileEntity sign)
	{
		this.sign = sign;
	}
	
	@Override
	public void initGui() {
		list = new GuiImageList(50, 50, width - 100, height - 100, new ResourceLocation("trafficcontrol:misc/signs.json"), sign.getType(), sign.getVariant());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		list.draw(mouseX, mouseY, fontRenderer, text -> x -> y -> drawHoveringText(text, x, y));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		list.onMouseClick(mouseX, mouseY);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		
		list.onMouseRelease();
	}
}
