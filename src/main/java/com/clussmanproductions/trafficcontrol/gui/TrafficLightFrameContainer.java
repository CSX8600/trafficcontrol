package com.clussmanproductions.trafficcontrol.gui;

import com.clussmanproductions.trafficcontrol.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TrafficLightFrameContainer extends Container {

	IItemHandler frameStackHandler;
	public TrafficLightFrameContainer(InventoryPlayer inventory, ItemStack frameStack)
	{
		// Hot bar
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventory, i, 7 + i * 18, 177));
		}
		
		// Player inventory
		for(int i = 9; i < 36; i++)
		{
			int rowWork = i;
			int row = 0;
			while (rowWork - 9 >= 9)
			{
				row++;
				rowWork -= 9;
			}
			
			int column = i % 9;
			
			addSlotToContainer(new Slot(inventory, i, 7 + column * 18, 119 + 18 * row));
		}
		
		// Item inventory
		frameStackHandler = frameStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(frameStackHandler, 0, 79, 13));
		addSlotToContainer(new SlotItemHandler(frameStackHandler, 1, 79, 44));
		addSlotToContainer(new SlotItemHandler(frameStackHandler, 2, 79, 76));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.getHeldItemMainhand().getItem() == ModItems.traffic_light_frame;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = inventorySlots.get(index);
		ItemStack originStack = null;
		ItemStack destinationStack = ItemStack.EMPTY;
		
		if (slot.getHasStack())
		{
			originStack = slot.getStack();
			destinationStack = originStack.copy();
			
			if (index > 35)
			{
				if (!mergeItemStack(originStack, 0, 35, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				Slot upper = inventorySlots.get(36);
				Slot middle = inventorySlots.get(37);
				Slot lower = inventorySlots.get(38);
				
				if (!upper.getHasStack() && upper.isItemValid(originStack))
				{
					mergeItemStack(originStack, 36, 37, false);
				}
				else if (!middle.getHasStack() && middle.isItemValid(originStack))
				{
					mergeItemStack(originStack, 37, 38, false);
				}
				else if (!lower.getHasStack() && lower.isItemValid(originStack))
				{
					mergeItemStack(originStack, 38, 39, false);
				}
				else
				{
					return ItemStack.EMPTY;
				}
			}
		}
		else
		{
			return ItemStack.EMPTY;
		}
		
		if (originStack.isEmpty())
		{
			slot.putStack(ItemStack.EMPTY);
		}
		else
		{
			slot.onSlotChanged();
		}
		
		return destinationStack;
	}
}
