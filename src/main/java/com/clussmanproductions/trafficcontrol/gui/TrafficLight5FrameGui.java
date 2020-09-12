package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight5FrameGui extends BaseTrafficLightFrameGui {

	public TrafficLight5FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLight5FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "traffic_light_5_frame_gui.png";
	}

	
}
