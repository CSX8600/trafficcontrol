package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntBorder;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntIsland;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity.EnumStatuses;
import com.clussmanproductions.trafficcontrol.util.AnyStatement;
import com.clussmanproductions.trafficcontrol.util.ImmersiveRailroadingHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RelayTileEntity extends TileEntity implements ITickable {

	private AutomatedRelaySystem relaySystem = null;
	
	private boolean isMaster;
	private boolean isPowered;
	private boolean automatedPowerOverride;
	private int masterX;
	private int masterY;
	private int masterZ;
	
	// Gate information
	private boolean alreadyNotifiedGates;
		
	// Lamp information
	private int lastFlash = 19;
	private EnumState state = EnumState.Off;
	
	// Bell information
	private boolean alreadyNotifiedBells;
	
	private ArrayList<BlockPos> crossingLampLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> crossingGateLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> bellLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> shuntBorderLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> shuntIslandLocations = new ArrayList<BlockPos>();
	
	private ArrayList<CrossingGateGateTileEntity> crossingGates = new ArrayList<CrossingGateGateTileEntity>();
	private ArrayList<BellBaseTileEntity> bells = new ArrayList<BellBaseTileEntity>();
	private ArrayList<ShuntBorderTileEntity> shuntBorders = new ArrayList<ShuntBorderTileEntity>();
	private ArrayList<ShuntIslandTileEntity> shuntIslands = new ArrayList<ShuntIslandTileEntity>();

	public RelayTileEntity() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		isMaster = compound.getBoolean("ismaster");
		isPowered = compound.getBoolean("ispowered");
		masterX = compound.getInteger("masterx");
		masterY = compound.getInteger("mastery");
		masterZ = compound.getInteger("masterz");
		
		// Gate information
		alreadyNotifiedGates = compound.getBoolean("alreadynotifiedgates");
		
		// Lamp information
		lastFlash = compound.getInteger("lastflash");
		state = EnumState.getStateByID(compound.getInteger("state"));
		
		// Bell information
		alreadyNotifiedBells = compound.getBoolean("alreadynotifiedbells");

		int i = 0;
		while (true) {
			if (!compound.hasKey("lamps" + i)) {
				break;
			}

			int[] blockPos = compound.getIntArray("lamps" + i);
			BlockPos lampPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			crossingLampLocations.add(lampPos);
			
			i++;
		}

		i = 0;
		while (true) {
			if (!compound.hasKey("gate" + i)) {
				break;
			}

			int[] blockPos = compound.getIntArray("gate" + i);
			BlockPos gatePos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			crossingGateLocations.add(gatePos);
			
			i++;
		}

		i = 0;
		while (true) {
			if (!compound.hasKey("bell" + i)) {
				break;
			}

			int[] blockPos = compound.getIntArray("bell" + i);
			BlockPos bellPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			bellLocations.add(bellPos);
			
			i++;
		}
		
		i = 0;
		while (true) {
			if (!compound.hasKey("shuntBorder" + i)) {
				break;
			}

			int[] blockPos = compound.getIntArray("shuntBorder" + i);
			BlockPos shuntPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			shuntBorderLocations.add(shuntPos);
			
			i++;
		}
		
		i = 0;
		while (true) {
			if (!compound.hasKey("shuntIsland" + i)) {
				break;
			}

			int[] blockPos = compound.getIntArray("shuntIsland" + i);
			BlockPos shuntPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			shuntIslandLocations.add(shuntPos);
			
			i++;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = super.writeToNBT(compound);

		nbt.setBoolean("ismaster", isMaster);
		nbt.setBoolean("ispowered", isPowered);
		nbt.setInteger("masterx", masterX);
		nbt.setInteger("mastery", masterY);
		nbt.setInteger("masterz", masterZ);
		
		// Gate information
		nbt.setBoolean("alreadynotifiedgates", alreadyNotifiedGates);
		
		// Lamp information
		nbt.setInteger("lastflash", lastFlash);
		nbt.setInteger("state", state.getID());
		
		// Bell information
		nbt.setBoolean("alreadynotifiedbells", alreadyNotifiedBells);

		for (int i = 0; i < crossingLampLocations.size(); i++) {
			BlockPos lamps = crossingLampLocations.get(i);

			int[] lampPos = new int[] { lamps.getX(), lamps.getY(), lamps.getZ() };
			nbt.setIntArray("lamps" + i, lampPos);
		}

		for (int i = 0; i < crossingGates.size(); i++) {
			CrossingGateGateTileEntity gate = crossingGates.get(i);

			int[] gatePos = new int[] { gate.getPos().getX(), gate.getPos().getY(), gate.getPos().getZ() };
			nbt.setIntArray("gate" + i, gatePos);
		}

		for (int i = 0; i < bells.size(); i++) {
			BellBaseTileEntity bell = bells.get(i);

			int[] bellPos = new int[] { bell.getPos().getX(), bell.getPos().getY(), bell.getPos().getZ() };
			nbt.setIntArray("bell" + i, bellPos);
		}

		for (int i = 0; i < shuntBorders.size(); i++) {
			ShuntBorderTileEntity shunt = shuntBorders.get(i);

			int[] shuntPos = new int[] { shunt.getPos().getX(), shunt.getPos().getY(), shunt.getPos().getZ() };
			nbt.setIntArray("shuntBorder" + i, shuntPos);
		}

		for (int i = 0; i < shuntIslands.size(); i++) {
			ShuntIslandTileEntity shunt = shuntIslands.get(i);

			int[] shuntPos = new int[] { shunt.getPos().getX(), shunt.getPos().getY(), shunt.getPos().getZ() };
			nbt.setIntArray("shuntIsland" + i, shuntPos);
		}

		return nbt;
	}

	@Override
	public void update() {
		if (world.isRemote || !isMaster)
		{
			return;
		}
		
		verifyLocations();
		notifyGates();
		updateLamps();
		updateBells();
		
		if (ModTrafficControl.IR_INSTALLED)
		{
			if (relaySystem == null)
			{
				relaySystem = new AutomatedRelaySystem();
			}
			
			relaySystem.tick();
		}
	}
	
	private void verifyLocations()
	{
		ArrayList<BlockPos> posToRemove = new ArrayList<BlockPos>();
		if (crossingGateLocations.size() != crossingGates.size() || 
				AnyStatement.Any(crossingGates, gate -> gate.isInvalid()))
		{
			crossingGates.clear();
			
			for(BlockPos crossingGatePos : crossingGateLocations)
			{
				TileEntity gate = world.getTileEntity(crossingGatePos);
				
				if (gate == null || !(gate instanceof CrossingGateGateTileEntity))
				{
					posToRemove.add(crossingGatePos);
				}
				else
				{
					crossingGates.add((CrossingGateGateTileEntity)gate);
				}
			}
			
			for (BlockPos gateLocationToRemove : posToRemove)
			{
				crossingGateLocations.remove(gateLocationToRemove);
			}
		}
				
		if (bellLocations.size() != bells.size() ||
				AnyStatement.Any(bells, bell -> bell.isInvalid()))
		{
			bells.clear();
			
			posToRemove.clear();
			for(BlockPos bellPos : bellLocations)
			{
				TileEntity bell = world.getTileEntity(bellPos);
				
				if (bell == null || !(bell instanceof BellBaseTileEntity))
				{
					posToRemove.add(bellPos);
				}
				else
				{
					bells.add((BellBaseTileEntity)bell);
				}
			}
			
			for (BlockPos bellLocationToRemove : posToRemove)
			{
				bellLocations.remove(bellLocationToRemove);
			}
		}
		
		if (shuntBorderLocations.size() != shuntBorders.size() ||
				AnyStatement.Any(shuntBorders, (border) -> border.isInvalid()))
		{
			shuntBorders.clear();
			
			posToRemove.clear();
			for(BlockPos shuntPos : shuntBorderLocations)
			{
				TileEntity shunt = world.getTileEntity(shuntPos);
				
				if (shunt == null || !(shunt instanceof ShuntBorderTileEntity))
				{
					posToRemove.add(shuntPos);
				}
				else
				{
					shuntBorders.add((ShuntBorderTileEntity)shunt);
				}
			}
			
			for (BlockPos shuntLocationToRemove : posToRemove)
			{
				shuntBorderLocations.remove(shuntLocationToRemove);
			}
		}
		
		if (shuntIslandLocations.size() != shuntIslands.size() ||
				AnyStatement.Any(shuntIslands, (island) -> island.isInvalid()))
		{
			shuntIslands.clear();
			
			posToRemove.clear();
			for(BlockPos shuntPos : shuntIslandLocations)
			{
				TileEntity shunt = world.getTileEntity(shuntPos);
				
				if (shunt == null || !(shunt instanceof ShuntIslandTileEntity))
				{
					posToRemove.add(shuntPos);
				}
				else
				{
					shuntIslands.add((ShuntIslandTileEntity)shunt);
				}
			}
			
			for (BlockPos shuntLocationToRemove : posToRemove)
			{
				shuntIslandLocations.remove(shuntLocationToRemove);
			}
		}
	}
	
	private void notifyGates()
	{
		if (!alreadyNotifiedGates)
		{
			for(CrossingGateGateTileEntity gate : crossingGates)
			{
				gate.setStatus(getPowered() ? EnumStatuses.Closing : EnumStatuses.Opening);
			}
			
			alreadyNotifiedGates = true;
		}
	}

	private void updateLamps()
	{
		if (!getPowered() && state != EnumState.Off)
		{
			if (crossingGates.size() == 0)
			{
				lastFlash = 19;
				state = EnumState.Off;
				
				notifyLamps();
				
				markDirty();
			}
			else
			{
				if (crossingGates.get(0).getStatus() == EnumStatuses.Open)
				{
					lastFlash = 19;
					state = EnumState.Off;
					
					notifyLamps();
					
					markDirty();
				}
			}
		}
		else if (!getPowered() && state == EnumState.Off)
		{
			return;
		}
		
		if (lastFlash < 20)
		{
			lastFlash++;
			
			markDirty();
			
			return;
		}
		
		lastFlash = 0;
		
		if (state == EnumState.Flash2 || state == EnumState.Off)
		{
			state = EnumState.Flash1;
		}
		else
		{
			state = EnumState.Flash2;
		}
		
		notifyLamps();
		
		markDirty();
	}
	
	private void notifyLamps()
	{
		ArrayList<BlockPos> positionsToRemove = new ArrayList<BlockPos>();
		for(BlockPos lampLocation : crossingLampLocations)
		{
			try
			{
				IBlockState lampState = world.getBlockState(lampLocation);
				world.setBlockState(lampLocation, lampState.withProperty(BlockLampBase.STATE, state));
			}
			catch (Exception ex)
			{
				positionsToRemove.add(lampLocation);
			}
		}
		
		for(BlockPos positionToRemove : positionsToRemove)
		{
			crossingLampLocations.remove(positionToRemove);
			
			ModTrafficControl.logger.error("Crossing Lamp at " + positionToRemove.getX() + ", " + positionToRemove.getY() + ", " + positionToRemove.getZ() + " has been unpaired due to an error");
		}
	}
	
	private void updateBells()
	{
		if (!alreadyNotifiedBells)
		{
			for(BellBaseTileEntity bell : bells)
			{
				bell.setIsRinging(getPowered());
			}
			
			alreadyNotifiedBells = true;
		}
	}
	
	public void setMaster() {
		isMaster = true;
		markDirty();
	}

	public void setMasterLocation(BlockPos pos) {
		masterX = pos.getX();
		masterY = pos.getY();
		masterZ = pos.getZ();
	}

	public RelayTileEntity getMaster(World world) {
		if (isMaster) {
			return this;
		}

		TileEntity master = world.getTileEntity(getMasterBlockPos());

		if (master == null || !(master instanceof RelayTileEntity)) {
			return null;
		}

		return (RelayTileEntity) master;
	}

	private BlockPos getMasterBlockPos() {
		return new BlockPos(masterX, masterY, masterZ);
	}

	public boolean addOrRemoveCrossingGateLamp(BlockPos lampPos) {
		BlockPos posToRemove = null;
		for(BlockPos existingLampPos : crossingLampLocations)
		{
			if (lampPos.equals(existingLampPos))
			{
				posToRemove = existingLampPos;
				break;
			}
		}
		
		if (posToRemove != null)
		{
			crossingLampLocations.remove(posToRemove);
			return false;
		}
		
		crossingLampLocations.add(lampPos);
		return true;
	}

	public boolean addOrRemoveCrossingGateGate(CrossingGateGateTileEntity gate) {
		BlockPos posToRemove = null;
		
		for(BlockPos pos : crossingGateLocations)
		{
			if (pos.equals(gate.getPos()))
			{
				posToRemove = pos;
				break;
			}
		}
		
		if (posToRemove != null)
		{
			crossingGateLocations.remove(posToRemove);
			return false;
		}
		
		crossingGateLocations.add(gate.getPos());
		return true;
	}

	public boolean addOrRemoveBell(BellBaseTileEntity bell) {
		BlockPos posToRemove = null;
		
		for(BlockPos pos : bellLocations)
		{
			if (pos.equals(bell.getPos()))
			{
				posToRemove = pos;
				break;
			}
		}
		
		if (posToRemove != null)
		{
			bellLocations.remove(posToRemove);
			return false;
		}
		
		bellLocations.add(bell.getPos());
		return true;
	}

	public boolean addOrRemoveShuntBorder(ShuntBorderTileEntity shuntBorder)
	{
		BlockPos borderPos = shuntBorder.getPos();
		
		if (shuntBorderLocations.contains(borderPos))
		{
			shuntBorderLocations.remove(borderPos);
			return false;
		}
		
		shuntBorderLocations.add(borderPos);
		return true;
	}
	
	public boolean addOrRemoveShuntIsland(ShuntIslandTileEntity shuntIsland)
	{
		BlockPos islandPos = shuntIsland.getPos();
		
		if (shuntIslandLocations.contains(islandPos))
		{
			shuntIslandLocations.remove(islandPos);
			return false;
		}
		
		shuntIslandLocations.add(islandPos);
		return true;
	}
	
	public void setPowered(boolean isPowered)
	{
		this.isPowered = isPowered;
		alreadyNotifiedGates = false;
		alreadyNotifiedBells = false;
		
		markDirty();
	}

	private boolean getPowered()
	{
		return isPowered || automatedPowerOverride;
	}
	
	private class AutomatedRelaySystem
	{
		private final int noMotionTimeout = 200;
		private HashMap<BlockPos, ShuntIslandTileEntity> islandOrigins = new HashMap<BlockPos, ShuntIslandTileEntity>();
		private ShuntBorderTileEntity lastEntity = null;
		private Vec3d lastPosition = null;
		private Vec3d lastMotion = null;
		private int ticksSinceLastMotion = 0;
		
 		public void tick()
		{
 			//ticksSinceIslandCheck++;
			// If there has been an addition or removal to our shunt lists,
			// we will need to reload our caches
			reloadCaches();
			
			boolean doOverride = checkIslands();
			
			if (!doOverride)
			{
				doOverride = checkBorders();
			}
			
			if ((doOverride && !automatedPowerOverride) || (automatedPowerOverride && !doOverride))
			{
				alreadyNotifiedGates = false;
				alreadyNotifiedBells = false;
			}
			
			automatedPowerOverride = doOverride;
		}
		
		private void reloadCaches()
		{
			if (shuntIslands.size() != islandOrigins.size())
			{
				islandOrigins.clear();
				
				for(ShuntIslandTileEntity island : shuntIslands)
				{
					islandOrigins.put(island.getTrackOrigin(), island);
				}
			}
		}
		
		private boolean checkIslands()
		{
			for(ShuntIslandTileEntity island : shuntIslands)
			{
				IBlockState blockState = world.getBlockState(island.getPos());
				if (blockState.getBlock() != ModBlocks.shunt_island)
				{
					continue; // This will be cleaned up shortly
				}
				EnumFacing facing = blockState.getValue(BlockShuntIsland.FACING);
				BlockPos originPos = island.getTrackOrigin();
				Vec3d vecOrigin = new Vec3d(originPos.getX(), originPos.getY(), originPos.getZ());
				Vec3d workingPos = new Vec3d(originPos.getX(), originPos.getY(), originPos.getZ());
				Vec3d motion = new Vec3d(facing.getDirectionVec());
				
				for(int i = 0; i < Config.islandTimeout; i++)
				{
					Vec3d nextPos = ImmersiveRailroadingHelper.getNextPosition(workingPos, motion, world);
					Vec3d thisMotion = new Vec3d(nextPos.x - workingPos.x, nextPos.y - workingPos.y, nextPos.z - workingPos.z);
					Tuple<UUID, Vec3d> nearbyStock = ImmersiveRailroadingHelper.getStockNearby(nextPos, world);
					if (nearbyStock != null)
					{
						return true;
					}
					
					ShuntIslandTileEntity entity = islandOrigins.getOrDefault(new BlockPos(nextPos.x, nextPos.y, nextPos.z), null);
					if (entity != null && entity != island)
					{
						ModTrafficControl.logger.debug("Breaking");
						break;
					}
					
					workingPos = nextPos;
					motion = thisMotion;
				}
			}
			
			return false;
		}
	
		private boolean checkBorders()
		{
			int blocksCheckedThisTick = 0;
			int entityIndex = shuntBorders.indexOf(lastEntity);
			
			while (true)
			{				
				if (shuntBorders.size() <= 0)
				{
					return false;
				}
				
				if (entityIndex == -1 || entityIndex == shuntBorders.size())
				{
					entityIndex = 0;
				}
				
				lastEntity = shuntBorders.get(entityIndex);
				if (blocksCheckedThisTick >= Config.borderTick)
				{
					return false;
				}
				
				if (lastPosition == null)
				{
					BlockPos origin = shuntBorders.get(entityIndex).getTrackOrigin();
					lastPosition = new Vec3d(origin.getX(), origin.getY(), origin.getZ());
					EnumFacing facing = world.getBlockState(shuntBorders.get(entityIndex).getPos()).getValue(BlockShuntBorder.FACING);
					lastMotion = new Vec3d(facing.getDirectionVec());
				}
				
				Vec3d nextPosition = ImmersiveRailroadingHelper.getNextPosition(lastPosition, lastMotion, world);
				blocksCheckedThisTick++;
				if (nextPosition == lastPosition)
				{
					lastPosition = null;
					lastMotion = null;
					entityIndex++;
					continue;
				}
				
				if (islandOrigins.containsKey(new BlockPos(nextPosition.x, nextPosition.y, nextPosition.z)))
				{
					lastPosition = null;
					lastMotion = null;
					entityIndex++;
					continue;
				}
				
				Tuple<UUID, Vec3d> stock = ImmersiveRailroadingHelper.getStockNearby(nextPosition, world);
				if (stock != null)
				{
					Vec3d stockMotion = stock.getSecond();
					
					boolean noMotion = stockMotion.equals(new Vec3d(0, 0, 0));
					
					if (noMotion)
					{
						if (ticksSinceLastMotion < noMotionTimeout)
						{
							ticksSinceLastMotion++;
						}
					}
					else
					{
						ticksSinceLastMotion = 0;
					}
					
					EnumFacing stockMovement = EnumFacing.getFacingFromVector((float)stockMotion.x, (float)stockMotion.y, (float)stockMotion.z);
					EnumFacing motionDirection = EnumFacing.getFacingFromVector((float)lastMotion.x, (float)lastMotion.y, (float)lastMotion.z);
					
					if ((!noMotion && stockMovement.equals(motionDirection)) || (noMotion && ticksSinceLastMotion < noMotionTimeout))
					{
						return true;
					}
					else
					{
						lastPosition = null;
						lastMotion = null;
						entityIndex++;
						continue;
					}
				}
				
				lastMotion = new Vec3d(nextPosition.x - lastPosition.x, nextPosition.y - lastPosition.y, nextPosition.z - lastPosition.z);
				lastPosition = nextPosition;
			}
		}
	}
}
