package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight2FrameGui extends BaseTrafficLightFrameGui
{
	public TrafficLight2FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLight2FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "traffic_light_2_frame_gui.png";
	}
}
