package com.clussmanproductions.trafficcontrol.gui;

import com.clussmanproductions.trafficcontrol.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TrafficLightFrameContainer extends Container {

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
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.getHeldItemMainhand().getItem() == ModItems.traffic_light_frame;
	}

}
