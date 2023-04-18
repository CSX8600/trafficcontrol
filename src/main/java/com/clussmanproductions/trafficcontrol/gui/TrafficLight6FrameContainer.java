package com.clussmanproductions.trafficcontrol.gui;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.gui.BaseTrafficLightFrameContainer.FrameSlotInfo;
import com.clussmanproductions.trafficcontrol.gui.BaseTrafficLightFrameContainer.FrameSlotInfo.EnumCheckboxOrientation;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class TrafficLight6FrameContainer extends BaseTrafficLightFrameContainer {

	public TrafficLight6FrameContainer(InventoryPlayer player, ItemStack frameStack) {
		super(player, frameStack);
	}
	
	@Override
	protected List<FrameSlotInfo> getItemSlots(IItemHandler frameStackHandler) 
		{
            return ImmutableList
                    .<FrameSlotInfo>builder()
					.add(new FrameSlotInfo(EnumCheckboxOrientation.LEFT, new SlotItemHandlerListenable(frameStackHandler, 0, 59, 11)))
                    .add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 1, 110, 11)))
                    .add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 2, 83, 52)))
					.add(new FrameSlotInfo(EnumCheckboxOrientation.RIGHT, new SlotItemHandlerListenable(frameStackHandler, 3, 83, 95)))
                    .build();
        }

	@Override
	protected int getYSize() {
		
		return 210;
	}

	@Override
	protected BaseItemTrafficLightFrame getValidFrameItem() {
	
		return ModItems.traffic_light_6_frame;
	}

}
