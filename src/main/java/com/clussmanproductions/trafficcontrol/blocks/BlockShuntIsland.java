package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.tileentity.ShuntIslandTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockShuntIsland extends BlockShuntBase {

	@Override
	protected String getName() {
		return "shunt_island";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new ShuntIslandTileEntity();
	}

}
