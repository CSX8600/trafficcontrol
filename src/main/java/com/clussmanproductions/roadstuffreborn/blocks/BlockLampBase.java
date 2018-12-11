package com.clussmanproductions.roadstuffreborn.blocks;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class BlockLampBase extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumState> STATE = PropertyEnum.create("state", EnumState.class);
	
	public BlockLampBase()
	{
		super(Material.IRON);
		setRegistryName(getLampRegistryName());
		setUnlocalizedName(ModRoadStuffReborn.MODID + "." + getLampRegistryName());
		setCreativeTab(ModRoadStuffReborn.CREATIVE_TAB);
	}
	
	protected abstract String getLampRegistryName();
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, STATE);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(STATE, EnumState.Off);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int modifier = 0;
		
		switch(state.getValue(STATE))
		{
			case Flash1:
				modifier = 4;
				break;
			case Flash2:
				modifier = 8;
				break;
			default:
				modifier = 0;
		}
		
		return state.getValue(FACING).getHorizontalIndex() + modifier;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int workingMeta = meta;
		EnumState state = EnumState.Off;
		if (workingMeta >= 4)
		{
			if (workingMeta >= 8)
			{
				state = EnumState.Flash2;
				workingMeta -= 8;
			}
			else
			{
				state = EnumState.Flash1;
				workingMeta -= 4;
			}
		}
		
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(workingMeta)).withProperty(STATE, state);
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
		EnumState enumState = state.getValue(STATE);
		
		if (enumState == EnumState.Flash1 || enumState == EnumState.Flash2)
		{
			return 15;
		}
		
		return 0;
	}
}
