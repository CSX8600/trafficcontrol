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

public class ItemTrafficLight1Frame extends BaseItemTrafficLightFrame
{
	public ItemTrafficLight1Frame()
	{
		super("traffic_light_1_frame");
	}
	
	@Override
	protected int getGuiID()
	{
		return GuiProxy.GUI_IDs.TRAFFIC_LIGHT_1_FRAME;
	}
	
	@Override
	public int getBulbCount()
	{
		return 1;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flag)
	{
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack subStack = handler.getStackInSlot(0);
		if(subStack != ItemStack.EMPTY)
		{
			tooltip.add("Top: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
	}
	
	@Override
	protected BlockBaseTrafficLight getBaseBlockTrafficLight()
	{
		return ModBlocks.traffic_light_1;
	}
}
