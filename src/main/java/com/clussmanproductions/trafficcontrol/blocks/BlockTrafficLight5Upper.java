package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrafficLight5Upper extends Block {
	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	
	public BlockTrafficLight5Upper() {
		super(Material.IRON);
		setRegistryName("traffic_light_5_upper");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_5_upper");
		setHardness(2);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return CustomAngleCalculator.rotationToMeta(state.getValue(ROTATION));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, CustomAngleCalculator.metaToRotation(meta));
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != ModBlocks.traffic_light_5_upper)
		{
			return FULL_BLOCK_AABB;
		}
		
		int rotation = state.getValue(ROTATION);
		
		switch(rotation)
		{
			case 0:
				return new AxisAlignedBB(0.1875, 0, 0.4375, 0.8125, 1, 0.75);
			case 8:
				return new AxisAlignedBB(0.1875, 0, 0.25, 0.8125, 1, 0.5625);
			case 4:
				return new AxisAlignedBB(0.25, 0, 0.1875, 0.5625, 1, 0.8125);
			case 12:
				return new AxisAlignedBB(0.4375, 0, 0.1875, 0.75, 1, 0.8125);
			case 1:
			case 15:
			case 7:
			case 9:
			case 3:
			case 5:
			case 11:
			case 13:
				return new AxisAlignedBB(0.375, 0, 0.375, 0.75, 1, 0.75);
			case 2:
			case 6:
			case 10:
			case 14:
				return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 1, 0.8);
		}
		
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.traffic_light_5)
		{
			worldIn.setBlockToAir(pos.down());
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		IBlockState stateBelow = world.getBlockState(pos.down());
		
		if (stateBelow.getBlock() != ModBlocks.traffic_light_5)
		{
			return super.getPickBlock(stateBelow, target, world, pos, player);
		}
		
		return ModBlocks.traffic_light_5.getPickBlock(stateBelow, target, world, pos.down(), player);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (fromPos.equals(pos.offset(EnumFacing.DOWN)) && worldIn.getBlockState(fromPos).getBlock() == ModBlocks.traffic_light_5)
		{
			worldIn.setBlockState(pos, state.withProperty(ROTATION, worldIn.getBlockState(fromPos).getValue(BlockTrafficLight5.ROTATION)));
		}
	}
}
