package com.clussmanproductions.trafficcontrol.gui;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TrafficLightFrameGui extends GuiContainer {

	ItemStack frameStack;
	public TrafficLightFrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLightFrameContainer(inventory, frameStack));
		this.frameStack = frameStack;
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModTrafficControl.MODID + ":textures/gui/traffic_light_frame_gui.png"));
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);
	}

}
