package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLight5FrameGui extends BaseTrafficLightFrameGui {

	public YTrafficLight5FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new YTrafficLight5FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "ytraffic_light_5_frame_gui.png";
	}

	
}
