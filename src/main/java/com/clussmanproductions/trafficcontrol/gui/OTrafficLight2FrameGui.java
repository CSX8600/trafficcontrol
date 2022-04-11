package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLight2FrameGui extends BaseTrafficLightFrameGui
{
	public OTrafficLight2FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new OTrafficLight2FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "otraffic_light_2_frame_gui.png";
	}
}
