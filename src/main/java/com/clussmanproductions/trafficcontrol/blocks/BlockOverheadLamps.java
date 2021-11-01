package com.clussmanproductions.trafficcontrol.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOverheadLamps extends BlockLampBase {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	@Override
	protected String getLampRegistryName() {
		return "overhead_lamps";
	}	
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		// legacy from when EnumState was part of meta
		int workingMeta = meta;
		if (workingMeta >= 4)
		{
			if (workingMeta >= 8)
			{
				workingMeta -= 8;
			}
			else
			{
				workingMeta -= 4;
			}
		}
		
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(workingMeta));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public IProperty<?> getRotationalProperty() {
		return FACING;
	}
}
