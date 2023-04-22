package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight6FrameGui extends BaseTrafficLightFrameGui {

	public TrafficLight6FrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new TrafficLight6FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "traffic_light_6_frame_gui.png";
	}
	
}
