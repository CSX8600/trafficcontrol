package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorLeft;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorStraight;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TrafficLightControlBoxTileEntity extends SyncableTileEntity implements ITickable {
	private ArrayList<BlockPos> westEastLights = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> northSouthLights = new ArrayList<BlockPos>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private ArrayList<BlockPos> sensors = new ArrayList<>();
	private boolean hasSensors = false; // Client only property
	private boolean powered;
	private Automator automator = null;
	
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
		
		for(int i = 0; i < sensors.size(); i++)
		{
			BlockPos sensorPos = sensors.get(i);
			compound.setLong("sensor" + i, sensorPos.toLong());
		}
		
		getAutomator().writeNBT(compound);
		
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
		
		for(String key : compound
				.getKeySet()
				.stream()
				.filter((key) -> key.startsWith("sensor"))
				.collect(Collectors.toSet()))
		{
			BlockPos sensorPos = BlockPos.fromLong(compound.getLong(key));
			sensors.add(sensorPos);
		}
		
		getAutomator().readNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		
		writeManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		writeManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		writeManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		writeManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		compound.setBoolean("hasSensors", !sensors.isEmpty());
		
		getAutomator().setSyncData(compound);
		
		return compound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		readManualSettingDictionary(tag, manualNorthSouthActive, "manualNorthSouthActive");
		readManualSettingDictionary(tag, manualWestEastActive, "manualWestEastActive");
		readManualSettingDictionary(tag, manualNorthSouthInactive, "manualNorthSouthInactive");
		readManualSettingDictionary(tag, manualWestEastInactive, "manualWestEastInactive");
		
		hasSensors = tag.getBoolean("hasSensors");
		
		getAutomator().readSyncData(tag);
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
		if (!sensors.isEmpty())
		{
			return;
		}
		
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

	public boolean addOrRemoveSensor(BlockPos pos)
	{
		if (sensors.contains(pos))
		{
			sensors.remove(pos);
			markDirty();
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
			return false;
		}
		else
		{
			sensors.add(pos);
			markDirty();
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
			return true;
		}
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
		
		getAutomator().setSyncData(compound);
		
		return compound;
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound compound) {
		readManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		readManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		readManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		readManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		getAutomator().readSyncData(compound);

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

	public boolean getHasSensors()
	{
		return hasSensors;
	}
	
	@Override
	public void update() {
		if (world.isRemote)
		{
			return;
		}
		
		if (!sensors.isEmpty())
		{
			getAutomator().update();
		}
	}
	
	public Automator getAutomator()
	{
		if (automator == null)
		{
			automator = new Automator();
		}
		
		return automator;
	}
	
	public class Automator
	{
		private long nextUpdate;
		private boolean hasInitialized = false;
		
		private final ImmutableList<Class<?>> sensorClasses = ImmutableList
				.<Class<?>>builder()
				.add(BlockTrafficSensorLeft.class)
				.add(BlockTrafficSensorStraight.class)
				.build();
		
		private final String nbtPrefix = "automated_";
		
		private Stages lastStage = Stages.Red;
		private RightOfWays lastRightOfWay = RightOfWays.EastWest;
		private double greenMinimum = 10;
		private double yellowTime = 3;
		private double redTime = 2;
		private double arrowMinimum = 5;
		
		public double getGreenMinimum() {
			return greenMinimum;
		}

		public void setGreenMinimum(double greenMinimum) {
			this.greenMinimum = greenMinimum;
		}

		public double getYellowTime() {
			return yellowTime;
		}

		public void setYellowTime(double yellowTime) {
			this.yellowTime = yellowTime;
		}

		public double getRedTime() {
			return redTime;
		}

		public void setRedTime(double redTime) {
			this.redTime = redTime;
		}

		public double getArrowMinimum() {
			return arrowMinimum;
		}

		public void setArrowMinimum(double arrowMinimum) {
			this.arrowMinimum = arrowMinimum;
		}
				
		public void update()
		{
			if (!hasInitialized)
			{
				initialize();
			}
			
			if (MinecraftServer.getCurrentTimeMillis() < nextUpdate)
			{
				return;
			}
			
			if (lastStage == Stages.Red)
			{
				lastRightOfWay = lastRightOfWay.getNext();
			}
			
			SensorCheckResult sensorResults = checkSensors(lastRightOfWay);
			
			lastStage = updateLightsByStage(getNextLogicalStage(lastStage, lastRightOfWay, sensorResults));
			
			markDirty();
		}
		
		private void initialize()
		{
			for(TrafficLightTileEntity te : northSouthLights
					.stream()
					.map(bp ->
					{
						TileEntity teAtPos = world.getTileEntity(bp);
						if (teAtPos instanceof TrafficLightTileEntity)
						{
							return (TrafficLightTileEntity)teAtPos;
						}
						
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList()))
			{
				te.powerOff();
				te.setActive(EnumTrafficLightBulbTypes.Red, true, false);
				te.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
			};
			
			for(TrafficLightTileEntity te : westEastLights
					.stream()
					.map(bp ->
					{
						TileEntity teAtPos = world.getTileEntity(bp);
						if (teAtPos instanceof TrafficLightTileEntity)
						{
							return (TrafficLightTileEntity)teAtPos;
						}
						
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList()))
			{
				te.powerOff();
				te.setActive(EnumTrafficLightBulbTypes.Red, true, false);
				te.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
			};
			
			hasInitialized = true;
		}
		
		private Stages updateLightsByStage(Stages stage)
		{
			List<TrafficLightTileEntity> trafficLightsForRightOfWay;
			EnumFacing direction1;
			EnumFacing direction2;
			
			if (lastRightOfWay == RightOfWays.NorthSouth)
			{
				trafficLightsForRightOfWay = northSouthLights
						.stream()
						.map(p ->
						{
							TileEntity te = world.getTileEntity(p);
							if (te instanceof TrafficLightTileEntity)
							{
								return (TrafficLightTileEntity)te;
							}
							
							return null;
						})
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				
				direction1 = EnumFacing.NORTH;
				direction2 = EnumFacing.SOUTH;
			}
			else
			{
				trafficLightsForRightOfWay = westEastLights
						.stream()
						.map(p ->
						{
							TileEntity te = world.getTileEntity(p);
							if (te instanceof TrafficLightTileEntity)
							{
								return (TrafficLightTileEntity)te;
							}
							
							return null;
						})
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				
				direction1 = EnumFacing.EAST;
				direction2 = EnumFacing.WEST;
			}
			
			switch(stage)
			{
				case Red:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
					});
					
					return Stages.Red;
				case Direction1TurnArrow:					
					trafficLightsForRightOfWay
						.stream()
						.forEach(tl -> {
							IBlockState tlBs = world.getBlockState(tl.getPos());
							if (!tlBs.getValue(BlockTrafficLight.FACING).equals(direction1))
							{
								return;
							}
							
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
						});
					
					return Stages.Direction1TurnArrow;
				case Direction2TurnArrow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!tlBs.getValue(BlockTrafficLight.FACING).equals(direction2))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
					});
					return Stages.Direction2TurnArrow;
				case BothTurnArrow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
					});

					return Stages.BothTurnArrow;
				case Direction1TurnArrowYellow:
					trafficLightsForRightOfWay
						.stream()
						.forEach(tl -> {
							IBlockState tlBs = world.getBlockState(tl.getPos());
							if (!tlBs.getValue(BlockTrafficLight.FACING).equals(direction1))
							{
								return;
							}
							
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
						});
					
					return Stages.Direction1TurnArrowYellow;
				case Direction2TurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!tlBs.getValue(BlockTrafficLight.FACING).equals(direction2))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
					});
					return Stages.Direction2TurnArrowYellow;
				case BothTurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
					});
					
					return Stages.BothTurnArrowYellow;
				case Yellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Yellow, true, false);
					});
					return Stages.Yellow;
				case Green:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, true);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
					});
					return Stages.Green;
			}
			
			return stage;
		}
		
		public void readNBT(NBTTagCompound nbt)
		{
			lastStage = Stages.getById(nbt.getInteger(getNbtKey("lastStage")));
			lastRightOfWay = RightOfWays.getbyIndex(nbt.getInteger(getNbtKey("lastRightOfWay")));
			
			readSyncData(nbt); // This may need to be changed if we send/receive data not needed to be saved
		}
		
		public void writeNBT(NBTTagCompound nbt)
		{
			nbt.setInteger(getNbtKey("lastStage"), lastStage.id);
			nbt.setInteger(getNbtKey("lastRightOfWay"), lastRightOfWay.index);
			
			setSyncData(nbt); // This may need to be changed if we send/receive data not needed to be saved
		}
		
		public void readSyncData(NBTTagCompound nbt)
		{
			greenMinimum = nbt.getDouble(getNbtKey("greenMinimum"));
			yellowTime = nbt.getDouble(getNbtKey("yellowTime"));
			redTime = nbt.getDouble(getNbtKey("redTime"));
			arrowMinimum = nbt.getDouble(getNbtKey("arrowMinimum"));
		}
		
		public void setSyncData(NBTTagCompound nbt)
		{
			nbt.setDouble(getNbtKey("greenMinimum"), greenMinimum);
			nbt.setDouble(getNbtKey("yellowTime"), yellowTime);
			nbt.setDouble(getNbtKey("redTime"), redTime);
			nbt.setDouble(getNbtKey("arrowMinimum"), arrowMinimum);
		}
		
		private String getNbtKey(String key)
		{
			return nbtPrefix + key;
		}
		
		private class SensorCheckResult
		{
			public boolean Direction1Sensor;
			public boolean Direction2Sensor;
			public boolean Direction1SensorLeft;
			public boolean Direction2SensorLeft;
		}
		
		private SensorCheckResult checkSensors(RightOfWays rightOfWay)
		{
			EnumFacing direction1 = rightOfWay == RightOfWays.NorthSouth ? EnumFacing.NORTH : EnumFacing.EAST;
			EnumFacing direction2 = rightOfWay == RightOfWays.NorthSouth ? EnumFacing.SOUTH : EnumFacing.WEST;
			
			ArrayList<BlockPos> invalidSensors = new ArrayList<>();
			SensorCheckResult result = new SensorCheckResult();
			
			for(BlockPos sensePos : sensors)
			{
				IBlockState senseState = world.getBlockState(sensePos);
				
				if (!sensorClasses.contains(senseState.getBlock().getClass()))
				{
					invalidSensors.add(sensePos);
					continue;
				}
				
				EnumFacing currentFacing;
				boolean isStraight = true;
				if (senseState.getBlock() instanceof BlockTrafficSensorLeft)
				{
					currentFacing = senseState.getValue(BlockTrafficSensorLeft.FACING);
					isStraight = false;
				}
				else
				{
					currentFacing = senseState.getValue(BlockTrafficSensorStraight.FACING);
				}
				
				if (!currentFacing.equals(direction1) && !currentFacing.equals(direction2))
				{
					continue;
				}
				
				if ((isStraight && currentFacing.equals(direction1) && result.Direction1Sensor) ||
						(isStraight && currentFacing.equals(direction2) && result.Direction2Sensor) ||
						(!isStraight && currentFacing.equals(direction1) && result.Direction1SensorLeft) ||
						(!isStraight && currentFacing.equals(direction2) && result.Direction2SensorLeft))
				{
					continue;
				}
				
				boolean isTripped = world
						.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(sensePos).expand(1, Config.sensorScanHeight, 1))
						.stream()
						.anyMatch(e -> !(e instanceof EntityPlayerMP) && Arrays.stream(Config.sensorEntities).anyMatch(eName -> eName.equals(EntityList.getKey(e).toString())));
				
				if (isTripped)
				{
					setSensorCheckResults(isStraight, currentFacing.equals(direction1), result);
				}
			}
			
			for(BlockPos invalidSensor : invalidSensors)
			{
				sensors.remove(invalidSensor);
			}
			
			return result;
		}
		
		private void setSensorCheckResults(boolean isStraight, boolean isDirection1, SensorCheckResult results)
		{
			if (isStraight && isDirection1) { results.Direction1Sensor = true; }
			else if (isStraight && !isDirection1) { results.Direction2Sensor = true; }
			else if (!isStraight && isDirection1) { results.Direction1SensorLeft = true; }
			else { results.Direction2SensorLeft = true; }
		}
		
		private Stages getNextLogicalStage(Stages currentStage, RightOfWays currentRightOfWay, Automator.SensorCheckResult sensorResult)
		{
			switch(currentStage)
			{
				case Red:
					if (sensorResult.Direction1SensorLeft && sensorResult.Direction2SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.BothTurnArrow;
					}
					else if (sensorResult.Direction1SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.Direction1TurnArrow;
					}
					else if (sensorResult.Direction2SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.Direction2TurnArrow;
					}					
					
					setNextUpdate(greenMinimum);				
					return Stages.Green;
				case BothTurnArrow:
					setNextUpdate(yellowTime);
					return Stages.BothTurnArrowYellow;
				case BothTurnArrowYellow:
					setNextUpdate(greenMinimum);
					return Stages.Green;
				case Direction1TurnArrow:
					setNextUpdate(yellowTime);
					return Stages.Direction1TurnArrowYellow;
				case Direction1TurnArrowYellow:
					setNextUpdate(greenMinimum);
					return Stages.Green;
				case Direction2TurnArrow:
					setNextUpdate(yellowTime);
					return Stages.Direction2TurnArrowYellow;
				case Direction2TurnArrowYellow:
					setNextUpdate(greenMinimum);
					return Stages.Green;
				case Green:
					Automator.SensorCheckResult crossSensorCheck = checkSensors(currentRightOfWay.getNext());
					if (crossSensorCheck.Direction1Sensor || 
							crossSensorCheck.Direction1SensorLeft ||
							crossSensorCheck.Direction2Sensor ||
							crossSensorCheck.Direction2SensorLeft)
					{
						setNextUpdate(yellowTime);
						return Stages.Yellow;
					}

					setNextUpdate(yellowTime);
					return Stages.Green;
				case Yellow:
					setNextUpdate(redTime);
					return Stages.Red;
			}
			
			return null;
		}
	
		private void setNextUpdate(double secondsIntoFuture)
		{
			nextUpdate = MinecraftServer.getCurrentTimeMillis() + (long)(secondsIntoFuture * 1000);
		}
	}
	
	private enum RightOfWays
	{
		NorthSouth(0),
		EastWest(1);
		
		private int index;
		private RightOfWays(int index)
		{
			this.index = index;			
		}
		
		public static RightOfWays getbyIndex(int index)
		{
			for(RightOfWays rightOfWay : RightOfWays.values())
			{
				if (rightOfWay.index == index)
				{
					return rightOfWay;
				}
			}
			
			return null;
		}
		
		public RightOfWays getNext()
		{
			RightOfWays newRow = getbyIndex(index + 1);
			if (newRow == null)
			{
				newRow = getbyIndex(0);
			}
			
			return newRow;
		}
	}
	
	private enum Stages
	{
		Red(0, 0),
		Direction1TurnArrow(1, 1),
		Direction2TurnArrow(2, 1),
		BothTurnArrow(3, 1),
		Direction1TurnArrowYellow(4, 2),
		Direction2TurnArrowYellow(5, 2),
		BothTurnArrowYellow(6, 2),
		Green(7, 3),
		Yellow(8, 4);
		
		private int priority;
		private int id;
		private Stages(int id, int priority)
		{
			this.id = id;
			this.priority = priority;
		}
		
		public static Stages getById(int id)
		{
			for(Stages stage : Stages.values())
			{
				if (stage.id == id)
				{
					return stage;
				}
			}
			
			return null;
		}
	}
}
