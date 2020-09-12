package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
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
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockTrafficLight5Upper() {
		super(Material.IRON);
		setRegistryName("traffic_light_5_upper");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_5_upper");
		setHardness(2);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
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
		switch(state.getValue(FACING))
		{
			case EAST:
				return new AxisAlignedBB(0.25, 0, 0.1875, 0.5625, 1, 0.8125);
			case NORTH:
				return new AxisAlignedBB(0.1875, 0, 0.4375, 0.8125, 1, 0.75);
			case SOUTH:
				return new AxisAlignedBB(0.1875, 0, 0.25, 0.8125, 1, 0.5625);
			case WEST:
				return new AxisAlignedBB(0.4375, 0, 0.1875, 0.75, 1, 0.8125);
		}
		return super.getBoundingBox(state, source, pos);
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
}
