package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLight1FrameGui extends BaseTrafficLightFrameGui
{
	public OTrafficLight1FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new OTrafficLight1FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}
	
	@Override
	protected String getGuiPngName()
	{
		return "otraffic_light_1_frame_gui.png";
	}
}
