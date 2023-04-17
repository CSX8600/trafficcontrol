package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorLeft;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorRight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorStraight;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class TrafficLightControlBoxTileEntity extends SyncableTileEntity implements ITickable {
	private ArrayList<BlockPos> westEastLights = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> northSouthLights = new ArrayList<BlockPos>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastActive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualNorthSouthInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private HashMap<EnumTrafficLightBulbTypes, Boolean> manualWestEastInactive = new HashMap<EnumTrafficLightBulbTypes, Boolean>();
	private ArrayList<BlockPos> sensors = new ArrayList<>();
	private ArrayList<BlockPos> northSouthPedButtons = new ArrayList<>();
	private ArrayList<BlockPos> westEastPedButtons = new ArrayList<>();
	private boolean isAutoMode = false; // Client only property
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
		
		NBTTagList northSouthPedButtonsList = new NBTTagList();
		for(BlockPos pos : northSouthPedButtons)
		{
			northSouthPedButtonsList.appendTag(new NBTTagLong(pos.toLong()));
		}
		
		NBTTagList westEastPedButtonsList = new NBTTagList();
		for(BlockPos pos : westEastPedButtons)
		{
			westEastPedButtonsList.appendTag(new NBTTagLong(pos.toLong()));
		}
		
		compound.setTag("northSouthPedButtons", northSouthPedButtonsList);
		compound.setTag("westEastPedButtons", westEastPedButtonsList);
		
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
		
		NBTTagList northSouthPedButtonList = compound.getTagList("northSouthPedButtons", NBT.TAG_LONG);
		NBTTagList westEastPedButtonList = compound.getTagList("westEastPedButtons", NBT.TAG_LONG);
		
		northSouthPedButtons = new ArrayList<>();
		westEastPedButtons = new ArrayList<>();
		
		for(NBTBase baseLong : northSouthPedButtonList)
		{
			NBTTagLong posLong = (NBTTagLong)baseLong;
			northSouthPedButtons.add(BlockPos.fromLong(posLong.getLong()));
		}
		
		for(NBTBase baseLong : westEastPedButtonList)
		{
			NBTTagLong posLong = (NBTTagLong)baseLong;
			westEastPedButtons.add(BlockPos.fromLong(posLong.getLong()));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		
		writeManualSettingDictionary(compound, manualNorthSouthActive, "manualNorthSouthActive");
		writeManualSettingDictionary(compound, manualWestEastActive, "manualWestEastActive");
		writeManualSettingDictionary(compound, manualNorthSouthInactive, "manualNorthSouthInactive");
		writeManualSettingDictionary(compound, manualWestEastInactive, "manualWestEastInactive");
		
		compound.setBoolean("isAutoMode", !sensors.isEmpty() || !northSouthPedButtons.isEmpty() || !westEastPedButtons.isEmpty());
		
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
		
		isAutoMode = tag.getBoolean("isAutoMode");
		
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
			if (te instanceof BaseTrafficLightTileEntity)
			{
				BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
				light.powerOff();
				light.setActive(EnumTrafficLightBulbTypes.DontCross, false, false);
			}
		}
		
		for(BlockPos northSouthLight : northSouthLights)
		{
			TileEntity te = world.getTileEntity(northSouthLight);
			if (te instanceof BaseTrafficLightTileEntity)
			{
				BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
				light.powerOff();
				light.setActive(EnumTrafficLightBulbTypes.DontCross, false, false);
			}
		}
		
		if (powered)
		{
			for(EnumTrafficLightBulbTypes bulbType : manualNorthSouthActive.keySet())
			{
				for(BlockPos northSouthLight : northSouthLights)
				{
					TileEntity te = world.getTileEntity(northSouthLight);
					if (te instanceof BaseTrafficLightTileEntity)
					{
						BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualNorthSouthActive.get(bulbType));
					}
				}
			}
			
			for(EnumTrafficLightBulbTypes bulbType : manualWestEastActive.keySet())
			{
				for(BlockPos westEastLight : westEastLights)
				{
					TileEntity te = world.getTileEntity(westEastLight);
					if (te instanceof BaseTrafficLightTileEntity)
					{
						BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
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
					if (te instanceof BaseTrafficLightTileEntity)
					{
						BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
						light.setActive(bulbType, true, manualNorthSouthInactive.get(bulbType));
					}
				}
			}
			
			for(EnumTrafficLightBulbTypes bulbType : manualWestEastInactive.keySet())
			{
				for(BlockPos westEastLight : westEastLights)
				{
					TileEntity te = world.getTileEntity(westEastLight);
					if (te instanceof BaseTrafficLightTileEntity)
					{
						BaseTrafficLightTileEntity light = (BaseTrafficLightTileEntity)te;
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
	
	public boolean addOrRemoveNorthSouthPedButton(BlockPos pos)
	{
		if (northSouthPedButtons.contains(pos))
		{
			northSouthPedButtons.remove(pos);
			markDirty();
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
			return false;
		}
		else
		{
			northSouthPedButtons.add(pos);
			markDirty();
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
			return true;
		}
	}
	
	public boolean addOrRemoveWestEastPedButton(BlockPos pos)
	{
		if (westEastPedButtons.contains(pos))
		{
			westEastPedButtons.remove(pos);
			markDirty();
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
			return false;
		}
		else
		{
			westEastPedButtons.add(pos);
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

	public boolean isAutoMode()
	{
		return isAutoMode;
	}
	
	@Override
	public void update() {
		if (world.isRemote)
		{
			return;
		}
		
		if (!sensors.isEmpty() || !northSouthPedButtons.isEmpty() || !westEastPedButtons.isEmpty())
		{
			getAutomator().update();
		}
	}
	
	public void onBreak(World world)
	{
		for(BlockPos pos : northSouthPedButtons)
		{
			TileEntity prelimPed = world.getTileEntity(pos);
			if (prelimPed == null || !(prelimPed instanceof PedestrianButtonTileEntity))
			{
				continue;
			}
			
			((PedestrianButtonTileEntity)prelimPed).removePairedBox(getPos());
		}
		
		for(BlockPos pos : westEastPedButtons)
		{
			TileEntity prelimPed = world.getTileEntity(pos);
			if (prelimPed == null || !(prelimPed instanceof PedestrianButtonTileEntity))
			{
				continue;
			}
			
			((PedestrianButtonTileEntity)prelimPed).removePairedBox(getPos());
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
				.add(BlockTrafficSensorRight.class)
				.build();
		
		private final String nbtPrefix = "automated_";
		
		private Stages lastStage = Stages.Red;
		private RightOfWays lastRightOfWay = RightOfWays.EastWest;
		
		private double greenMinimum = 10;
		private double yellowTime = 3;
		private double redTime = 2;
		private double arrowMinimum = 5;
		private double crossTime = 5;
		private double crossWarningTime = 7;
		private double rightArrowTime = 5;
		private boolean northSouthPedQueued;
		private boolean westEastPedQueued;
		
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
				
		public double getCrossTime() {
			return crossTime;
		}

		public void setCrossTime(double crossTime) {
			this.crossTime = crossTime;
		}

		public double getCrossWarningTime() {
			return crossWarningTime;
		}

		public void setCrossWarningTime(double crossWarningTime) {
			this.crossWarningTime = crossWarningTime;
		}

		public double getRightArrowTime() {
			return rightArrowTime;
		}

		public void setRightArrowTime(double rightArrowTime) {
			this.rightArrowTime = rightArrowTime;
		}

		public boolean isNorthSouthPedQueued() {
			return northSouthPedQueued;
		}

		public void setNorthSouthPedQueued(boolean northSouthPedQueued) {
			this.northSouthPedQueued = northSouthPedQueued;
		}

		public boolean isWestEastPedQueued() {
			return westEastPedQueued;
		}

		public void setWestEastPedQueued(boolean westEastPedQueued) {
			this.westEastPedQueued = westEastPedQueued;
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
			for(BaseTrafficLightTileEntity te : northSouthLights
					.stream()
					.map(bp ->
					{
						TileEntity teAtPos = world.getTileEntity(bp);
						if (teAtPos instanceof BaseTrafficLightTileEntity)
						{
							return (BaseTrafficLightTileEntity)teAtPos;
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
			
			for(BaseTrafficLightTileEntity te : westEastLights
					.stream()
					.map(bp ->
					{
						TileEntity teAtPos = world.getTileEntity(bp);
						if (teAtPos instanceof BaseTrafficLightTileEntity)
						{
							return (BaseTrafficLightTileEntity)teAtPos;
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
			List<BlockPos> trafficLightPosForRightOfWay;
			List<BlockPos> trafficLightPosOpposingRightOfWay;
			List<BaseTrafficLightTileEntity> trafficLightsForRightOfWay;
			List<BaseTrafficLightTileEntity> trafficLightsOpposingRightOfWay;
			EnumFacing direction1;
			EnumFacing direction2;
			
			if (lastRightOfWay == RightOfWays.NorthSouth)
			{
				trafficLightPosForRightOfWay = northSouthLights;
				trafficLightPosOpposingRightOfWay = westEastLights;
				
				direction1 = EnumFacing.NORTH;
				direction2 = EnumFacing.SOUTH;
			}
			else
			{
				trafficLightPosForRightOfWay = westEastLights;
				trafficLightPosOpposingRightOfWay = northSouthLights;
				
				direction1 = EnumFacing.EAST;
				direction2 = EnumFacing.WEST;
			}
			
			trafficLightsForRightOfWay = trafficLightPosForRightOfWay
					.stream()
					.map(p ->
					{
						TileEntity te = world.getTileEntity(p);
						if (te instanceof BaseTrafficLightTileEntity)
						{
							return (BaseTrafficLightTileEntity)te;
						}
						
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			
			trafficLightsOpposingRightOfWay = trafficLightPosOpposingRightOfWay
					.stream()
					.map(p ->
					{
						TileEntity te = world.getTileEntity(p);
						if (te instanceof BaseTrafficLightTileEntity)
						{
							return (BaseTrafficLightTileEntity)te;
						}
						
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			
			EnumFacing direction1cw = direction1.rotateY();
			EnumFacing direction2cw = direction2.rotateY();
			switch(stage)
			{
				case Red:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction1RightTurnArrow:
				case Direction1LeftTurnArrow:					
					trafficLightsForRightOfWay
						.stream()
						.forEach(tl -> {
							IBlockState tlBs = world.getBlockState(tl.getPos());
							if (CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1.getOpposite()))
							{
								tl.powerOff();
								tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
								tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
								
								return;
							}
							
							if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1))
							{
								return;
							}
							
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.GreenArrowUTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.TunnelGreen, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.SGreen, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
						});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1cw))
						{
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction2RightTurnArrow:
				case Direction2LeftTurnArrow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2.getOpposite()))
						{
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
							
							return;
						}
						
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());						
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2cw))
						{
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
							
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case BothTurnArrow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction1LeftTurnArrowYellow:
					trafficLightsForRightOfWay
						.stream()
						.forEach(tl -> {
							IBlockState tlBs = world.getBlockState(tl.getPos());
							if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1))
							{
								return;
							}
							
							tl.powerOff();
							tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.TunnelGreen, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.SGreen, true, false);
							tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1cw))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction2LeftTurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2cw))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case BothTurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction1RightTurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Yellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SYellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
					});
				
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction1cw))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Direction2RightTurnArrowYellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Yellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SYellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
					});
				
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						IBlockState tlBs = world.getBlockState(tl.getPos());
						if (!CustomAngleCalculator.isRotationFacing(tlBs.getValue(BlockBaseTrafficLight.ROTATION), direction2cw))
						{
							return;
						}
						
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Yellow:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.Yellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SYellow, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
				case Green:
				case GreenCross:
				case GreenDontCrossWarning:
					trafficLightsForRightOfWay
					.stream()
					.forEach(tl ->
					{
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowLeft, true, true);
						tl.setActive(EnumTrafficLightBulbTypes.YellowArrowUTurn, true, true);
						tl.setActive(EnumTrafficLightBulbTypes.Green, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SGreen, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						
						if (stage == Stages.GreenCross)
						{
							tl.setActive(EnumTrafficLightBulbTypes.DontCross, false, false);
							tl.setActive(EnumTrafficLightBulbTypes.Cross, true, false);
						}
						else if (stage == Stages.GreenDontCrossWarning)
						{
							tl.setActive(EnumTrafficLightBulbTypes.DontCross, true, true);
						}
					});
					
					trafficLightsOpposingRightOfWay
					.stream()
					.forEach(tl -> {
						tl.powerOff();
						tl.setActive(EnumTrafficLightBulbTypes.Red, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.TunnelRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.SRed, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowRight, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoRightTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
						tl.setActive(EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
					});
					break;
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
			crossTime = nbt.getDouble(getNbtKey("crossTime"));
			crossWarningTime = nbt.getDouble(getNbtKey("crossWarningTime"));
			rightArrowTime = nbt.getDouble(getNbtKey("rightArrowMinimum"));
		}
		
		public void setSyncData(NBTTagCompound nbt)
		{
			nbt.setDouble(getNbtKey("greenMinimum"), greenMinimum);
			nbt.setDouble(getNbtKey("yellowTime"), yellowTime);
			nbt.setDouble(getNbtKey("redTime"), redTime);
			nbt.setDouble(getNbtKey("arrowMinimum"), arrowMinimum);
			nbt.setDouble(getNbtKey("crossTime"), crossTime);
			nbt.setDouble(getNbtKey("crossWarningTime"), crossWarningTime);
			nbt.setDouble(getNbtKey("rightArrowMinimum"), rightArrowTime);
			
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
			public boolean Direction1SensorRight;
			public boolean Direction2SensorRight;
		}
		
		private SensorCheckResult checkSensors(RightOfWays rightOfWay)
		{
			EnumFacing direction1 = rightOfWay == RightOfWays.NorthSouth ? EnumFacing.NORTH : EnumFacing.EAST;
			EnumFacing direction2 = rightOfWay == RightOfWays.NorthSouth ? EnumFacing.SOUTH : EnumFacing.WEST;
			
			ArrayList<BlockPos> invalidSensors = new ArrayList<>();
			SensorCheckResult result = new SensorCheckResult();
			
			boolean pedTripped = direction1 == EnumFacing.NORTH ? isNorthSouthPedQueued() : isWestEastPedQueued();
			result.Direction1Sensor = pedTripped;
			result.Direction2Sensor = pedTripped;
			
			for(BlockPos sensePos : sensors)
			{
				IBlockState senseState = world.getBlockState(sensePos);
				
				if (!sensorClasses.contains(senseState.getBlock().getClass()))
				{
					invalidSensors.add(sensePos);
					continue;
				}
				
				EnumFacing currentFacing = null;
				boolean isStraight = false;
				boolean isLeft = false;
				boolean isRight = false;
				if (senseState.getBlock() instanceof BlockTrafficSensorLeft)
				{
					currentFacing = senseState.getValue(BlockTrafficSensorLeft.FACING);
					isLeft = true;
				}
				else if (senseState.getBlock() instanceof BlockTrafficSensorStraight)
				{
					currentFacing = senseState.getValue(BlockTrafficSensorStraight.FACING);
					isStraight = true;
				}
				else if (senseState.getBlock() instanceof BlockTrafficSensorRight)
				{
					currentFacing = senseState.getValue(BlockTrafficSensorRight.FACING);
					isRight = true;
				}
				
				if (!currentFacing.equals(direction1) && !currentFacing.equals(direction2))
				{
					continue;
				}
				
				if ((isStraight && currentFacing.equals(direction1) && result.Direction1Sensor) ||
						(isStraight && currentFacing.equals(direction2) && result.Direction2Sensor) ||
						(isLeft && currentFacing.equals(direction1) && result.Direction1SensorLeft) ||
						(isLeft && currentFacing.equals(direction2) && result.Direction2SensorLeft) ||
						(isRight && currentFacing.equals(direction1) && result.Direction1SensorRight) ||
						(isRight && currentFacing.equals(direction2) && result.Direction2SensorRight))
				{
					continue;
				}
				
				boolean isTripped = world
						.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(sensePos).expand(-1, Config.sensorScanHeight, 1))
						.stream()
						.anyMatch(e -> !(e instanceof EntityPlayerMP) && Arrays.stream(Config.sensorClasses).anyMatch(eName ->
						{
							Class<?> nextClass = e.getClass();
							
							while(nextClass != null)
							{
								if (eName.equals(nextClass.getName()))
								{
									return true;
								}
								
								nextClass = nextClass.getSuperclass();
							}
							
							return false;
						}));
				
				if (isTripped)
				{
					setSensorCheckResults(isStraight, isLeft, isRight, currentFacing.equals(direction1), result);
				}
			}
			
			for(BlockPos invalidSensor : invalidSensors)
			{
				sensors.remove(invalidSensor);
			}
			
			return result;
		}
		
		private void setSensorCheckResults(boolean isStraight, boolean isLeft, boolean isRight, boolean isDirection1, SensorCheckResult results)
		{
			if (isDirection1)
			{
				if (isStraight) { results.Direction1Sensor = true; }
				else if (isLeft) { results.Direction1SensorLeft = true; }
				else if (isRight) { results.Direction1SensorRight = true; }
			}
			else
			{
				if (isStraight) { results.Direction2Sensor = true; }
				else if (isLeft) { results.Direction2SensorLeft = true; }
				else if (isRight) { results.Direction2SensorRight = true; }
			}
		}
		
		private Stages getNextLogicalStage(Stages currentStage, RightOfWays currentRightOfWay, Automator.SensorCheckResult sensorResult)
		{
			switch(currentStage)
			{
				case Red:
					if (sensorResult.Direction1SensorRight)
					{
						setNextUpdate(rightArrowTime);
						return Stages.Direction1RightTurnArrow;
					}
					else if (sensorResult.Direction2SensorRight)
					{
						setNextUpdate(rightArrowTime);
						return Stages.Direction2RightTurnArrow;
					}
					else if (sensorResult.Direction1SensorLeft &&
							sensorResult.Direction2SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.BothTurnArrow;
					}
					else if (sensorResult.Direction1SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.Direction1LeftTurnArrow;
					}
					else if (sensorResult.Direction2SensorLeft)
					{
						setNextUpdate(arrowMinimum);
						return Stages.Direction2LeftTurnArrow;
					}					
					
					return pedCheckedGreen(currentRightOfWay);
				case Direction1RightTurnArrow:
					setNextUpdate(yellowTime);
					if (sensorResult.Direction2SensorRight || sensorResult.Direction2SensorLeft)
					{
						return Stages.Direction1RightTurnArrowYellow;
					}
					else
					{
						return Stages.Direction1LeftTurnArrowYellow;
					}
				case Direction1RightTurnArrowYellow:
					if (sensorResult.Direction2SensorRight)
					{
						setNextUpdate(rightArrowTime);
					}
					else
					{
						setNextUpdate(arrowMinimum);
					}
					return Stages.Direction2LeftTurnArrow;
				case Direction2RightTurnArrow:
					setNextUpdate(yellowTime);
					if (sensorResult.Direction1SensorLeft || sensorResult.Direction1SensorRight)
					{
						return Stages.Direction2RightTurnArrowYellow;
					}
					else
					{
						return Stages.Direction2LeftTurnArrowYellow;
					}
				case Direction2RightTurnArrowYellow:
					if (sensorResult.Direction2SensorRight)
					{
						setNextUpdate(rightArrowTime);
					}
					else
					{
						setNextUpdate(arrowMinimum);
					}
					setNextUpdate(rightArrowTime);
					return Stages.Direction1LeftTurnArrow;
				case BothTurnArrow:
					setNextUpdate(yellowTime);
					return Stages.BothTurnArrowYellow;
				case BothTurnArrowYellow:
					return pedCheckedGreen(currentRightOfWay);
				case Direction1LeftTurnArrow:
					setNextUpdate(yellowTime);
					return Stages.Direction1LeftTurnArrowYellow;
				case Direction1LeftTurnArrowYellow:
					if (sensorResult.Direction2SensorRight)
					return pedCheckedGreen(currentRightOfWay);
				case Direction2LeftTurnArrow:
					setNextUpdate(yellowTime);
					return Stages.Direction2LeftTurnArrowYellow;
				case Direction2LeftTurnArrowYellow:
					return pedCheckedGreen(currentRightOfWay);
				case GreenDontCrossWarning:
				case Green:
					Automator.SensorCheckResult crossSensorCheck = checkSensors(currentRightOfWay.getNext());
					if (crossSensorCheck.Direction1Sensor || 
							crossSensorCheck.Direction1SensorLeft ||
							crossSensorCheck.Direction2Sensor ||
							crossSensorCheck.Direction2SensorLeft ||
							crossSensorCheck.Direction1SensorRight ||
							crossSensorCheck.Direction2SensorRight)
					{
						setNextUpdate(yellowTime);
						return Stages.Yellow;
					}

					Stages nextStage = pedCheckedGreen(currentRightOfWay);
					setNextUpdate(yellowTime);
					return nextStage;
				case Yellow:
					setNextUpdate(redTime);
					return Stages.Red;
				case GreenCross:
					setNextUpdate(crossWarningTime);
					return Stages.GreenDontCrossWarning;
			}
			
			return null;
		}
		
		private Stages pedCheckedGreen(RightOfWays rightOfWay)
		{
			if ((rightOfWay == RightOfWays.NorthSouth && isNorthSouthPedQueued()) ||
					(rightOfWay == RightOfWays.EastWest && isWestEastPedQueued()))
			{
				if (rightOfWay == RightOfWays.NorthSouth)
				{
					setNorthSouthPedQueued(false);
				}
				else
				{
					setWestEastPedQueued(false);
				}
				
				setNextUpdate(crossTime);
				return Stages.GreenCross;
			}
			
			setNextUpdate(greenMinimum);
			return Stages.Green;
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
		Red(0),
		Direction1LeftTurnArrow(1),
		Direction2LeftTurnArrow(2),
		BothTurnArrow(3),
		Direction1LeftTurnArrowYellow(4),
		Direction2LeftTurnArrowYellow(5),
		BothTurnArrowYellow(6),
		GreenCross(7),
		GreenDontCrossWarning(8),
		Green(9),
		Yellow(10),
		Direction1RightTurnArrow(11),
		Direction2RightTurnArrow(12),
		Direction1RightTurnArrowYellow(13),
		Direction2RightTurnArrowYellow(14);
		
		private int id;
		private Stages(int id)
		{
			this.id = id;
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
