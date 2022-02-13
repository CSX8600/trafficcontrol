package com.clussmanproductions.trafficcontrol.gui;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.gui.BaseTrafficLightFrameContainer.FrameSlotInfo.EnumCheckboxOrientation;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class YTrafficLight5FrameContainer extends BaseTrafficLightFrameContainer {

	public YTrafficLight5FrameContainer(InventoryPlayer inventory, ItemStack frameStack) {
		super(inventory, frameStack);
	}
		
	@Override
	protected List<FrameSlotInfo> getItemSlots(IItemHandler frameStackHandler) {
		return ImmutableList
			.<FrameSlotInfo>builder()
			.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 0, 79, 12)))
			.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 1, 79, 44)))
			.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 2, 79, 76)))
			.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 3, 79, 107)))
			.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 4, 79, 139)))
			.build();
	}


	@Override
	protected BaseItemTrafficLightFrame getValidFrameItem() {
		return ModItems.ytraffic_light_5_frame;
	}

	@Override
	protected int getYSize() {
		// TODO Auto-generated method stub
		return 263;
	}	
}
