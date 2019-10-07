package com.clussmanproductions.trafficcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class Type3BarrierTileEntity extends TileEntity {
	private boolean renderSign;
	
	public boolean getRenderSign() {
		return renderSign;
	}

	public void setRenderSign(boolean renderSign) {
		this.renderSign = renderSign;
		
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setRenderSign(compound.getBoolean("renderSign"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("renderSign", getRenderSign());
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("renderSign", renderSign);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		setRenderSign(tag.getBoolean("renderSign"));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
}
