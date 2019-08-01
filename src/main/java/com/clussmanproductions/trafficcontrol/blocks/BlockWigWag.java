package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.WigWagTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.RendererWigWag;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockWigWag extends Block implements ITileEntityProvider {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool ACTIVE = PropertyBool.create("active");
	public BlockWigWag()
	{
		super(Material.IRON);
		setRegistryName("wig_wag");
		setUnlocalizedName(ModTrafficControl.MODID + ".wig_wag");
		setHardness(2f);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(WigWagTileEntity.class, new RendererWigWag());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int modifier = 0;
		if (state.getValue(ACTIVE))
		{
			modifier = 4;
		}
		return state.getValue(FACING).getHorizontalIndex() + modifier;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean active = meta >= 4;
		meta = active ? meta - 4 : meta;
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(ACTIVE, active);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getBlock() != ModBlocks.wig_wag)
		{
			return FULL_BLOCK_AABB;
		}
		
		switch(state.getValue(FACING))
		{
			case WEST:
				return new AxisAlignedBB(0.375,0,0.375,0.625,1.25,1.3125);
			case EAST:
				return new AxisAlignedBB(0.375,0,0.5625,0.625,1.25,-0.375);
			case NORTH:
				return new AxisAlignedBB(-0.375,0,0.625,0.5625,1.25,0.375);
			case SOUTH:
				return new AxisAlignedBB(0.4375,0,0.625,1.375,1.25,0.375);
		}
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(ACTIVE) ? 15 : 0;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(ACTIVE, false);
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
}
