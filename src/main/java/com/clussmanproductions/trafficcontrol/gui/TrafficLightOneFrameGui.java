package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLightOneFrameGui extends BaseTrafficLightFrameGui
{
	public TrafficLightOneFrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new TrafficLightOneFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "traffic_light_one_frame_gui.png";
	}
}
