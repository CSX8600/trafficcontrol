package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.statewatcher.IStateWatchable;
import com.clussmanproductions.trafficcontrol.statewatcher.StateWatcher;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightDoubleTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.StreetLightSingleTileEntity;
import com.clussmanproductions.trafficcontrol.util.MaterialCustomTransparency;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockStreetLightDouble extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockStreetLightDouble()
	{
		super(new MaterialCustomTransparency());
		setRegistryName("street_light_double");
		setUnlocalizedName(ModTrafficControl.MODID + ".street_light_double");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, POWERED);
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
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		boolean powered = world.isBlockPowered(pos);
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(POWERED, powered);
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
		return (state.getValue(POWERED)) ? 0 : 15;
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
			watchable.removeStateWatchers();
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1F;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof StreetLightDoubleTileEntity)
		{
			StreetLightDoubleTileEntity sld = (StreetLightDoubleTileEntity)te;
			boolean shouldRefresh = false;
			
			if (worldIn.isBlockPowered(pos))
			{
				sld.removeLightSources();
				sld.removeStateWatchers();
				
				shouldRefresh = !state.getValue(POWERED);
			}
			else
			{
				sld.addLightSources();
				sld.addStateWatchers();
				
				shouldRefresh = state.getValue(POWERED);
			}
			
			if (shouldRefresh)
			{
				worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
			}
		}
		
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}
}
