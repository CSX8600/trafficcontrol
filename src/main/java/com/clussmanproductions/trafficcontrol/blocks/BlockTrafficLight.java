package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTrafficLight extends Block {
	public BlockTrafficLight()
	{
		super(Material.IRON);
		setRegistryName("traffic_light");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light");
		setHardness(2F);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
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
}
