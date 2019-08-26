package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.tileentity.ShuntBorderTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockShuntBorder extends BlockShuntBase {

	@Override
	protected String getName() {
		return "shunt_border";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new ShuntBorderTileEntity();
	}

}
