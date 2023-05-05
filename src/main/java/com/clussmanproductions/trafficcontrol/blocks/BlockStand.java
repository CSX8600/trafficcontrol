package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
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

public class BlockStand extends Block {
	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public BlockStand()
	{
		super(Material.IRON);
		setRegistryName("stand");
		setUnlocalizedName(ModTrafficControl.MODID + ".stand");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		setLightOpacity(1);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.375, 0, .375, 0.625, 1, 0.625);
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(ROTATION, CustomAngleCalculator.getRotationForYaw(placer.rotationYaw));
	}
}
