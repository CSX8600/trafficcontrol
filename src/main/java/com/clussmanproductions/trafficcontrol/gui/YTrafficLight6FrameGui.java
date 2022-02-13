package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLight6FrameGui extends BaseTrafficLightFrameGui {

	public YTrafficLight6FrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new YTrafficLight6FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "ytraffic_light_6_frame_gui.png";
	}
	
}
