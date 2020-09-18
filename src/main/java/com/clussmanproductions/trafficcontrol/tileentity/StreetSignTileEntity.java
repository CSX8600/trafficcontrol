package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StreetSignTileEntity extends SyncableTileEntity {
	public static final int MAX_STREET_SIGNS = 4;
	private StreetSign[] streetSigns = new StreetSign[MAX_STREET_SIGNS];
	
	public boolean addStreetSign(StreetSign sign)
	{
		for(int i = 0; i < streetSigns.length; i++)
		{
			if (streetSigns[i] == null)
			{
				streetSigns[i] = sign;
				markDirty();
				return true;
			}
		}
		
		return false;
	}
	
	public StreetSign getStreetSign(int index)
	{
		return streetSigns[index];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		for(int i = 0; i < MAX_STREET_SIGNS; i++)
		{
			if (compound.hasKey("street_sign" + i))
			{
				StreetSign sign = new StreetSign();
				sign.deserializeNBT(compound.getTag("street_sign" + i));
				
				streetSigns[i] = sign;
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for(int i = 0; i < streetSigns.length; i++)
		{
			StreetSign sign = streetSigns[i];
			
			if (sign != null)
			{
				compound.setTag("street_sign" + i, sign.serializeNBT());
			}
		}
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return newSate.getBlock() != ModBlocks.street_sign;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		for(int i = 0; i < MAX_STREET_SIGNS; i++)
		{
			StreetSign sign = streetSigns[i];
			if (sign == null)
			{
				continue;
			}
			
			compound.setTag("street_sign" + i, sign.serializeNBT());
		}
		
		return compound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		for(int i = 0; i < MAX_STREET_SIGNS; i++)
		{
			if (!tag.hasKey("street_sign" + i))
			{
				continue;
			}
			
			StreetSign sign = new StreetSign();
			sign.deserializeNBT(tag.getTag("street_sign" + i));
			streetSigns[i] = sign;
		}
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

	@Override
	public NBTTagCompound getClientToServerUpdateTag() {
		return getUpdateTag();
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound compound) {
		handleUpdateTag(compound);		
		markDirty();
		
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}
}
