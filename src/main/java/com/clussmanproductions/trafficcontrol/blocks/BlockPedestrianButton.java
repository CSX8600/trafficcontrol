package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.PedestrianButtonTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPedestrianButton extends Block {

	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockPedestrianButton() {
		super(Material.IRON);
		setRegistryName("pedestrian_button");
		setUnlocalizedName(ModTrafficControl.MODID + ".pedestrian_button");
		setHardness(2F);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
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
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
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
		if (worldIn.isRemote)
		{
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof PedestrianButtonTileEntity)
		{
			PedestrianButtonTileEntity pedTE = (PedestrianButtonTileEntity)te;
			for(BlockPos controller : pedTE.getPairedBoxes())
			{
				TileEntity prelimCtrlrTE = worldIn.getTileEntity(controller);
				if (prelimCtrlrTE == null || !(prelimCtrlrTE instanceof TrafficLightControlBoxTileEntity))
				{
					pedTE.removePairedBox(controller);
					continue;
				}
				
				EnumFacing myFacing = state.getValue(FACING);
				TrafficLightControlBoxTileEntity ctrlr = (TrafficLightControlBoxTileEntity)prelimCtrlrTE;
				if (myFacing == EnumFacing.NORTH || myFacing == EnumFacing.SOUTH)
				{
					ctrlr.getAutomator().setWestEastPedQueued(true);
				}
				else
				{
					ctrlr.getAutomator().setNorthSouthPedQueued(true);
				}
			}
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
		{
			super.breakBlock(worldIn, pos, state);
			return;
		}
		
		PedestrianButtonTileEntity te = (PedestrianButtonTileEntity)worldIn.getTileEntity(pos);
		if (te != null)
		{
			te.onBreak(worldIn, state.getValue(FACING));
		}
		
		super.breakBlock(worldIn, pos, state);
	}
}
