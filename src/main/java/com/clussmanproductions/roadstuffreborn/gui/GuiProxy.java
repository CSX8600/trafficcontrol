package com.clussmanproductions.roadstuffreborn.gui;

import com.clussmanproductions.roadstuffreborn.tileentity.SignTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof SignTileEntity)
		{
			SignTileEntity signTE = (SignTileEntity)te;
			return new SignGui(signTE, pos);
		}
		
		return null;
	}

}
