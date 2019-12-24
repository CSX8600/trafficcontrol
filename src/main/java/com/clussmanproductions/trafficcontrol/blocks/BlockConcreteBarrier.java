package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.ConcreteBarrierTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockConcreteBarrier extends Block {
	public static PropertyInteger DYE = PropertyInteger.create("dye", 0, 15);
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockConcreteBarrier()
	{
		super(Material.ROCK);
		setHardness(1f);
		setRegistryName("concrete_barrier");
		setUnlocalizedName(ModTrafficControl.MODID + ".concrete_barrier");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		for(EnumDyeColor color : EnumDyeColor.values())
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), color.getMetadata(), new ModelResourceLocation(getRegistryName(), "dye=" + color.getMetadata() + ",facing=north"));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DYE, FACING);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for(EnumDyeColor color : EnumDyeColor.values())
		{
			items.add(new ItemStack(Item.getItemFromBlock(this), 1, color.getMetadata()));
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DYE);
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DYE, meta);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(DYE, placer.getHeldItem(hand).getMetadata());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		ConcreteBarrierTileEntity te = (ConcreteBarrierTileEntity)worldIn.getTileEntity(pos);
		te.setFacing(placer.getHorizontalFacing());
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ConcreteBarrierTileEntity te = (ConcreteBarrierTileEntity)worldIn.getTileEntity(pos);
		return state.withProperty(FACING, te.getFacing());
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new ConcreteBarrierTileEntity();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
}
