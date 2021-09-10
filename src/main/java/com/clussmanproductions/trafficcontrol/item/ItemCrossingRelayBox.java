package com.clussmanproductions.trafficcontrol.item;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayNE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayNW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelaySE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelaySW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopNE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopNW;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopSE;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingRelayTopSW;
import com.clussmanproductions.trafficcontrol.tileentity.RelayTileEntity;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCrossingRelayBox extends Item {
	private final TextComponentTranslation tooltipInfo = new TextComponentTranslation("tc.tt.crossingrelay");
	public ItemCrossingRelayBox()
	{
		setRegistryName("crossing_relay_box");
		setUnlocalizedName(ModTrafficControl.MODID + ".crossing_relay_box");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		tooltipInfo.getStyle().setColor(TextFormatting.GOLD);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(tooltipInfo.getFormattedText());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		
		if (!checkSpacing(worldIn, pos, player.getHorizontalFacing()))
		{
			return EnumActionResult.SUCCESS;
		}
		
		placeMultiblock(worldIn, pos, player.getHorizontalFacing());
		
		if (!player.isCreative())
		{
			ItemStack stack = player.inventory.getCurrentItem();
			int stackSize = stack.getCount();
			if (stackSize == 1)
			{
				player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Blocks.AIR));
			}
			else
			{
				stack.setCount(stackSize--);
			}
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	private boolean checkSpacing(World world, BlockPos pos, EnumFacing facing)
	{
		BlockPos placingPos = pos.offset(EnumFacing.UP);
		EnumFacing lastFacing = facing;
		
		boolean success = checkSpacing(world, placingPos); // SE
		
		lastFacing = rotateLeft(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		success = success && checkSpacing(world, placingPos); // SW
		
		for (int i = 0; i < 2; i++) // NW & NE
		{
			lastFacing = rotateRight(lastFacing);
			placingPos = placingPos.offset(lastFacing);
			
			success = success && checkSpacing(world, placingPos);
		}
		
		placingPos = placingPos.offset(EnumFacing.UP);
		
		success = success && checkSpacing(world, placingPos); // NE
		
		for (int i = 0; i < 3; i++) // SE, SW, & NW
		{
			lastFacing = rotateRight(lastFacing);
			placingPos = placingPos.offset(lastFacing);
			
			success = success && checkSpacing(world, placingPos);
		}
		
		return success;
	}
	
	private boolean checkSpacing(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}
	
	private void placeMultiblock(World world, BlockPos pos, EnumFacing facing)
	{
		BlockPos placingPos = pos.offset(EnumFacing.UP);
		EnumFacing lastFacing = facing;
		BlockPos masterTEPos = new BlockPos(placingPos.getX(), placingPos.getY(), placingPos.getZ());
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_se.getDefaultState().withProperty(BlockCrossingRelaySE.FACING, facing));
		RelayTileEntity te = (RelayTileEntity)world.getTileEntity(placingPos); 
		te.setMaster();
		
		lastFacing = rotateLeft(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_sw.getDefaultState().withProperty(BlockCrossingRelaySW.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		lastFacing = rotateRight(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_nw.getDefaultState().withProperty(BlockCrossingRelayNW.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		lastFacing = rotateRight(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_ne.getDefaultState().withProperty(BlockCrossingRelayNE.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		placingPos = placingPos.offset(EnumFacing.UP);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_top_ne.getDefaultState().withProperty(BlockCrossingRelayTopNE.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		lastFacing = rotateRight(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_top_se.getDefaultState().withProperty(BlockCrossingRelayTopSE.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		lastFacing = rotateRight(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_top_sw.getDefaultState().withProperty(BlockCrossingRelayTopSW.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
		
		lastFacing = rotateRight(lastFacing);
		placingPos = placingPos.offset(lastFacing);
		
		world.setBlockState(placingPos, ModBlocks.crossing_relay_top_nw.getDefaultState().withProperty(BlockCrossingRelayTopNW.FACING, facing));
		((RelayTileEntity)world.getTileEntity(placingPos)).setMasterLocation(masterTEPos);
	}
	
	private EnumFacing rotateLeft(EnumFacing in)
	{
		switch(in)
		{
			case NORTH:
				return EnumFacing.WEST;
			case WEST:
				return EnumFacing.SOUTH;
			case SOUTH:
				return EnumFacing.EAST;
			case EAST:
				return EnumFacing.NORTH;
			default:
				return null;
		}
	}
	
	private EnumFacing rotateRight(EnumFacing in)
	{
		switch(in)
		{
			case NORTH:
				return EnumFacing.EAST;
			case EAST:
				return EnumFacing.SOUTH;
			case SOUTH:
				return EnumFacing.WEST;
			case WEST:
				return EnumFacing.NORTH;
			default:
				return null;
		}
	}
}
