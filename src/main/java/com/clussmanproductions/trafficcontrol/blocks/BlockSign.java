package com.clussmanproductions.trafficcontrol.blocks;

import java.util.Arrays;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.util.UnlistedPropertyInteger;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockSign extends Block implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final UnlistedPropertyInteger TYPE = new UnlistedPropertyInteger("type");
	public static final UnlistedPropertyInteger SELECTION = new UnlistedPropertyInteger("selection");
	public static final PropertyBool VALIDHORIZONTALBAR = PropertyBool.create("validhorizontalbar");
	
	public BlockSign()
	{
		super(Material.IRON);
		setRegistryName("sign");
		setUnlocalizedName(ModTrafficControl.MODID + ".sign");
		setHardness(2f);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		IProperty<?>[] listedProperties = new IProperty[] { FACING, VALIDHORIZONTALBAR };
		IUnlistedProperty<?>[] unlistedProperties = new IUnlistedProperty[] { TYPE, SELECTION };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SignTileEntity();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean validHorizontalBar = false;
		
		EnumFacing currentFacing = state.getValue(FACING);
		if (currentFacing == EnumFacing.NORTH || currentFacing == EnumFacing.SOUTH)
		{
			validHorizontalBar = getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.west()), EnumFacing.NORTH, EnumFacing.SOUTH)
									|| getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.east()), EnumFacing.NORTH, EnumFacing.SOUTH);
		}
		else
		{
			validHorizontalBar = getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.north()), EnumFacing.WEST, EnumFacing.EAST)
									|| getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.south()), EnumFacing.WEST, EnumFacing.EAST);
		}
		
		return state.withProperty(VALIDHORIZONTALBAR, validHorizontalBar);
	}
	
	private boolean getValidStateForAttachableSubModels(IBlockState signState, IBlockState state, EnumFacing... validFacings)
	{
		if (state.getBlock() == ModBlocks.crossing_gate_pole)
		{
			return true;
		}
		
		if (state.getBlock() == ModBlocks.horizontal_pole)
		{
			EnumFacing facing = state.getValue(BlockHorizontalPole.FACING);
			
			return Arrays.stream(validFacings).noneMatch(vf -> vf.equals(facing));
		}
		
		if (state.getBlock() == ModBlocks.traffic_light)
		{
			return true;
		}
		
		if (state.getBlock() == ModBlocks.sign)
		{
			EnumFacing facing = state.getValue(FACING);
			
			return Arrays.stream(validFacings).anyMatch(vf -> vf.equals(facing));
		}
		
		return false;
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof SignTileEntity)
		{
			IExtendedBlockState extendedState = (IExtendedBlockState)state;
			SignTileEntity signTE = (SignTileEntity)te;
			extendedState = extendedState.withProperty(TYPE, signTE.getType()).withProperty(SELECTION, signTE.getVariant());
			return extendedState;
		}
		
		return super.getExtendedState(state, world, pos);
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
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote)
		{
			return true;
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof SignTileEntity))
		{
			return false;
		}
		
		playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.SIGN, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		double poleHeight = 1;
		TileEntity worldTE = source.getTileEntity(pos);
		
		if (!(worldTE instanceof SignTileEntity))
		{
			return FULL_BLOCK_AABB;
		}
		
		SignTileEntity te = (SignTileEntity)worldTE;
		if (te != null)
		{
			int type = te.getType();
			int variant = te.getVariant();
			
			if (type == 2 &&
					(variant >= 33 && variant <= 40))
			{
				poleHeight = 0.5;
			}
		}
		
		switch(state.getValue(FACING))
		{
			case NORTH:
				return new AxisAlignedBB(0, 0, 0.43125, 1, poleHeight, 0.5625);
			case SOUTH:
				return new AxisAlignedBB(0, 0, 0.4375, 1, poleHeight, 0.56875);
			case WEST:
				return new AxisAlignedBB(0.4375, 0, 0, 0.56875, poleHeight, 1);
			case EAST:
				return new AxisAlignedBB(0.43125, 0, 0, 0.5625, poleHeight, 1);
		}
		
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

}
