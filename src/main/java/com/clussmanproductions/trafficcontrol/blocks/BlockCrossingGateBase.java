package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCrossingGateBase extends Block {
	public BlockCrossingGateBase()
	{
		super(Material.IRON);
		setRegistryName("crossing_gate_base");
		setUnlocalizedName(ModTrafficControl.MODID + ".crossing_gate_base");
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		setLightOpacity(15);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
		return new AxisAlignedBB(0.375, 0, .375, 0.625, 1, 0.625);
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) 
	{
		return false;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1;
	}
}
