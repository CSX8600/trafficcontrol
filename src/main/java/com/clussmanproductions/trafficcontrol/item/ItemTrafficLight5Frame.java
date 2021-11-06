package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight5Upper;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

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

public class ItemTrafficLight5Frame extends BaseItemTrafficLightFrame {

	public ItemTrafficLight5Frame() {
		super("traffic_light_5_frame");
	}

	@Override
	protected int getGuiID() {
		// TODO Auto-generated method stub
		return GuiProxy.GUI_IDs.TRAFFIC_LIGHT_5_FRAME;
	}

	@Override
	public int getBulbCount() {
		// TODO Auto-generated method stub
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
			tooltip.add("Upper Middle: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(2);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Middle: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(3);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Lower Middle: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
		
		subStack = handler.getStackInSlot(4);
		if (subStack != ItemStack.EMPTY)
		{
			tooltip.add("Bottom: " + subStack.getItem().getItemStackDisplayName(subStack));
		}
	}

	@Override
	protected BlockBaseTrafficLight getBaseBlockTrafficLight() {
		return ModBlocks.traffic_light_5;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (worldIn.isRemote)
		{
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ); 
		}
		
		IBlockState stateAbove = null;
		BlockPos stateAbovePos = null;
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
		{
			stateAbove = worldIn.getBlockState(pos.up());
			stateAbovePos = pos.up();
		}
		else if (worldIn.getBlockState(pos.offset(facing)).getBlock().isReplaceable(worldIn, pos.offset(facing)))
		{
			stateAbove = worldIn.getBlockState(pos.offset(facing).up());
			stateAbovePos = pos.offset(facing).up();
		}
		else
		{
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
		
		if (!stateAbove.getBlock().isReplaceable(worldIn, pos))
		{
			return EnumActionResult.FAIL;
		}
		
		worldIn.setBlockState(stateAbovePos, ModBlocks.traffic_light_5_upper.getDefaultState().withProperty(BlockTrafficLight5Upper.ROTATION, CustomAngleCalculator.getRotationForYaw(player.rotationYaw)));
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
