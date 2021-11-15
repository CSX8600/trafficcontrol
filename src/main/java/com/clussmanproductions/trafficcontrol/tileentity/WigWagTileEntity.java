package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WigWagTileEntity extends TileEntity implements ITickable {
	private int rotation = 0;
	private AnimationMode mode = AnimationMode.SwingPositive;
	private boolean active = false;
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		active = compound.getBoolean("active");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("active", active);
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if (newSate.getBlock() == ModBlocks.wig_wag)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void update() {
		if (!world.isRemote)
		{
			return;
		}
		
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() != ModBlocks.wig_wag)
		{
			return;
		}
		
		if (!isActive() && rotation != 0)
		{
			if (rotation < 0 && mode != AnimationMode.SwingPositive)
			{
				mode = AnimationMode.SwingPositive;
			}
			
			if (rotation > 0 && mode != AnimationMode.SwingNegative)
			{
				mode = AnimationMode.SwingNegative;
			}
		}
		
		if (!isActive() && rotation == 0)
		{
			return;
		}
		
		if (isActive())
		{
			if (rotation > 30)
			{
				mode = AnimationMode.SwingNegative;
			}
			
			if (rotation < -30)
			{
				mode = AnimationMode.SwingPositive;
			}
		}
		
		switch(mode)
		{
			case SwingNegative:
				rotation -= 4;
				break;
			case SwingPositive:
				rotation += 4;
				break;
		}
	}
	
	private enum AnimationMode
	{
		SwingNegative,
		SwingPositive
	}

	public int getRotation()
	{
		return rotation;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		markDirty();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}
}
