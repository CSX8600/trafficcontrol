package com.clussmanproductions.trafficcontrol.gui;

import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;

import net.minecraft.client.gui.GuiScreen;

public class GuiType3Barrier extends GuiScreen {
	private Type3BarrierTileEntity tileEntity;
	
	public GuiType3Barrier(Type3BarrierTileEntity barrierTE)
	{
		tileEntity = barrierTE;
	}
	
	
}
