package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TrafficLightTileEntity extends TileEntity implements ITickable {

	HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(3);
	HashMap<Integer, Boolean> activeBySlot = new HashMap<Integer, Boolean>(3);
	HashMap<Integer, Boolean> flashBySlot = new HashMap<Integer, Boolean>(3);
	HashMap<Integer, Integer> flashTimeBySlot = new HashMap<Integer, Integer>(3);
	HashMap<Integer, Boolean> flashCurrent = new HashMap<Integer, Boolean>(3);
	
	public TrafficLightTileEntity() {
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		int[] bulbTypes = new int[3];
		EnumTrafficLightBulbTypes bulbTypeInSlot = getBulbTypeBySlot(0);
		bulbTypes[0] = bulbTypeInSlot != null ? bulbTypeInSlot.getIndex() : -1;
		bulbTypeInSlot = getBulbTypeBySlot(1);
		bulbTypes[1] = bulbTypeInSlot != null ? bulbTypeInSlot.getIndex() : -1;
		bulbTypeInSlot = getBulbTypeBySlot(2);
		bulbTypes[2] = bulbTypeInSlot != null ? bulbTypeInSlot.getIndex() : -1;
		
		compound.setIntArray("bulbTypes", bulbTypes);
		compound.setBoolean("active0", getActiveBySlot(0));
		compound.setBoolean("active1", getActiveBySlot(1));
		compound.setBoolean("active2", getActiveBySlot(2));
		compound.setBoolean("flash0", getFlashBySlot(0));
		compound.setBoolean("flash1", getFlashBySlot(1));
		compound.setBoolean("flash2", getFlashBySlot(2));
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(3);
		activeBySlot = new HashMap<Integer, Boolean>(3);
		
		int[] bulbTypes = compound.getIntArray("bulbTypes");
		bulbsBySlot.put(0, EnumTrafficLightBulbTypes.get(bulbTypes[0]));
		bulbsBySlot.put(1, EnumTrafficLightBulbTypes.get(bulbTypes[1]));
		bulbsBySlot.put(2, EnumTrafficLightBulbTypes.get(bulbTypes[2]));
		
		activeBySlot.put(0, compound.getBoolean("active0"));
		activeBySlot.put(1, compound.getBoolean("active1"));
		activeBySlot.put(2, compound.getBoolean("active2"));
		
		flashBySlot.put(0, compound.getBoolean("flash0"));
		flashBySlot.put(1, compound.getBoolean("flash1"));
		flashBySlot.put(2, compound.getBoolean("flash2"));
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		int[] bulbTypes = new int[3];
		
		bulbsBySlot.forEach((key, value) ->
		{
			bulbTypes[key] = value != null ? value.getIndex() : -1;
		});
		
		tag.setIntArray("bulbTypes", bulbTypes);
		tag.setBoolean("active0", getActiveBySlot(0));
		tag.setBoolean("active1", getActiveBySlot(1));
		tag.setBoolean("active2", getActiveBySlot(2));
		tag.setBoolean("flash0", getFlashBySlot(0));
		tag.setBoolean("flash1", getFlashBySlot(1));
		tag.setBoolean("flash2", getFlashBySlot(2));
		
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>();
		
		int[] bulbTypes = tag.getIntArray("bulbTypes");
		for(int i = 0; i < bulbTypes.length; i++)
		{
			bulbsBySlot.put(i, EnumTrafficLightBulbTypes.get(bulbTypes[i]));
		}
		
		activeBySlot.put(0, tag.getBoolean("active0"));
		activeBySlot.put(1, tag.getBoolean("active1"));
		activeBySlot.put(2, tag.getBoolean("active2"));
		
		flashBySlot.put(0, tag.getBoolean("flash0"));
		flashBySlot.put(1, tag.getBoolean("flash1"));
		flashBySlot.put(2, tag.getBoolean("flash2"));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public void setBulbsBySlot(HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot)
	{
		this.bulbsBySlot = bulbsBySlot;
		activeBySlot = new HashMap<Integer, Boolean>();
		activeBySlot.put(1, false);
		activeBySlot.put(2, false);
		activeBySlot.put(3, false);
		
		markDirty();
	}
	
	public boolean hasBulb(EnumTrafficLightBulbTypes bulbType)
	{
		return bulbsBySlot.containsValue(bulbType);
	}
	
	public void setActive(EnumTrafficLightBulbTypes bulbType, boolean active, boolean flash)
	{
		bulbsBySlot.forEach((slot, type) -> 
		{
			if (type == bulbType)
			{
				activeBySlot.put(slot, active);
				flashBySlot.put(slot, flash);
			}
		});
		
		markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}
	
	public void powerOff()
	{
		activeBySlot.put(0, false);
		activeBySlot.put(1, false);
		activeBySlot.put(2, false);
		flashBySlot.put(0, false);
		flashBySlot.put(1, false);
		flashBySlot.put(2, false);
		
		markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}
	
	public EnumTrafficLightBulbTypes getBulbTypeBySlot(int slot)
	{
		if (bulbsBySlot.containsKey(slot))
		{
			return bulbsBySlot.get(slot);
		}
		
		return null;
	}

	public boolean getActiveBySlot(int slot)
	{
		if (activeBySlot.containsKey(slot))
		{
			return activeBySlot.get(slot);
		}
		
		return false;
	}
	
	public boolean getFlashBySlot(int slot)
	{
		if (flashBySlot.containsKey(slot))
		{
			return flashBySlot.get(slot);
		}
		
		return false;
	}

	public boolean getFlashCurrentBySlot(int slot)
	{
		if (flashCurrent.containsKey(slot))
		{
			return flashCurrent.get(slot);
		}
		
		return true;
	}
	
	public boolean anyActive()
	{
		return (getActiveBySlot(0) && (!getFlashBySlot(0) || getFlashCurrentBySlot(0))) || 
				(getActiveBySlot(1) && (!getFlashBySlot(1) || getFlashCurrentBySlot(1))) || 
				(getActiveBySlot(2) && (!getFlashBySlot(2) || getFlashCurrentBySlot(2)));
	}
	
	public int getYRotation()
	{
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() != ModBlocks.traffic_light)
		{
			return 0;
		}
		
		switch(state.getValue(BlockTrafficLight.FACING))
		{
			case EAST:
				return 270;
			case NORTH:
				return 0;
			case SOUTH:
				return 180;
			case WEST:
				return 90;
			
		}
		
		return 0;
	}

	@Override
	public void update() {
		if (!world.isRemote)
		{
			return;
		}
		
		for (int i = 0; i < 3; i++)
		{
			if (getFlashBySlot(i))
			{
				if (!flashTimeBySlot.containsKey(i))
				{
					flashTimeBySlot.put(i, 0);
				}
				
				if (!flashCurrent.containsKey(i))
				{
					flashCurrent.put(i, false);
				}
				
				flashTimeBySlot.put(i, flashTimeBySlot.get(i) + 1);
				
				if (flashTimeBySlot.get(i) > 20)
				{
					flashCurrent.put(i, !flashCurrent.get(i));
					flashTimeBySlot.put(i, 0);
					world.checkLight(getPos());
				}
			}
		}
	}
}
