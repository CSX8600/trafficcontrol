package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight4FrameGui extends BaseTrafficLightFrameGui
{
	public TrafficLight4FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLight4FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "traffic_light_4_frame_gui.png";
	}
}
