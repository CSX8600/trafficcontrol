package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLight6FrameGui extends BaseTrafficLightFrameGui {

	public OTrafficLight6FrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new OTrafficLight6FrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "otraffic_light_6_frame_gui.png";
	}
	
}
