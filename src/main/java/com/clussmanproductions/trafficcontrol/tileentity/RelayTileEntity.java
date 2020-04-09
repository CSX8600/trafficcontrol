package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;
import com.clussmanproductions.trafficcontrol.scanner.IScannerSubscriber;
import com.clussmanproductions.trafficcontrol.scanner.ScanCompleteData;
import com.clussmanproductions.trafficcontrol.scanner.ScanRequest;
import com.clussmanproductions.trafficcontrol.scanner.ScannerThread;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity.EnumStatuses;
import com.clussmanproductions.trafficcontrol.util.AnyStatement;
import com.clussmanproductions.trafficcontrol.util.Tuple;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RelayTileEntity extends TileEntity implements ITickable, IScannerSubscriber {
	
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
	
	// Wig Wag information
	private boolean alreadyNotifiedWigWags;
	
	private ArrayList<BlockPos> crossingLampLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> crossingGateLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> bellLocations = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> wigWagLocations = new ArrayList<BlockPos>();
	private ArrayList<Tuple<BlockPos, EnumFacing>> shuntBorderOriginsAndFacing = new ArrayList<Tuple<BlockPos, EnumFacing>>();
	private ArrayList<Tuple<BlockPos, EnumFacing>> shuntIslandOriginsAndFacing = new ArrayList<Tuple<BlockPos, EnumFacing>>();
	
	private ArrayList<CrossingGateGateTileEntity> crossingGates = new ArrayList<CrossingGateGateTileEntity>();
	private ArrayList<BellBaseTileEntity> bells = new ArrayList<BellBaseTileEntity>();

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

		fillArrayListFromNBT("lamps", crossingLampLocations, compound);
		fillArrayListFromNBT("gate", crossingGateLocations, compound);
		fillArrayListFromNBT("bell", bellLocations, compound);
		fillArrayListFromNBT("wigwags", wigWagLocations, compound);
		
		int i = 0;
		while(compound.hasKey("island_pos_" + i))
		{
			BlockPos pos = BlockPos.fromLong(compound.getLong("island_pos_" + i));
			EnumFacing facing = EnumFacing.VALUES[compound.getInteger("island_facing_" + i)];
			
			shuntIslandOriginsAndFacing.add(new Tuple<BlockPos, EnumFacing>(pos, facing));
			i++;
		}
		
		i = 0;
		while(compound.hasKey("border_pos_" + i))
		{
			BlockPos pos = BlockPos.fromLong(compound.getLong("border_pos_" + i));
			EnumFacing facing = EnumFacing.VALUES[compound.getInteger("border_facing_" + i)];
			
			shuntBorderOriginsAndFacing.add(new Tuple<BlockPos, EnumFacing>(pos, facing));
			i++;
		}
	}
	
	private void fillArrayListFromNBT(String key, ArrayList<BlockPos> list, NBTTagCompound tag)
	{
		int i = 0;
		while (true) {
			if (!tag.hasKey(key + i)) {
				break;
			}

			int[] blockPos = tag.getIntArray(key + i);
			BlockPos pos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			list.add(pos);
			
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
		
		// Wig Wag information
		nbt.setBoolean("alreadynotifiedwigwags", alreadyNotifiedWigWags);

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
		
		for (int i = 0; i < wigWagLocations.size(); i++) {
			BlockPos wigWag = wigWagLocations.get(i);

			int[] wigWagPos = new int[] { wigWag.getX(), wigWag.getY(), wigWag.getZ() };
			nbt.setIntArray("wigwags" + i, wigWagPos);
		}

		for(int i = 0; i < shuntIslandOriginsAndFacing.size(); i++)
		{
			Tuple<BlockPos, EnumFacing> island = shuntIslandOriginsAndFacing.get(i);
			
			compound.setLong("island_pos_" + i, island.getFirst().toLong());
			compound.setInteger("island_facing_" + i, island.getSecond().getIndex());
		}
		
		for(int i = 0; i < shuntBorderOriginsAndFacing.size(); i++)
		{
			Tuple<BlockPos, EnumFacing> border = shuntBorderOriginsAndFacing.get(i);
			
			compound.setLong("border_pos_" + i, border.getFirst().toLong());
			compound.setInteger("border_facing_" + i, border.getSecond().getIndex());
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
		notifyWigWags();
		
		if (ModTrafficControl.IR_INSTALLED)
		{
			ScannerThread.ThreadsByWorld.get(world).subscribe(this);
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
	
	private void notifyWigWags()
	{
		if (!alreadyNotifiedWigWags)
		{
			ArrayList<BlockPos> positionsToRemove = new ArrayList<BlockPos>();
			for(BlockPos pos : wigWagLocations)
			{
				try
				{
					IBlockState currentState = world.getBlockState(pos);
					world.setBlockState(pos, currentState.withProperty(BlockWigWag.ACTIVE, getPowered()));
				}
				catch (Exception ex)
				{
					positionsToRemove.add(pos);
				}
			}
			
			for(BlockPos pos : positionsToRemove)
			{
				wigWagLocations.remove(pos);
				
				ModTrafficControl.logger.error("Wig Wag at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " has been unpaired due to an error");
			}
			
			alreadyNotifiedWigWags = true;
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

	public boolean addOrRemoveWigWag(BlockPos wigWagPos)
	{
		if (wigWagLocations.contains(wigWagPos))
		{
			wigWagLocations.remove(wigWagPos);
			return false;
		}
		else
		{
			wigWagLocations.add(wigWagPos);
			return true;
		}
	}
	
	public boolean addOrRemoveShuntBorder(BlockPos trackOrigin, EnumFacing shuntFacing)
	{
		Tuple<BlockPos, EnumFacing> value = new Tuple<BlockPos, EnumFacing>(trackOrigin, shuntFacing);
		
		if (shuntBorderOriginsAndFacing.contains(value))
		{
			shuntBorderOriginsAndFacing.remove(value);
			return false;
		}
		
		shuntBorderOriginsAndFacing.add(value);
		return true;
	}
	
	public boolean addOrRemoveShuntIsland(BlockPos trackOrigin, EnumFacing shuntFacing)
	{
		Tuple<BlockPos, EnumFacing> value = new Tuple<BlockPos, EnumFacing>(trackOrigin, shuntFacing);
		
		if (shuntIslandOriginsAndFacing.contains(value))
		{
			shuntIslandOriginsAndFacing.remove(value);
			return false;
		}
		
		shuntIslandOriginsAndFacing.add(value);
		return true;
	}
	
	public void setPowered(boolean isPowered)
	{
		this.isPowered = isPowered;
		alreadyNotifiedGates = false;
		alreadyNotifiedBells = false;
		alreadyNotifiedWigWags = false;
		
		markDirty();
	}

	private boolean getPowered()
	{
		return isPowered || automatedPowerOverride;
	}
	
	private final UUID islandRequest = UUID.fromString("da2e3487-9fe6-4369-80bc-4b5ce40f0530");
	private final UUID borderRequest = UUID.fromString("c4ba0fb7-3df0-4c18-9edf-491d825899d9");

	@Override
	public List<ScanRequest> getScanRequests() {
		ArrayList<ScanRequest> scanRequestList = new ArrayList<ScanRequest>();
		for(Tuple<BlockPos, EnumFacing> islandOrigin : shuntIslandOriginsAndFacing)
		{
			scanRequestList.add(new ScanRequest(
				islandRequest,
				islandOrigin.getFirst(),
				shuntIslandOriginsAndFacing.stream().map(tup -> tup.getFirst()).filter(pos -> !pos.equals(islandOrigin.getFirst())).collect(Collectors.toList()),
				islandOrigin.getSecond()));
		}
		
		for(Tuple<BlockPos, EnumFacing> borderOrigin : shuntBorderOriginsAndFacing)
		{
			scanRequestList.add(new ScanRequest(
					borderRequest,
					borderOrigin.getFirst(),
					shuntIslandOriginsAndFacing.stream().map(tup -> tup.getFirst()).collect(Collectors.toList()),
					borderOrigin.getSecond()));
		}

		return scanRequestList;
	}

	long lastMovementWorldTime = 0;
	@Override
	public void onScanComplete(ScanCompleteData scanCompleteData) {
		UUID scanRequestID = scanCompleteData.getScanRequest().getRequestID();
		if (scanRequestID.equals(islandRequest))
		{
			if (scanCompleteData.getTrainFound())
			{
				if (!automatedPowerOverride)
				{
					alreadyNotifiedBells = false;
					alreadyNotifiedGates = false;
					alreadyNotifiedWigWags = false;
					automatedPowerOverride = true;
					
					markDirty();
				}
				
				scanCompleteData.cancelScanningForTileEntity();
				return;
			}
		}
		else
		{
			if (scanCompleteData.getTimedOut())
			{
				return;
			}
			
			if (scanCompleteData.getTrainFound())
			{
				if (scanCompleteData.getTrainMovingTowardsDestination())
				{
					lastMovementWorldTime = world.getTotalWorldTime();
				}
				
				if (!automatedPowerOverride && scanCompleteData.getTrainMovingTowardsDestination())
				{
					alreadyNotifiedBells = false;
					alreadyNotifiedGates = false;
					alreadyNotifiedWigWags = false;
					automatedPowerOverride = true;
					
					markDirty();
				}
				
				if (automatedPowerOverride && !scanCompleteData.getTrainMovingTowardsDestination() && world.getTotalWorldTime() - lastMovementWorldTime > 200)
				{
					
				}
				else
				{
					scanCompleteData.cancelScanningForTileEntity(); // Islands are always checked before borders, so we can cancel
				}
				return;
			}
		}
	}
	
	@Override
	public void onScanRequestsCompleted() {
		if (automatedPowerOverride)
		{
			alreadyNotifiedBells = false;
			alreadyNotifiedGates = false;
			alreadyNotifiedWigWags = false;
			automatedPowerOverride = false;
			
			markDirty();
			return;
		}
	}

}
