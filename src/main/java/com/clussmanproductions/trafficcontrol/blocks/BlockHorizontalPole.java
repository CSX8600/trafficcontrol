package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHorizontalPole extends Block implements IHorizontalPoleConnectable {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL); 
	public BlockHorizontalPole()
	{
		super(Material.IRON);
		setRegistryName("horizontal_pole");
		setUnlocalizedName(ModTrafficControl.MODID + ".horizontal_pole");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		setHardness(2f);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch(state.getValue(FACING))
		{
			case NORTH:
			case SOUTH:
				return new AxisAlignedBB(0.4375, 0.4375, 0, 0.5625, 0.5625, 1);
			case EAST:
			case WEST:
				return new AxisAlignedBB(0, 0.4375, 0.4375, 1, 0.5625, 0.5625);
		}
		
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1;
	}

	@Override
	public boolean canConnectHorizontalPole(IBlockState state, EnumFacing fromFacing) {
		EnumFacing myFacing = state.getValue(FACING);
		return myFacing.equals(fromFacing) || myFacing.getOpposite().equals(fromFacing);
	}
}
