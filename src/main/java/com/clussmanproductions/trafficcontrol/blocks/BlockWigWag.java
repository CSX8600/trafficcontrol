package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.WigWagTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.RendererWigWag;
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
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWigWag extends Block implements ITileEntityProvider {
	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static PropertyBool ACTIVE = PropertyBool.create("active");
	public BlockWigWag()
	{
		super(Material.IRON);
		setRegistryName("wig_wag");
		setUnlocalizedName(ModTrafficControl.MODID + ".wig_wag");
		setHardness(1f);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(WigWagTileEntity.class, new RendererWigWag());
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
		return new BlockStateContainer(this, ROTATION, ACTIVE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		WigWagTileEntity wigWagTE = null;
		if (worldIn instanceof ChunkCache)
		{
			ChunkCache chunkCache = (ChunkCache)worldIn;
			TileEntity te = chunkCache.getTileEntity(pos);
			if (!(te instanceof WigWagTileEntity))
			{
				return super.getActualState(state, worldIn, pos);
			}
			
			wigWagTE = (WigWagTileEntity)te;
		}
		else
		{
			World world = (World)worldIn;
			TileEntity te = world.getTileEntity(pos);
			if (!(te instanceof WigWagTileEntity))
			{
				return super.getActualState(state, worldIn, pos);
			}
			
			wigWagTE = (WigWagTileEntity)te;
		}
		
		return state.withProperty(ACTIVE, wigWagTE.isActive());
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != ModBlocks.wig_wag)
		{
			return FULL_BLOCK_AABB;
		}
		
		int rotation = state.getValue(ROTATION);
		
		switch(rotation)
		{
			case 0:
				return new AxisAlignedBB(-0.375,0,0.625,0.5625,1,0.375);
			case 8:
				return new AxisAlignedBB(0.4375,0,0.625,1.375,1,0.375);
			case 4:
				return new AxisAlignedBB(0.375,0,0.5625,0.625,1,-0.375);
			case 12:
				return new AxisAlignedBB(0.375,0,0.375,0.625,1,1.3125);
			case 1:
			case 15:
			case 7:
			case 9:
			case 3:
			case 5:
			case 11:
			case 13:
				return new AxisAlignedBB(0.375, 0, 0.375, 0.75, 1, 0.75);
			case 2:
			case 6:
			case 10:
			case 14:
				return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 1, 0.8);
		}
		
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(ACTIVE) ? 15 : 0;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(ROTATION, CustomAngleCalculator.getRotationForYaw(placer.rotationYaw)).withProperty(ACTIVE, false);
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new WigWagTileEntity();
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
}
