package com.clussmanproductions.trafficcontrol.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BaseTrafficLightFrameContainer extends Container {
	IItemHandler frameStackHandler;
	ItemStack frameStack;
	
	public BaseTrafficLightFrameContainer(InventoryPlayer inventory, ItemStack frameStack)
	{
		int ySize = getYSize();
		
		// Hot bar
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventory, i, 7 + i * 18, 177 + (ySize - 200)));
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
			
			addSlotToContainer(new Slot(inventory, i, 7 + column * 18, 119 + (ySize - 200) + 18 * row));
		}
		
		// Item inventory
		frameStackHandler = frameStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for(Slot slot : getItemSlots(frameStackHandler).stream().map(fsi -> fsi.getSlot()).collect(Collectors.toList()))
		{
			addSlotToContainer(slot);
		}
		
		this.frameStack = frameStack;
	}
	
	protected abstract List<FrameSlotInfo> getItemSlots(IItemHandler frameStackHandler);
	
	protected abstract int getYSize();
	
	public ItemStack getFrameStack() { return frameStack; }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.getHeldItemMainhand().getItem() == getValidFrameItem();
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
				BaseItemTrafficLightFrame heldFrame = (BaseItemTrafficLightFrame)playerIn.getHeldItemMainhand().getItem();
				
				boolean didMerge = false;
				for(int i = 0; i < heldFrame.getBulbCount(); i++)
				{
					Slot frameSlot = inventorySlots.get(i + 36);
					
					if (!frameSlot.getHasStack() && frameSlot.isItemValid(originStack))
					{
						mergeItemStack(originStack, i + 36, i + 37, false);
						didMerge = true;
						break;
					}
				}
				
				if (!didMerge)
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
	
	protected abstract BaseItemTrafficLightFrame getValidFrameItem();

	public static class FrameSlotInfo
	{
		private EnumCheckboxOrientation checkboxOrientation;
		private SlotItemHandlerListenable slot;
		
		public FrameSlotInfo(EnumCheckboxOrientation checkboxOrienation, SlotItemHandlerListenable slot)
		{
			this.checkboxOrientation = checkboxOrienation;
			this.slot = slot;
		}
		
		public EnumCheckboxOrientation getCheckboxOrientation()
		{
			return checkboxOrientation;
		}
		
		public SlotItemHandlerListenable getSlot()
		{
			return slot;
		}
		
		public enum EnumCheckboxOrientation
		{
			LEFT,
			RIGHT,
			ABOVE,
			BELOW;
		}
	}
}
