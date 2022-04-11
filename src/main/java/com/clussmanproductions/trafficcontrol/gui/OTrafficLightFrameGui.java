package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class OTrafficLightFrameGui extends BaseTrafficLightFrameGui {

	public OTrafficLightFrameGui(InventoryPlayer inventory, ItemStack frameStack) {
		super(new YTrafficLightFrameContainer(inventory, frameStack));
		
		xSize = 174;
		ySize = 200;
	}

	@Override
	protected String getGuiPngName() {
		return "otraffic_light_frame_gui.png";
	}

}
