package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TrafficLightTileEntity extends TileEntity {

	HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(3);
	HashMap<Integer, Boolean> activeBySlot = new HashMap<Integer, Boolean>(3);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		int[] bulbTypes = new int[3];
		bulbTypes[0] = bulbsBySlot.get(0).getIndex();
		bulbTypes[1] = bulbsBySlot.get(1).getIndex();
		bulbTypes[2] = bulbsBySlot.get(2).getIndex();
		
		compound.setIntArray("bulbTypes", bulbTypes);
		compound.setBoolean("active0", activeBySlot.get(0));
		compound.setBoolean("active1", activeBySlot.get(1));
		compound.setBoolean("active2", activeBySlot.get(2));
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}
	
	public void setBulbsBySlot(HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot)
	{
		bulbsBySlot = this.bulbsBySlot;
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
}
