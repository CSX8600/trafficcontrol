package com.clussmanproductions.trafficcontrol.blocks;

import net.minecraft.util.BlockRenderLayer;

public class BlockOverheadLamps extends BlockLampBase {

	@Override
	protected String getLampRegistryName() {
		return "overhead_lamps";
	}	
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
