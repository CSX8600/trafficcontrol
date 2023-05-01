package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightDoubleTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.StreetLightDoubleRenderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class BlockStreetLightDouble extends Block implements ITileEntityProvider {
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	
	public BlockStreetLightDouble()
	{
		super(Material.ROCK);
		setRegistryName("street_light_double");
		setUnlocalizedName(ModTrafficControl.MODID + ".street_light_double");
		setHardness(2f);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(StreetLightDoubleTileEntity.class, new StreetLightDoubleRenderer());
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
		int rotation = CustomAngleCalculator.getRotationForYaw(placer.rotationYaw);
		return getDefaultState().withProperty(ROTATION, rotation);
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
		return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new StreetLightDoubleTileEntity();
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
		{
			super.onBlockAdded(worldIn, pos, state);
			return;
		}
		
		addLightSources(worldIn, pos);
		
		super.onBlockAdded(worldIn, pos, state);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (worldIn.isRemote)
		{
			super.onBlockHarvested(worldIn, pos, state, player);
			return;
		}
		
		removeLightSources(worldIn, pos);
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
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
		
		if (worldIn.isBlockPowered(pos))
		{
			removeLightSources(worldIn, pos);
		}
		else
		{
			addLightSources(worldIn, pos);
		}
		
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}
	
	public void addLightSources(World world, BlockPos pos)
	{		
		tryPlaceLightSource(world, pos.up());
		pos = pos.north(2).west(2);
		tryPlaceLightSource(world, pos);
		pos = pos.east(4);
		tryPlaceLightSource(world, pos);
		pos = pos.south(4);
		tryPlaceLightSource(world, pos);
		pos = pos.west(4);
		tryPlaceLightSource(world, pos);
	}
	
	private void tryPlaceLightSource(World world, BlockPos pos)
	{
		IBlockState proposedBlockState = world.getBlockState(pos);
		
		if (proposedBlockState.getBlock() != ModBlocks.light_source && proposedBlockState.getBlock() != Blocks.AIR)
		{
			pos = pos.up();
			proposedBlockState = world.getBlockState(pos);
			
			if (proposedBlockState.getBlock() != ModBlocks.light_source && proposedBlockState.getBlock() != Blocks.AIR)
			{
				proposedBlockState = null;
			}
		}
		
		if (proposedBlockState != null)
		{
			world.setBlockState(pos, ModBlocks.light_source.getDefaultState());
		}
	}
	

	
	private void removeLightSources(World world, BlockPos pos)
	{		
		tryRemoveLightSource(world, pos.up());
		pos = pos.north(2).west(2);
		tryRemoveLightSource(world, pos);
		pos = pos.east(4);
		tryRemoveLightSource(world, pos);
		pos = pos.south(4);
		tryRemoveLightSource(world, pos);
		pos = pos.west(4);
		tryRemoveLightSource(world, pos);
	}
	
	private void tryRemoveLightSource(World world, BlockPos pos)
	{
		IBlockState proposedBlockState = world.getBlockState(pos);
		
		if (proposedBlockState.getBlock() == ModBlocks.light_source)
		{
			world.setBlockToAir(pos);
		}
		
		proposedBlockState = world.getBlockState(pos.up());
		if (proposedBlockState.getBlock() == ModBlocks.light_source)
		{
			world.setBlockToAir(pos);
		}
	}
}
