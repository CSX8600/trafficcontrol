package com.clussmanproductions.trafficcontrol.gui;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.gui.BaseTrafficLightFrameContainer.FrameSlotInfo.EnumCheckboxOrientation;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class TrafficLightDoghouseFrameContainer extends BaseTrafficLightFrameContainer {

	public TrafficLightDoghouseFrameContainer(InventoryPlayer player, ItemStack frameStack) {
		super(player, frameStack);
	}
	
	@Override
	protected List<FrameSlotInfo> getItemSlots(IItemHandler frameStackHandler) {
		return ImmutableList
				.<FrameSlotInfo>builder()
				.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 0, 79, 10)))
				.add(new FrameSlotInfo(EnumCheckboxOrientation.LEFT, new SlotItemHandlerListenable(frameStackHandler, 1, 48, 54)))
				.add(new FrameSlotInfo(EnumCheckboxOrientation.LEFT, new SlotItemHandlerListenable(frameStackHandler, 2, 48, 86)))
				.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 3, 110, 54)))
				.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 4, 110, 86)))
				.build();
	}

	@Override
	protected int getYSize() {
		// TODO Auto-generated method stub
		return 210;
	}

	@Override
	protected BaseItemTrafficLightFrame getValidFrameItem() {
		// TODO Auto-generated method stub
		return ModItems.traffic_light_doghouse_frame;
	}

}
