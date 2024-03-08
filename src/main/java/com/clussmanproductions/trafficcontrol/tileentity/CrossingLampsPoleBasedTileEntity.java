package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.network.PacketCrossingLampPoleBasedSync;
import com.clussmanproductions.trafficcontrol.network.PacketHandler;

import net.minecraft.nbt.NBTTagCompound;

public class CrossingLampsPoleBasedTileEntity extends CrossingLampsTileEntity {

	private int nwBulbRotation = 0;
	private int neBulbRotation = 0;
	private int swBulbRotation = 0;
	private int seBulbRotation = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		nwBulbRotation = compound.getInteger("nw_bulb_rotation");
		neBulbRotation = compound.getInteger("ne_bulb_rotation");
		swBulbRotation = compound.getInteger("sw_bulb_rotation");
		seBulbRotation = compound.getInteger("se_bulb_rotation");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("nw_bulb_rotation", nwBulbRotation);
		compound.setInteger("ne_bulb_rotation", neBulbRotation);
		compound.setInteger("sw_bulb_rotation", swBulbRotation);
		compound.setInteger("se_bulb_rotation", seBulbRotation);
		return super.writeToNBT(compound);
	}

	public int getNwBulbRotation() {
		return nwBulbRotation;
	}

	public void setNwBulbRotation(int nwBulbRotation) {
		this.nwBulbRotation = nwBulbRotation;
		markDirty();
	}

	public int getNeBulbRotation() {
		return neBulbRotation;
	}

	public void setNeBulbRotation(int neBulbRotation) {
		this.neBulbRotation = neBulbRotation;
		markDirty();
	}

	public int getSwBulbRotation() {
		return swBulbRotation;
	}

	public void setSwBulbRotation(int swBulbRotation) {
		this.swBulbRotation = swBulbRotation;
		markDirty();
	}

	public int getSeBulbRotation() {
		return seBulbRotation;
	}

	public void setSeBulbRotation(int seBulbRotation) {
		this.seBulbRotation = seBulbRotation;
		markDirty();
	}
	
	public void syncClientToServer()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("neRot", neBulbRotation);
		tag.setInteger("nwRot", nwBulbRotation);
		tag.setInteger("swRot", swBulbRotation);
		tag.setInteger("seRot", seBulbRotation);
		
		PacketCrossingLampPoleBasedSync sync = new PacketCrossingLampPoleBasedSync();
		sync.pos = getPos();
		sync.syncData = tag;
		PacketHandler.INSTANCE.sendToServer(sync);
	}
	
	public void onSyncFromClient(NBTTagCompound tag)
	{
		setNeBulbRotation(tag.getInteger("neRot"));
		setNwBulbRotation(tag.getInteger("nwRot"));
		setSwBulbRotation(tag.getInteger("swRot"));
		setSeBulbRotation(tag.getInteger("seRot"));
		
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return Double.MAX_VALUE;
	}
}
