package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight5Upper;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemTrafficLight7Frame extends BaseItemTrafficLightFrame
{
	public ItemTrafficLight7Frame()
	{
		super("traffic_light_7_frame");
	}
	
	@Override
	public int getBulbCount()
	{
		return 3;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack subStack = handler.getStackInSlot(0);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Top Left: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(1);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Top Right: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(2);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Bottom: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		
	}
	
	@Override
	protected BlockBaseTrafficLight getBaseBlockTrafficLight() 
	{
		return ModBlocks.traffic_light_7;
	}

	@Override
	protected int getGuiID() 
	{
		return GuiProxy.GUI_IDs.TRAFFIC_LIGHT_7_FRAME;
	}
}
