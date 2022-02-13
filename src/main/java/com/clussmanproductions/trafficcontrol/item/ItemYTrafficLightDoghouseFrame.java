package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemYTrafficLightDoghouseFrame extends BaseItemTrafficLightFrame {

	public ItemYTrafficLightDoghouseFrame() {
		super("ytraffic_light_doghouse_frame");
	}
	
	@Override
	protected int getGuiID() {
		return GuiProxy.GUI_IDs.YTRAFFIC_LIGHT_DOGHOUSE_FRAME;
	}

	@Override
	public int getBulbCount() {
		return 5;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack subStack = handler.getStackInSlot(0);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Top: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(1);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Upper Left: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(2);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Lower Left: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(3);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Upper Right: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(4);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Lower Right: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
	}

	@Override
	protected BlockBaseTrafficLight getBaseBlockTrafficLight() {
		return ModBlocks.ytraffic_light_doghouse;
	}
}
