package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy.GUI_IDs;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.CrossingLampsRenderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public abstract class BlockLampBase extends Block {
	public static final PropertyEnum<EnumState> STATE = PropertyEnum.create("state", EnumState.class);
	
	public BlockLampBase()
	{
		super(Material.IRON);
		setRegistryName(getLampRegistryName());
		setUnlocalizedName(ModTrafficControl.MODID + "." + getLampRegistryName());
		setHardness(2f);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public abstract String getLampRegistryName();

	@Override
	public abstract IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand);
	
	@Override
	public abstract IBlockState getStateFromMeta(int meta);
	
	@Override
	public abstract int getMetaFromState(IBlockState state);

	public abstract IProperty<?> getRotationalProperty();
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(CrossingLampsTileEntity.class, new CrossingLampsRenderer());
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new CrossingLampsTileEntity();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, getRotationalProperty(), STATE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		CrossingLampsTileEntity crossingLampsTE = null;
		TileEntity te = worldIn.getTileEntity(pos);
		if (te == null || !(te instanceof CrossingLampsTileEntity))
		{
			return state;
		}
		crossingLampsTE = (CrossingLampsTileEntity)te;
		
		EnumState flashState = EnumState.Off;
		if (crossingLampsTE != null)
		{
			flashState = crossingLampsTE.getState();
		}
		
		return state.withProperty(STATE, flashState);
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	public enum EnumState implements IStringSerializable
	{
		Off(0, "off"),
		Flash1(1, "flash1"),
		Flash2(2, "flash2");
		
		private int id;
		private String name;
		private EnumState(int id, String name)
		{
			this.id = id;
			this.name= name;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public int getID()
		{
			return id;
		}
		
		public static EnumState getStateByID(int id)
		{
			for(EnumState state : EnumState.values())
			{
				if (state.id == id)
				{
					return state;
				}
			}
			
			return null;
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		CrossingLampsTileEntity crossingLampsTE = null;
		TileEntity te = world.getTileEntity(pos);
		if (te == null || !(te instanceof CrossingLampsTileEntity))
		{
			return 0;
		}
		crossingLampsTE = (CrossingLampsTileEntity)te;
		
		EnumState flashState = EnumState.Off;
		if (crossingLampsTE != null)
		{
			flashState = crossingLampsTE.getState();
		}
		
		if (flashState == EnumState.Flash1 || flashState == EnumState.Flash2)
		{
			return 15;
		}
		
		return 0;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() == ModItems.crossing_relay_tuner)
		{
			return false;
		}
		
		playerIn.openGui(ModTrafficControl.instance, GUI_IDs.CROSSING_GATE_LAMPS, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
