package com.clussmanproductions.roadstuffreborn.tileentity;

import com.clussmanproductions.roadstuffreborn.util.ILoopableSoundTileEntity;
import com.clussmanproductions.roadstuffreborn.util.LoopableTileEntitySound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BellBaseTileEntity extends TileEntity implements ILoopableSoundTileEntity {
	private boolean isRinging = false;
	@SideOnly(Side.CLIENT)
	private ISound bellSound;
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		isRinging = compound.getBoolean("isringing");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = super.writeToNBT(compound);
		nbt.setBoolean("isringing", isRinging);
		return nbt;
	}
	
	public void setIsRinging(boolean ringing)
	{
		this.isRinging = ringing;
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setBoolean("ringing", isRinging);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		isRinging = tag.getBoolean("ringing");
		
		if (world.isRemote && isRinging)
		{
			handlePlaySound();
		}
	}
	
	private void handlePlaySound()
	{
		if (bellSound == null)
		{
			bellSound = new LoopableTileEntitySound(getSoundEvent(), this, pos, 1F, 1);
		}
		
		SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
		if (handler != null & !handler.isSoundPlaying(bellSound))
		{
			handler.playSound(bellSound);
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
		handleUpdateTag(nbt);
	}

	@Override
	public boolean isDonePlayingSound() {
		return !isRinging;
	}
	
	protected abstract SoundEvent getSoundEvent();
}
