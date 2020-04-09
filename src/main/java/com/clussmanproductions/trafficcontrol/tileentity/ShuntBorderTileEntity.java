package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.function.Consumer;

import com.clussmanproductions.trafficcontrol.blocks.BlockShuntBorder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ShuntBorderTileEntity extends ShuntBaseTileEntity {

	@Override
	protected <T extends IBlockState> Consumer<T> getRelayAddOrRemoveShuntMethod(
			RelayTileEntity relayTileEntity) 
	{
		return blockState -> 
		{
			BlockPos originPos = getTrackOrigin();
			EnumFacing facing = blockState.getValue(BlockShuntBorder.FACING);
			
			relayTileEntity.addOrRemoveShuntBorder(originPos, facing);
		};
	}
}
