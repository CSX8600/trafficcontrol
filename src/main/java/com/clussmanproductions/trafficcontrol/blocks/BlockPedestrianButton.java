package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModSounds;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.PedestrianButtonTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPedestrianButton extends Block {

	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static PropertyBool ABOVE = PropertyBool.create("above");

	public BlockPedestrianButton() {
		super(Material.IRON);
		setRegistryName("pedestrian_button");
		setUnlocalizedName(ModTrafficControl.MODID + ".pedestrian_button");
		setHardness(2F);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION, ABOVE);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(ABOVE, !worldIn.isAirBlock(pos.up()));
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

	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		int rotation = CustomAngleCalculator.getRotationForYaw(placer.rotationYaw);
		return getDefaultState().withProperty(ROTATION, rotation);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new PedestrianButtonTileEntity();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		worldIn.playSound(null, pos, ModSounds.pedButton, SoundCategory.BLOCKS, 0.3F, 1F);
		
		if (worldIn.isRemote) {
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}

		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof PedestrianButtonTileEntity) {
			PedestrianButtonTileEntity pedTE = (PedestrianButtonTileEntity) te;
			for (BlockPos controller : pedTE.getPairedBoxes()) {
				TileEntity prelimCtrlrTE = worldIn.getTileEntity(controller);
				if (prelimCtrlrTE == null || !(prelimCtrlrTE instanceof TrafficLightControlBoxTileEntity)) {
					pedTE.removePairedBox(controller);
					continue;
				}

				TrafficLightControlBoxTileEntity ctrlr = (TrafficLightControlBoxTileEntity) prelimCtrlrTE;
				
				int rotation = state.getValue(ROTATION);
				if (!CustomAngleCalculator.isNorthSouth(rotation)) {
					ctrlr.getAutomator().setWestEastPedQueued(true);
				} else {
					ctrlr.getAutomator().setNorthSouthPedQueued(true);
				}
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote) {
			super.breakBlock(worldIn, pos, state);
			return;
		}

		PedestrianButtonTileEntity te = (PedestrianButtonTileEntity) worldIn.getTileEntity(pos);
		if (te != null) {
			int rotation = state.getValue(ROTATION);
			te.onBreak(worldIn, CustomAngleCalculator.isNorthSouth(rotation));
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (!(state.getBlock() instanceof BlockPedestrianButton))
		{
			return FULL_BLOCK_AABB;
		}
		
		int rotation = state.getValue(ROTATION);
		
		switch(rotation)
		{
			case 0:
				return new AxisAlignedBB(0.71875, 0, 0.4375, 0.28125, 0.625, 0.625);
			case 8:
				return new AxisAlignedBB(0.71875, 0, 0.375, 0.28125, 0.625, 0.5625);
			case 4:
				return new AxisAlignedBB(0.375, 0, 0.71875, 0.5625, 0.625, 0.28125);
			case 12:
				return new AxisAlignedBB(0.4375, 0, 0.71875, 0.625, 0.625, 0.28125);
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
}
