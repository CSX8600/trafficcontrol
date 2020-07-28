package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockTrafficRail extends Block {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool ISFURTHESTLEFT = PropertyBool.create("isfurthestleft");
	public static PropertyBool ISFURTHESTRIGHT = PropertyBool.create("isfurthestright");
	
	public BlockTrafficRail()
	{
		super(Material.IRON);
		setRegistryName("traffic_rail");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_rail");
		setHardness(2f);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ISFURTHESTLEFT, ISFURTHESTRIGHT);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean furthestLeft = true;
		boolean furthestRight = true;
		
		EnumFacing currentFacing = state.getValue(FACING);
		
		IBlockState stateToLeft = worldIn.getBlockState(pos.offset(currentFacing.rotateYCCW()));
		if (stateToLeft.getBlock() instanceof BlockTrafficRail)
		{
			furthestLeft = stateToLeft.getValue(FACING) != currentFacing;
		}
		
		IBlockState stateToRight = worldIn.getBlockState(pos.offset(currentFacing.rotateY()));
		if (stateToRight.getBlock() instanceof BlockTrafficRail)
		{
			furthestRight = stateToRight.getValue(FACING) != currentFacing;
		}
		
		return state.withProperty(ISFURTHESTLEFT, furthestLeft).withProperty(ISFURTHESTRIGHT, furthestRight);
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
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		double leftAmount = 0;
		double rightAmount = 0;
		
		IBlockState realState = state.getActualState(source, pos);
		
		if (!realState.getValue(ISFURTHESTLEFT))
		{
			leftAmount = 0.25;
		}
		
		if (!realState.getValue(ISFURTHESTRIGHT))
		{
			rightAmount = 0.25;
		}
		
		switch(state.getValue(FACING))
		{
			case NORTH:
				return new AxisAlignedBB(0.25 - leftAmount, 0, 0.75, 0.75 + rightAmount, 1.25, 0.40625);
			case SOUTH:
				return new AxisAlignedBB(0.25 - rightAmount, 0, 0.25, 0.75 + leftAmount, 1.25, 0.59375);
			case EAST:
				return new AxisAlignedBB(0.25, 0, 0.25 - leftAmount, 0.59375, 1.25, 0.75 + rightAmount);
			case WEST:
				return new AxisAlignedBB(0.40625, 0, 0.25 - rightAmount, 0.75, 1.25, 0.75 + leftAmount);
		}

		return FULL_BLOCK_AABB;
	}
}

