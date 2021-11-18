package com.clussmanproductions.trafficcontrol.blocks;

import java.util.Arrays;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.SignRenderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
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
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockSign extends Block implements ITileEntityProvider {

	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static final PropertyBool VALIDHORIZONTALBAR = PropertyBool.create("validhorizontalbar");
	public static final PropertyBool ISHALFHEIGHT = PropertyBool.create("ishalfheight");
	
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
		
		ClientRegistry.bindTileEntitySpecialRenderer(SignTileEntity.class, new SignRenderer());
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION, VALIDHORIZONTALBAR, ISHALFHEIGHT);
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
		boolean isHalfHeight = false;
		
		int rotation = state.getValue(ROTATION);
		boolean isCardinal = CustomAngleCalculator.isCardinal(rotation);
		
		if (isCardinal && CustomAngleCalculator.isNorthSouth(rotation))
		{
			validHorizontalBar = getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.west()), EnumFacing.NORTH, EnumFacing.SOUTH)
									|| getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.east()), EnumFacing.NORTH, EnumFacing.SOUTH);
		}
		else if (isCardinal)
		{
			validHorizontalBar = getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.north()), EnumFacing.WEST, EnumFacing.EAST)
									|| getValidStateForAttachableSubModels(state, worldIn.getBlockState(pos.south()), EnumFacing.WEST, EnumFacing.EAST);
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof SignTileEntity)
		{
			SignTileEntity signTE = (SignTileEntity)te;
			
			if (signTE.getType() == 2)
			{
				int variant = signTE.getVariant();
				
				if ((variant >= 33 && variant <= 40) || 
					(variant >= 106 && variant <= 107) ||
					(variant >= 110 && variant <= 111) ||
					(variant >= 120 && variant <= 124))
				{
					isHalfHeight = true;
				}
			}
		}
		
		return state.withProperty(VALIDHORIZONTALBAR, validHorizontalBar).withProperty(ISHALFHEIGHT, isHalfHeight);
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
		
		if (state.getBlock() instanceof BlockBaseTrafficLight)
		{
			return true;
		}
		
		if (state.getBlock() == ModBlocks.sign)
		{
			int otherRotation = state.getValue(ROTATION);
			boolean otherIsCardinal = CustomAngleCalculator.isCardinal(otherRotation);
			boolean isForNorthSouth = Arrays.stream(validFacings).anyMatch(facing -> facing == EnumFacing.NORTH);
			boolean thisSignNorthSouth = CustomAngleCalculator.isNorthSouth(signState.getValue(ROTATION));
			
			return otherIsCardinal && ((isForNorthSouth && thisSignNorthSouth) || (!isForNorthSouth && !thisSignNorthSouth));
		}
		
		return false;
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
		return getDefaultState().withProperty(ROTATION, CustomAngleCalculator.getRotationForYaw(placer.rotationYaw));
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
				(variant >= 33 && variant <= 40) || 
				(variant >= 106 && variant <= 107) ||
				(variant >= 110 && variant <= 111) ||
				(variant >= 120 && variant <= 124))
			{
				poleHeight = 0.5;
			}
		}
		
		int rotation = state.getValue(ROTATION);
		
//		if (CustomAngleCalculator.isNorth(rotation))
//		{
//			return new AxisAlignedBB(0, 0, 0.43125, 1, poleHeight, 0.5625);
//		}
//		else if (CustomAngleCalculator.isSouth(rotation))
//		{
//			return new AxisAlignedBB(0, 0, 0.4375, 1, poleHeight, 0.56875);
//		}
//		else if (CustomAngleCalculator.isWest(rotation))
//		{
//			return new AxisAlignedBB(0.4375, 0, 0, 0.56875, poleHeight, 1);
//		}
//		else if (CustomAngleCalculator.isEast(rotation))
//		{
//			return new AxisAlignedBB(0.43125, 0, 0, 0.5625, poleHeight, 1);
//		}
		
		switch(rotation)
		{
			case 0:
			case 8:
				return new AxisAlignedBB(0, 0, 0.43125, 1, poleHeight, 0.5625);
			case 4:
			case 12:
				return new AxisAlignedBB(0.4375, 0, 0, 0.56875, poleHeight, 1);
			case 1:
			case 15:
			case 7:
			case 9:
			case 3:
			case 5:
			case 11:
			case 13:
				return new AxisAlignedBB(0.375, 0, 0.375, 0.75, poleHeight, 0.75);
			case 2:
			case 6:
			case 10:
			case 14:
				return new AxisAlignedBB(0.2, 0, 0.2, 0.8, poleHeight, 0.8);
		}
		
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

}
