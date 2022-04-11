package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemOTrafficLightFrame extends BaseItemTrafficLightFrame {

	public ItemOTrafficLightFrame()
	{
		super("otraffic_light_frame");
		
	}
	
	@Override
	protected int getGuiID() {
		return GuiProxy.GUI_IDs.OTRAFFIC_LIGHT_FRAME;
	}

	@Override
	public int getBulbCount() {
		return 3;
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
			tooltip.add("Middle: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(2);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Bottom: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
	}

	@Override
	protected BlockBaseTrafficLight getBaseBlockTrafficLight() {
		return ModBlocks.otraffic_light;
	}
	
}
