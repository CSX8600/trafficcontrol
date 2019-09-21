package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TrafficLightControlBoxTileEntity extends TileEntity {
	private ArrayList<BlockPos> westEastLights = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> northSouthLights = new ArrayList<BlockPos>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private boolean powered;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for(int i = 0; i < westEastLights.size(); i++)
		{
			BlockPos pos = westEastLights.get(i);
			int[] blockPosArray = new int[] { pos.getX(), pos.getY(), pos.getZ() };
			compound.setIntArray("westEast" + i, blockPosArray);
		}
		
		for(int i = 0; i < northSouthLights.size(); i++)
		{
			BlockPos pos = northSouthLights.get(i);
			int[] blockPosArray = new int[] { pos.getX(), pos.getY(), pos.getZ() };
			compound.setIntArray("northSouth" + i, blockPosArray);
		}
		
		compound.setBoolean("powered", powered);
		
		return super.writeToNBT(compound);
	}
	
	private void writeManualSettingDictionary(NBTTagCompound compound, HashMap<EnumTrafficLightBulbTypes, Boolean> map, String prefix)
	{
		Set<EnumTrafficLightBulbTypes> keySet = map.keySet();
		Collection<Boolean> values = map.values();
		for(int i = 0; i < map.size(); i++)
		{
			
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		int counter = 0;
		while(compound.hasKey("westEast" + counter))
		{
			int[] blockPosArray = compound.getIntArray("westEast" + counter);
			
			BlockPos newBlockPos = new BlockPos(blockPosArray[0], blockPosArray[1], blockPosArray[2]);
			westEastLights.add(newBlockPos);
			counter++;
		}
		
		counter = 0;
		while(compound.hasKey("northSouth" + counter))
		{
			int[] blockPosArray = compound.getIntArray("northSouth" + counter);
			
			BlockPos newBlockPos = new BlockPos(blockPosArray[0], blockPosArray[1], blockPosArray[2]);
			northSouthLights.add(newBlockPos);
			counter++;
		}
		
		powered = compound.getBoolean("powered");
	}
	
	public void setPowered(boolean powered)
	{
		this.powered = powered;
		
		TrafficLightTileEntity te = (TrafficLightTileEntity)world.getTileEntity(northSouthLights.get(0));
		te.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, powered);
		
		markDirty();
	}
	
	public boolean addOrRemoveWestEastTrafficLight(BlockPos pos)
	{
		if (westEastLights.contains(pos))
		{
			westEastLights.remove(pos);
			return false;
		}
		
		westEastLights.add(pos);
		markDirty();
		return true;		
	}
	
	public boolean addOrRemoveNorthSouthTrafficLight(BlockPos pos)
	{
		if (northSouthLights.contains(pos))
		{
			northSouthLights.remove(pos);
			return false;
		}
		
		northSouthLights.add(pos);
		markDirty();
		return true;		
	}
}
