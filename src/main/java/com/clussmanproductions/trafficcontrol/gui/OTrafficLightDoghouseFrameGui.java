package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLightDoghouseFrameGui extends BaseTrafficLightFrameGui {

	public OTrafficLightDoghouseFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new OTrafficLightDoghouseFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "otraffic_light_doghouse_frame_gui.png";
	}
	
}
