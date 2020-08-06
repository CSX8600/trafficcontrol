package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightSingleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class BlockStreetLightSingle extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockStreetLightSingle()
	{
		super(Material.ROCK);
		setRegistryName("street_light_single");
		setUnlocalizedName(ModTrafficControl.MODID + ".street_light_single");
		setHardness(2f);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	public int getMetaFromState(IBlockState state) {
		int modifier = (state.getValue(POWERED)) ? 0 : 4;
		
		return state.getValue(FACING).getHorizontalIndex() + modifier;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean powered = false;
		
		if (meta > 3)
		{
			meta -= 4;
			powered = true;
		}
		
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(POWERED, powered);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, POWERED);
	}
		
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return (state.getValue(POWERED)) ? 0 : 15;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		boolean powered = world.isBlockPowered(pos);
		
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(POWERED, powered);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new StreetLightSingleTileEntity();
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof StreetLightSingleTileEntity)
		{
			StreetLightSingleTileEntity sls = (StreetLightSingleTileEntity)te;
			sls.removeLightSources();
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof StreetLightSingleTileEntity)
		{
			StreetLightSingleTileEntity watchable = (StreetLightSingleTileEntity)te;
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1F;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isRemote)
		{
			super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
			return;
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof StreetLightSingleTileEntity)
		{
			StreetLightSingleTileEntity sls = (StreetLightSingleTileEntity)te;
			boolean shouldUpdate = false;
			if (worldIn.isBlockPowered(pos))
			{
				sls.removeLightSources();
				
				shouldUpdate = !state.getValue(POWERED);
			}
			else
			{
				sls.addLightSources();
				
				shouldUpdate = state.getValue(POWERED);
			}
			
			if (shouldUpdate)
			{
				worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
			}
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent e)
	{
		if (e.getWorld().isRemote)
		{
			return;
		}
		
		BlockPos workingPos = new BlockPos(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ());
		
		workingPos = workingPos.north(2).west(2);
		IBlockState state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_single && state.getValue(FACING) != EnumFacing.EAST && state.getValue(FACING) != EnumFacing.SOUTH)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.east(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_single && state.getValue(FACING) != EnumFacing.SOUTH && state.getValue(FACING) != EnumFacing.WEST)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.south(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_single && state.getValue(FACING) != EnumFacing.WEST && state.getValue(FACING) != EnumFacing.NORTH)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.west(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_single && state.getValue(FACING) != EnumFacing.NORTH && state.getValue(FACING) != EnumFacing.EAST)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
	}
}
