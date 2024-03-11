package com.clussmanproductions.trafficcontrol.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface IHorizontalPoleConnectable {

	boolean canConnectHorizontalPole(IBlockState state, EnumFacing fromFacing);
}
