package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLightDoghouseFrameGui extends BaseTrafficLightFrameGui {

	public YTrafficLightDoghouseFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new YTrafficLightDoghouseFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 210;
	}
	
	@Override
	protected String getGuiPngName() {
		return "ytraffic_light_doghouse_frame_gui.png";
	}
	
}
