package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class YTrafficLightFrameGui extends BaseTrafficLightFrameGui {

	public YTrafficLightFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new YTrafficLightFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}

	@Override
	protected String getGuiPngName() {
		return "ytraffic_light_frame_gui.png";
	}

}
