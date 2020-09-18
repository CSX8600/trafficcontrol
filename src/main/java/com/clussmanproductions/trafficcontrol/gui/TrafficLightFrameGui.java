package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLightFrameGui extends BaseTrafficLightFrameGui {

	public TrafficLightFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new TrafficLightFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}

	@Override
	protected String getGuiPngName() {
		return "traffic_light_frame_gui.png";
	}

}
