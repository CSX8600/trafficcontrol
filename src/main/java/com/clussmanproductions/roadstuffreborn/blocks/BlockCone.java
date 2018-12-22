package com.clussmanproductions.roadstuffreborn.blocks;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCone extends Block {
	public BlockCone()
	{
		super(Material.ROCK);
		setRegistryName("cone");
		setUnlocalizedName(ModRoadStuffReborn.MODID + ".cone");
		setLightOpacity(1);
		setCreativeTab(ModRoadStuffReborn.CREATIVE_TAB);
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
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO Auto-generated method stub
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.3,0,0.3,0.7,1,0.7);
	}
}
