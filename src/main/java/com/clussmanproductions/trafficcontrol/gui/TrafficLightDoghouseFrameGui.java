package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLightDoghouseFrameGui extends BaseTrafficLightFrameGui {

	public TrafficLightDoghouseFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new TrafficLightDoghouseFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "traffic_light_doghouse_frame_gui.png";
	}
	
}
