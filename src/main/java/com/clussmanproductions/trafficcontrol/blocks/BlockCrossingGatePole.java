package com.clussmanproductions.trafficcontrol.blocks;

import java.util.Arrays;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCrossingGatePole extends Block {
	public static PropertyBool NORTH = PropertyBool.create("north");
	public static PropertyBool WEST = PropertyBool.create("west");
	public static PropertyBool SOUTH = PropertyBool.create("south");
	public static PropertyBool EAST = PropertyBool.create("east");
	
	public BlockCrossingGatePole()
	{
		super(Material.IRON);
		setRegistryName("crossing_gate_pole");
		setUnlocalizedName(ModTrafficControl.MODID + ".crossing_gate_pole");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		setHardness(2f);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, WEST, SOUTH, EAST);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean north = getStateIsValidForSubModel(worldIn.getBlockState(pos.north()), EnumFacing.NORTH);
		boolean west = getStateIsValidForSubModel(worldIn.getBlockState(pos.west()), EnumFacing.WEST);
		boolean south = getStateIsValidForSubModel(worldIn.getBlockState(pos.south()), EnumFacing.SOUTH);
		boolean east = getStateIsValidForSubModel(worldIn.getBlockState(pos.east()), EnumFacing.EAST);
		
		return state
				.withProperty(NORTH, north)
				.withProperty(WEST, west)
				.withProperty(SOUTH, south)
				.withProperty(EAST, east);
	}
	
	public boolean getStateIsValidForSubModel(IBlockState state, EnumFacing facing)
	{
		if (state.getBlock() == ModBlocks.horizontal_pole)
		{
			EnumFacing stateFacing = state.getValue(BlockHorizontalPole.FACING);
			
			return (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) ? 
					stateFacing == EnumFacing.NORTH || stateFacing == EnumFacing.SOUTH :
					stateFacing == EnumFacing.WEST || stateFacing == EnumFacing.EAST;
		}
		
		if (state.getBlock() == ModBlocks.traffic_light)
		{
			EnumFacing stateFacing = state.getValue(BlockTrafficLight.FACING);
			return stateFacing != facing;
		}
		
		return false;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.4325, 0, 0.4325, 0.5575, 1, 0.5575);
	}
}
