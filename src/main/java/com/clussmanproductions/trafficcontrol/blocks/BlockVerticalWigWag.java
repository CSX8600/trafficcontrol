package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.VerticalWigWagTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.WigWagTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.RendererWigWag;
import com.clussmanproductions.trafficcontrol.tileentity.render.VerticalWigWagRenderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
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

public class BlockVerticalWigWag extends Block implements ITileEntityProvider
{
	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockVerticalWigWag()
	{
		super(Material.IRON);
		setRegistryName("vertical_wig_wag");
		setUnlocalizedName(ModTrafficControl.MODID + ".vertical_wig_wag");
		setHardness(1f);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(VerticalWigWagTileEntity.class, new VerticalWigWagRenderer());
	}
	
	@Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) 
	{
        if (face == EnumFacing.UP)
        {
            return BlockFaceShape.UNDEFINED;
        }
        return super.getBlockFaceShape(worldIn, state, pos, face);
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
		VerticalWigWagTileEntity wigWagTE = null;
		if (worldIn instanceof ChunkCache)
		{
			ChunkCache chunkCache = (ChunkCache)worldIn;
			TileEntity te = chunkCache.getTileEntity(pos);
			if (!(te instanceof VerticalWigWagTileEntity))
			{
				return super.getActualState(state, worldIn, pos);
			}
			
			wigWagTE = (VerticalWigWagTileEntity)te;
		}
		else
		{
			World world = (World)worldIn;
			TileEntity te = world.getTileEntity(pos);
			if (!(te instanceof VerticalWigWagTileEntity))
			{
				return super.getActualState(state, worldIn, pos);
			}
			
			wigWagTE = (VerticalWigWagTileEntity)te;
		}
		
		return state.withProperty(ACTIVE, wigWagTE.isActive());
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != ModBlocks.vertical_wig_wag)
		{
			return FULL_BLOCK_AABB;
		}
		
		return new AxisAlignedBB(0.125, 0, 0.1875, 0.875, 0.625, 0.8125);
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
		return new VerticalWigWagTileEntity();
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
}
