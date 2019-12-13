package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TrafficLightControlBoxTileEntity extends SyncableTileEntity {
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
		
		writeManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		writeManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		writeManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		writeManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		return super.writeToNBT(compound);
	}
	
	private void writeManualSettingDictionary(NBTTagCompound compound, HashMap<EnumTrafficLightBulbTypes, Boolean> map, String prefix)
	{
		ArrayList<EnumTrafficLightBulbTypes> keyList = new ArrayList<EnumTrafficLightBulbTypes>(map.keySet());
		ArrayList<Boolean> valueList = new ArrayList<Boolean>(map.values());
		
		for(int i = 0; i < map.size(); i++)
		{
			String keyKey = prefix + "-key-" + i;
			String valueKey = prefix + "-value-" + i;
			
			compound.setInteger(keyKey, keyList.get(i).getIndex());
			compound.setBoolean(valueKey, valueList.get(i));
		}
	}
	
	private void readManualSettingDictionary(NBTTagCompound compound, HashMap<EnumTrafficLightBulbTypes, Boolean> map, String prefix)
	{
		map.clear();
		int i = 0;
		while(compound.hasKey(prefix + "-key-" + i))
		{
			int bulbType = compound.getInteger(prefix + "-key-" + i);
			boolean flash = compound.getBoolean(prefix + "-value-" + i);
			
			map.put(EnumTrafficLightBulbTypes.get(bulbType), flash);
			
			i++;
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
		
		readManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		readManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		readManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		readManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		
		writeManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		writeManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		writeManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		writeManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		return compound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		readManualSettingDictionary(tag, manualNorthSouthActive, "manualNorthSouthActive");
		readManualSettingDictionary(tag, manualWestEastActive, "manualWestEastActive");
		readManualSettingDictionary(tag, manualNorthSouthInactive, "manualNorthSouthInactive");
		readManualSettingDictionary(tag, manualWestEastInactive, "manualWestEastInactive");
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
	
	public void setPowered(boolean powered)
	{
		this.powered = powered;
		
		// Power off all lamps
		for(BlockPos westEastLight : westEastLights)
		{
			TileEntity te = world.getTileEntity(westEastLight);
			if (te instanceof TrafficLightTileEntity)
			{
				TrafficLightTileEntity light = (TrafficLightTileEntity)te;
				light.powerOff();
			}
		}
		
		for(BlockPos northSouthLight : northSouthLights)
		{
			TileEntity te = world.getTileEntity(northSouthLight);
			if (te instanceof TrafficLightTileEntity)
			{
				TrafficLightTileEntity light = (TrafficLightTileEntity)te;
				light.powerOff();
			}
		}
		
		if (powered)
		{
			for(EnumTrafficLightBulbTypes bulbType : manualNorthSouthActive.keySet())
			{
				for(BlockPos northSouthLight : northSouthLights)
				{
					TileEntity te = world.getTileEntity(northSouthLight);
					if (te instanceof TrafficLightTileEntity)
					{
						TrafficLightTileEntity light = (TrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualNorthSouthActive.get(bulbType));
					}
				}
			}
			
			for(EnumTrafficLightBulbTypes bulbType : manualWestEastActive.keySet())
			{
				for(BlockPos westEastLight : westEastLights)
				{
					TileEntity te = world.getTileEntity(westEastLight);
					if (te instanceof TrafficLightTileEntity)
					{
						TrafficLightTileEntity light = (TrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualWestEastActive.get(bulbType));
					}
				}
			}
		}
		else
		{
			for(EnumTrafficLightBulbTypes bulbType : manualNorthSouthInactive.keySet())
			{
				for(BlockPos northSouthLight : northSouthLights)
				{
					TileEntity te = world.getTileEntity(northSouthLight);
					if (te instanceof TrafficLightTileEntity)
					{
						TrafficLightTileEntity light = (TrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualNorthSouthInactive.get(bulbType));
					}
				}
			}
			
			for(EnumTrafficLightBulbTypes bulbType : manualWestEastInactive.keySet())
			{
				for(BlockPos westEastLight : westEastLights)
				{
					TileEntity te = world.getTileEntity(westEastLight);
					if (te instanceof TrafficLightTileEntity)
					{
						TrafficLightTileEntity light = (TrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualWestEastInactive.get(bulbType));
					}
				}
			}
		}
		
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

	public void addRemoveNorthSouthActive(EnumTrafficLightBulbTypes type, boolean flash, boolean add)
	{
		if (add)
		{
			manualNorthSouthActive.put(type, flash);
		}
		else if (!add && flash)
		{
			manualNorthSouthActive.put(type, false);
		}
		else
		{
			manualNorthSouthActive.remove(type);
		}
	}
	public void addRemoveWestEastActive(EnumTrafficLightBulbTypes type, boolean flash, boolean add)
	{
		if (add)
		{
			manualWestEastActive.put(type, flash);
		}
		else if (!add && flash)
		{
			manualWestEastActive.put(type, false);
		}
		else
		{
			manualWestEastActive.remove(type);
		}
	}
	public void addRemoveNorthSouthInactive(EnumTrafficLightBulbTypes type, boolean flash, boolean add)
	{
		if (add)
		{
			manualNorthSouthInactive.put(type, flash);
		}
		else if (!add && flash)
		{
			manualNorthSouthInactive.put(type, false);
		}
		else
		{
			manualNorthSouthInactive.remove(type);
		}
	}
	public void addRemoveWestEastInactive(EnumTrafficLightBulbTypes type, boolean flash, boolean add)
	{
		if (add)
		{
			manualWestEastInactive.put(type, flash);
		}
		else if (!add && flash)
		{
			manualWestEastInactive.put(type, false);
		}
		else
		{
			manualWestEastInactive.remove(type);
		}
	}

	@Override
	public NBTTagCompound getClientToServerUpdateTag() {
		NBTTagCompound compound = new NBTTagCompound();
		writeManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		writeManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		writeManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		writeManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		return compound;
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound compound) {
		readManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		readManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		readManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		readManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");

		markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}

	public boolean hasSpecificNorthSouthManualOption(EnumTrafficLightBulbTypes bulbType, boolean flash, boolean forActive)
	{
		if (forActive)
		{
			boolean result = manualNorthSouthActive.containsKey(bulbType);
			if (flash)
			{
				result = result && manualNorthSouthActive.get(bulbType);
			}
			
			return result;
		}
		else
		{
			boolean result = manualNorthSouthInactive.containsKey(bulbType);
			if (flash)
			{
				result = result && manualNorthSouthInactive.get(bulbType);
			}
			
			return result;
		}
	}

	public boolean hasSpecificWestEastManualOption(EnumTrafficLightBulbTypes bulbType, boolean flash, boolean forActive)
	{
		if (forActive)
		{
			boolean result = manualWestEastActive.containsKey(bulbType);
			if (flash)
			{
				result = result && manualWestEastActive.get(bulbType);
			}
			
			return result;
		}
		else
		{
			boolean result = manualWestEastInactive.containsKey(bulbType);
			if (flash)
			{
				result = result && manualWestEastInactive.get(bulbType);
			}
			
			return result;
		}
	}
}
