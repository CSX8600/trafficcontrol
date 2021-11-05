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
import net.minecraft.item.Item;
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te == null || !(te instanceof StreetLightDoubleTileEntity))
		{
			return 0;
		}
		
		StreetLightDoubleTileEntity streetLightDoubleTileEntity = (StreetLightDoubleTileEntity)te;
		
		return streetLightDoubleTileEntity.isPowered() ? 0 : 15;
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
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof StreetLightDoubleTileEntity)
		{
			StreetLightDoubleTileEntity sld = (StreetLightDoubleTileEntity)te;
			sld.removeLightSources();
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof StreetLightDoubleTileEntity)
		{
			StreetLightDoubleTileEntity watchable = (StreetLightDoubleTileEntity)te;
		}
		
		super.breakBlock(worldIn, pos, state);
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
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof StreetLightDoubleTileEntity)
		{
			StreetLightDoubleTileEntity sls = (StreetLightDoubleTileEntity)te;
			sls.setPowered(worldIn.isBlockPowered(pos));
			if (sls.isPowered())
			{
				sls.removeLightSources();
			}
			else
			{
				sls.addLightSources();
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
		if (state.getBlock() == ModBlocks.street_light_double)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.east(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_double)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.south(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_double)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
		
		workingPos = workingPos.west(4);
		state = e.getWorld().getBlockState(workingPos);
		if (state.getBlock() == ModBlocks.street_light_double)
		{
			e.getWorld().setBlockState(e.getPos(), ModBlocks.light_source.getDefaultState());
			e.setCanceled(true);
		}
	}
}
