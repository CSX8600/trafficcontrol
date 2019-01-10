package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.util.ILoopableSoundTileEntity;
import com.clussmanproductions.trafficcontrol.util.LoopableTileEntitySound;

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
	private boolean soundPlaying = false;
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
		
		if (world.isRemote)
		{
			if (isRinging)
			{
				handlePlaySound();
			}
			else
			{
				soundPlaying = false;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void handlePlaySound()
	{
		if (!soundPlaying)
		{
			LoopableTileEntitySound sound = new LoopableTileEntitySound(getSoundEvent(), this, pos, 1F, 1);
			
			Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			
			soundPlaying = true;
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
		return !soundPlaying;
	}
	
	@Override
	public void onChunkUnload() {
		soundPlaying = false;
	}
	
	@SideOnly(Side.CLIENT)
	protected abstract SoundEvent getSoundEvent();
}
