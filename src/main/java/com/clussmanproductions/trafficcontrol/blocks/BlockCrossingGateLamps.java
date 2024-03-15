package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.util.UnlistedPropertyBoolean;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockCrossingGateLamps extends BlockLampBase implements IHorizontalPoleConnectable {

	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static UnlistedPropertyBoolean NORTH = new UnlistedPropertyBoolean("north");
	public static UnlistedPropertyBoolean WEST = new UnlistedPropertyBoolean("west");
	public static UnlistedPropertyBoolean SOUTH = new UnlistedPropertyBoolean("south");
	public static UnlistedPropertyBoolean EAST = new UnlistedPropertyBoolean("east");
	public static UnlistedPropertyBoolean DOWN = new UnlistedPropertyBoolean("down");
	
	@Override
	public String getLampRegistryName() {
		return "crossing_gate_lamps";
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		int i = CustomAngleCalculator.getRotationForYaw(placer.rotationYaw);
		return getDefaultState().withProperty(ROTATION, i);
	}

	
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	public IProperty<?> getRotationalProperty() {
		return ROTATION;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		for(IProperty<?> property : super.createBlockState().getProperties())
		{
			builder.add(property);
		}
		
		builder.add(NORTH, WEST, SOUTH, EAST, DOWN);
		return builder.build();
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((IExtendedBlockState)super.getExtendedState(state, world, pos))
				.withProperty(NORTH, checkDirection(world, pos, EnumFacing.NORTH))
				.withProperty(WEST, checkDirection(world, pos, EnumFacing.WEST))
				.withProperty(SOUTH, checkDirection(world, pos, EnumFacing.SOUTH))
				.withProperty(EAST, checkDirection(world, pos, EnumFacing.EAST))
				.withProperty(DOWN, checkDirection(world, pos, EnumFacing.DOWN));
	}
	
	private boolean checkDirection(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState otherState = world.getBlockState(pos.offset(facing));
		if (facing == EnumFacing.DOWN && otherState.getBlock().getRegistryName().getResourceDomain().equalsIgnoreCase(ModTrafficControl.MODID))
		{
			return true;
		}
		
		if (otherState.getBlock() instanceof IHorizontalPoleConnectable)
		{
			return ((IHorizontalPoleConnectable)otherState.getBlock()).canConnectHorizontalPole(otherState, facing.getOpposite());
		}
		
		return otherState.isSideSolid(world, pos.offset(facing), facing);
	}
	
	@Override
	public boolean canConnectHorizontalPole(IBlockState state, EnumFacing fromFacing) {
		return true;
	}
}
