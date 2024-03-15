package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrossingLampsTileEntity extends SyncableTileEntity {
	private EnumState state = EnumState.Off;
	private int nwBulbRotation = 0;
	private int neBulbRotation = 0;
	private int swBulbRotation = 0;
	private int seBulbRotation = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		state = EnumState.getStateByID(compound.getInteger("state"));
		nwBulbRotation = compound.getInteger("nw_bulb_rotation");
		neBulbRotation = compound.getInteger("ne_bulb_rotation");
		swBulbRotation = compound.getInteger("sw_bulb_rotation");
		seBulbRotation = compound.getInteger("se_bulb_rotation");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("state", state.getID());
		compound.setInteger("nw_bulb_rotation", nwBulbRotation);
		compound.setInteger("ne_bulb_rotation", neBulbRotation);
		compound.setInteger("sw_bulb_rotation", swBulbRotation);
		compound.setInteger("se_bulb_rotation", seBulbRotation);
		return super.writeToNBT(compound);
	}

	public EnumState getState() {
		return state;
	}

	public void setState(EnumState state) {
		this.state = state;
		markDirty();
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
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		handleUpdateTag(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.checkLight(pos);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getClientToServerUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("neRot", neBulbRotation);
		tag.setInteger("nwRot", nwBulbRotation);
		tag.setInteger("swRot", swBulbRotation);
		tag.setInteger("seRot", seBulbRotation);
		return tag;
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound tag) {
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
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return !(newSate.getBlock() instanceof BlockLampBase);
	}
}
