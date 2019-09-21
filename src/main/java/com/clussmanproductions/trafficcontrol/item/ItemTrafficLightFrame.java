package com.clussmanproductions.trafficcontrol.item;

import java.util.HashMap;
import java.util.List;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemTrafficLightFrame extends Item {
	public ItemTrafficLightFrame()
	{
		super();
		setRegistryName("traffic_light_frame");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_frame");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.rayTrace(EntityPlayer.REACH_DISTANCE.getDefaultValue(), 0).typeOfHit != Type.BLOCK)
		{
			playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.TRAFFIC_LIGHT_FRAME, worldIn, 0, 0, 0);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ItemTrafficLightFrameCapabilityProvider();
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}
		
		worldIn.setBlockState(pos, ModBlocks.traffic_light.getDefaultState().withProperty(BlockTrafficLight.FACING, player.getHorizontalFacing()));
		TrafficLightTileEntity trafficLight = (TrafficLightTileEntity)worldIn.getTileEntity(pos);
		
		HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(3);
		IItemHandler handler = player.getHeldItem(hand).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		bulbsBySlot.put(0, EnumTrafficLightBulbTypes.get(handler.getStackInSlot(0).getMetadata()));
		bulbsBySlot.put(1, EnumTrafficLightBulbTypes.get(handler.getStackInSlot(1).getMetadata()));
		bulbsBySlot.put(2, EnumTrafficLightBulbTypes.get(handler.getStackInSlot(2).getMetadata()));
		
		trafficLight.setBulbsBySlot(bulbsBySlot);
		
		return EnumActionResult.SUCCESS;
	}
}
