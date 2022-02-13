package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLight2FrameGui extends BaseTrafficLightFrameGui
{
	public YTrafficLight2FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new YTrafficLight2FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "ytraffic_light_2_frame_gui.png";
	}
}
