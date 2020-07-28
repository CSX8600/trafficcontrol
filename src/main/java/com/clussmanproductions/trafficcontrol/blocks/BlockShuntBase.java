package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.ShuntBaseTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class BlockShuntBase extends Block implements ITileEntityProvider {
	protected abstract String getName();
	
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockShuntBase()
	{
		super(Material.ROCK);
		setRegistryName(getName());
		setUnlocalizedName(ModTrafficControl.MODID + "." + getName());
		setHardness(1f);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
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
	public boolean isNormalCube(IBlockState state) {
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
				return new AxisAlignedBB(0, 0, 0.40625, 0.25, 1, 0.65625);
			case SOUTH:
				return new AxisAlignedBB(0.75, 0, 0.34375, 1, 1, 0.59375);
			case WEST:
				return new AxisAlignedBB(0.40625, 0, 0.75, 0.65625, 1, 1);
			case EAST:
				return new AxisAlignedBB(0.34375, 0, 0, 0.59375, 1, 0.25);
			
		}
		
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote)
		{
			ShuntBaseTileEntity te = (ShuntBaseTileEntity)worldIn.getTileEntity(pos);
			te.onBreak(state);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		ShuntBaseTileEntity te = (ShuntBaseTileEntity)worldIn.getTileEntity(pos);
		if (!te.setOrigin(placer.getHorizontalFacing()))
		{
			placer.sendMessage(new TextComponentString("WARNING - no track found nearby"));
		}
		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}
