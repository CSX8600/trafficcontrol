package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockType3Barrier extends Block {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool ISFURTHESTLEFT = PropertyBool.create("isfurthestleft");
	public static PropertyBool ISFURTHESTRIGHT = PropertyBool.create("isfurthestright");
	public BlockType3Barrier()
	{
		super(Material.IRON);
		setRegistryName("type_3_barrier");
		setUnlocalizedName(ModTrafficControl.MODID + ".type_3_barrier");
		setHardness(2f);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(FACING).add(ISFURTHESTLEFT).add(ISFURTHESTRIGHT).build();
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
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean isFurthestLeft = false;
		boolean isFurthestRight = false;
		EnumFacing currentFacing = state.getValue(FACING);
		EnumFacing directionOfTravel = currentFacing.rotateY();
		IBlockState borderState = worldIn.getBlockState(pos.offset(directionOfTravel));
		if (borderState.getBlock() == ModBlocks.type_3_barrier)
		{
			isFurthestRight = borderState.getValue(FACING) != currentFacing;
		}
		
		directionOfTravel = currentFacing.rotateYCCW();
		borderState = worldIn.getBlockState(pos.offset(directionOfTravel));
		if (borderState.getBlock() == ModBlocks.type_3_barrier)
		{
			isFurthestLeft = borderState.getValue(FACING) != currentFacing;
		}
		
		IExtendedBlockState currentState = (IExtendedBlockState)state;
		return currentState.withProperty(ISFURTHESTLEFT, isFurthestLeft).withProperty(ISFURTHESTRIGHT, isFurthestRight);
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
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
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
}
