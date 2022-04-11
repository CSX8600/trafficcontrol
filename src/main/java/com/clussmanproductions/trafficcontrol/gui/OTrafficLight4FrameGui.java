package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLight4FrameGui extends BaseTrafficLightFrameGui
{
	public OTrafficLight4FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new OTrafficLight4FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "otraffic_light_4_frame_gui.png";
	}
}
