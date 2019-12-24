package com.clussmanproductions.trafficcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ConcreteBarrierTileEntity extends TileEntity {

	private EnumFacing facing = EnumFacing.NORTH;
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("facing", facing.getHorizontalIndex());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
		super.readFromNBT(compound);
	}
	
	public EnumFacing getFacing() {
		return facing;
	}
	
	public void setFacing(EnumFacing facing) {
		boolean shouldMarkDirty = !world.isRemote && facing != this.facing;
		
		this.facing = facing;
		
		if (shouldMarkDirty)
		{
			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setInteger("facing", facing.getHorizontalIndex());
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		setFacing(EnumFacing.getHorizontal(tag.getInteger("facing")));
		super.handleUpdateTag(tag);
	}
}
