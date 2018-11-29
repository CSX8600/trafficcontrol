package com.clussmanproductions.roadstuffreborn.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class RelayTileEntity extends TileEntity implements ITickable {

	private boolean isMaster;
	private boolean hasMaster;
	private int masterX;
	private int masterY;
	private int masterZ;
	
	public RelayTileEntity()
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		isMaster = compound.getBoolean("ismaster");
		hasMaster = compound.getBoolean("hasmaster");
		masterX = compound.getInteger("masterx");
		masterY = compound.getInteger("mastery");
		masterZ = compound.getInteger("masterz");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = super.writeToNBT(compound);
		
		nbt.setBoolean("ismaster", isMaster);
		nbt.setBoolean("hasmaster", hasMaster);
		nbt.setInteger("masterx", masterX);
		nbt.setInteger("mastery", masterY);
		nbt.setInteger("masterz", masterZ);
		
		return nbt;
	}
	
	@Override
	public void update() {
		if (!isMaster && !hasMaster)
		{
			
		}
	}

}
