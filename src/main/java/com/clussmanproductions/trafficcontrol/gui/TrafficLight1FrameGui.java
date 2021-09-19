package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLight1FrameGui extends BaseTrafficLightFrameGui
{
	public TrafficLight1FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLight1FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "traffic_light_1_frame_gui.png";
	}
}
