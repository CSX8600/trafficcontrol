package com.clussmanproductions.trafficcontrol.blocks;

import java.util.Random;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.RelayTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRelayBase extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockRelayBase()
	{
		super(Material.IRON);
		setRegistryName(registryName());
		setUnlocalizedName(ModTrafficControl.MODID + "." + registryName());
		setHardness(2);
	}
	
	protected abstract String registryName();
	
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(ModItems.crossing_relay_box, 1);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.crossing_relay_box;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		BlockPos workingPos = pos.offset(EnumFacing.SOUTH);
		
		while(true)
		{
			if (!(worldIn.getBlockState(workingPos).getBlock() instanceof BlockRelayBase))
			{
				workingPos = workingPos.offset(EnumFacing.NORTH);
				break;
			}
			
			workingPos = workingPos.offset(EnumFacing.SOUTH);
		}
		
		workingPos = workingPos.offset(EnumFacing.EAST);
		while(true)
		{
			if (!(worldIn.getBlockState(workingPos).getBlock() instanceof BlockRelayBase))
			{
				workingPos = workingPos.offset(EnumFacing.WEST);
				break;
			}
			
			workingPos = workingPos.offset(EnumFacing.EAST);
		}
		
		workingPos = workingPos.offset(EnumFacing.DOWN);
		while(true)
		{
			if (!(worldIn.getBlockState(workingPos).getBlock() instanceof BlockRelayBase))
			{
				workingPos = workingPos.offset(EnumFacing.UP);
				break;
			}
			
			workingPos = workingPos.offset(EnumFacing.DOWN);
		}
		
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.WEST);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.NORTH);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.EAST);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.UP);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.SOUTH);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.WEST);
		tryBreakAtPos(worldIn, pos, workingPos);
		
		workingPos = workingPos.offset(EnumFacing.NORTH);
		tryBreakAtPos(worldIn, pos, workingPos);
	}
	
	private void tryBreakAtPos(World worldIn, BlockPos harvestPos, BlockPos currentPos)
	{
		if (harvestPos.getX() == currentPos.getX() &&
				harvestPos.getY() == currentPos.getY() &&
				harvestPos.getZ() == currentPos.getZ())
		{
			return;
		}
		
		worldIn.setBlockState(currentPos, Blocks.AIR.getDefaultState());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new RelayTileEntity();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		
		if(worldIn.isRemote)
		{
			return;
		}
		
		if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockRelayBase)
		{
			return;
		}
		
		RelayTileEntity te = (RelayTileEntity)worldIn.getTileEntity(pos);
		te = te.getMaster(worldIn);
		
		if (te != null)
		{
			te.setPowered(worldIn.isBlockPowered(pos));
		}
	}
}
