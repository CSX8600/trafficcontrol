package com.clussmanproductions.trafficcontrol.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemTrafficLightFrameCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	private final ItemStackHandler handler;
	private ItemStack stack;
	
	public ItemTrafficLightFrameCapabilityProvider(ItemStack stack) {
		handler = new ItemStackHandler(3)
		{			
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return stack.getItem() instanceof ItemTrafficLightBulb;
			}
			
			@Override
			protected int getStackLimit(int slot, ItemStack stack) {
				return 1;
			}
		};
		this.stack = stack;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("items", handler.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("items"))
		{
			handler.deserializeNBT((NBTTagCompound)nbt.getTag("items"));
		}
	}	
}
