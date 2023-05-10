package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight7FrameGui extends BaseTrafficLightFrameGui {

	public TrafficLight7FrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new TrafficLight7FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "traffic_light_7_frame_gui.png";
	}
	
}
