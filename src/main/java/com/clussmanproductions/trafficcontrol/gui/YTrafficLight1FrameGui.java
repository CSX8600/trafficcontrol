package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLight1FrameGui extends BaseTrafficLightFrameGui
{
	public YTrafficLight1FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new YTrafficLight1FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "ytraffic_light_1_frame_gui.png";
	}
}
