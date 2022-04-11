package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLight5FrameGui extends BaseTrafficLightFrameGui {

	public OTrafficLight5FrameGui(InventoryPlayer inventory, ItemStack frameStack)
	{
		super(new OTrafficLight5FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 263;
	}
	
	@Override
	protected String getGuiPngName() {
		return "otraffic_light_5_frame_gui.png";
	}

	
}
