package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLight4FrameGui extends BaseTrafficLightFrameGui
{
	public YTrafficLight4FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new YTrafficLight4FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "ytraffic_light_4_frame_gui.png";
	}
}
