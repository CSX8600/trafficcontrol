package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLightTwoFrameGui extends BaseTrafficLightFrameGui
{
	public TrafficLightTwoFrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLightTwoFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "traffic_light_two_frame_gui.png";
	}
}
