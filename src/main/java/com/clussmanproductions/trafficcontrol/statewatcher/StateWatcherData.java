package com.clussmanproductions.trafficcontrol.statewatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import scala.Tuple2;

public class StateWatcherData extends WorldSavedData {

	private HashMap<BlockPos, ArrayList<IStateWatchable>> map = new HashMap<BlockPos, ArrayList<IStateWatchable>>();
	private HashMap<BlockPos, ArrayList<BlockPos>> preloadInformation = new HashMap<BlockPos, ArrayList<BlockPos>>();
	private boolean isLoaded = false;
 	public StateWatcherData()
	{
		super(DATA_NAME);
	}
	
	public StateWatcherData(String name) {
		super(DATA_NAME);
	}

	private static String DATA_NAME = ModTrafficControl.MODID + "_StateWatcherData";
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		for(String key : nbt.getKeySet())
		{
			String[] keyParts = key.split(",");
			
			if (keyParts.length == 3)
			{
				int x = Integer.parseInt(keyParts[0]);
				int y = Integer.parseInt(keyParts[1]);
				int z = Integer.parseInt(keyParts[2]);
				
				BlockPos pos = new BlockPos(x, y, z);
				
				int[] tePos = nbt.getIntArray(key);
				
				ArrayList<BlockPos> teBlockPos = new ArrayList<BlockPos>();
				for(int i = 0; i < tePos.length; i += 3)
				{
					int teX = tePos[i];
					int teY = tePos[i + 1];
					int teZ = tePos[i + 2];
					
					BlockPos thisPos = new BlockPos(teX, teY, teZ);
					teBlockPos.add(thisPos);
				}
				
				preloadInformation.put(pos, teBlockPos);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for(BlockPos key : map.keySet())
		{
			String saveKey = key.getX() + "," + key.getY() + "," + key.getZ();
			
			int[] teCoords = new int[map.get(key).toArray().length * 3];
			
			if (teCoords.length == 0)
			{
				continue;
			}
			
			teCoords[1] = -1;
			
			int lastIndex = 0;
			for(IStateWatchable watchable : map.get(key))
			{
				TileEntity te = (TileEntity)watchable;
				if (te != null)
				{
					BlockPos pos = te.getPos();
					teCoords[lastIndex] = pos.getX();
					teCoords[lastIndex + 1] = pos.getY();
					teCoords[lastIndex + 2] = pos.getZ();
					
					lastIndex += 3;
				}
			}
			
			if (teCoords[1] == -1)
			{
				continue;
			}
			
			compound.setIntArray(saveKey, teCoords);
		}
		
		return compound;
	}

	public static StateWatcherData get(World world)
	{
		MapStorage storage = world.getMapStorage();
		StateWatcherData watcherData = (StateWatcherData) storage.getOrLoadData(StateWatcherData.class, DATA_NAME);
		
		if (watcherData == null)
		{
			watcherData = new StateWatcherData();
			storage.setData(DATA_NAME, watcherData);
		}
		
		if (!watcherData.isLoaded)
		{
			watcherData.finishLoading(world);
		}
		
		return watcherData;
	}

	public ArrayList<IStateWatchable> getStateWatchersForBlockPos(BlockPos pos)
	{
		if (map.containsKey(pos))
		{
			return map.get(pos);
		}
		
		return new ArrayList<IStateWatchable>();
	}

	public void removeStateWatchers(ArrayList<Tuple2<BlockPos, IStateWatchable>> stateWatchablesToRemove)
	{
		ArrayList<BlockPos> keysToRemove = new ArrayList<BlockPos>();
		for(Tuple2<BlockPos, IStateWatchable> tuple : stateWatchablesToRemove)
		{
			if (map.containsKey(tuple._1))
			{
				ArrayList<IStateWatchable> watchables = map.get(tuple._1);
				if (watchables.contains(tuple._2))
				{
					watchables.remove(tuple._2);
					
					if (watchables.isEmpty())
					{
						keysToRemove.add(tuple._1);
					}
				}
			}
		}
		
		for(BlockPos keyToRemove : keysToRemove)
		{
			map.remove(keyToRemove);
		}
		
		markDirty();
	}

	public void addStateWatcher(BlockPos pos, IStateWatchable watchable)
	{
		if (!map.containsKey(pos))
		{
			map.put(pos, new ArrayList<IStateWatchable>());
		}
		
		if (!map.get(pos).contains(watchable))
		{
			map.get(pos).add(watchable);
			
			markDirty();
		}
	}
	
	private void finishLoading(World world)
	{
		for(BlockPos key : preloadInformation.keySet())
		{
			for(BlockPos tePos : preloadInformation.get(key))
			{
				TileEntity te = world.getTileEntity(tePos);
				IStateWatchable watchable = (IStateWatchable)te;
				
				if (watchable != null)
				{
					if (!map.containsKey(key))
					{
						map.put(key, new ArrayList<IStateWatchable>());
					}
					
					map.get(key).add(watchable);
				}
			}
		}
		
		isLoaded = true;
	}
}
