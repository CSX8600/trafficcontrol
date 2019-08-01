package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity.EnumStatuses;
import com.clussmanproductions.trafficcontrol.util.AnyStatement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RelayTileEntity extends TileEntity implements ITickable {

	private boolean isMaster;
	private boolean isPowered;
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

		fillArrayListFromNBT("lamps", bellLocations, compound);
		fillArrayListFromNBT("gate", bellLocations, compound);
		fillArrayListFromNBT("bell", bellLocations, compound);
		fillArrayListFromNBT("wigwags", wigWagLocations, compound);
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

		return nbt;
	}

	@Override
	public void update() {
		if (world.isRemote)
		{
			return;
		}
		
		verifyLocations();
		notifyGates();
		updateLamps();
		updateBells();
		notifyWigWags();
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
				gate.setStatus(isPowered ? EnumStatuses.Closing : EnumStatuses.Opening);
			}
			
			alreadyNotifiedGates = true;
		}
	}

	private void updateLamps()
	{
		if (!isPowered && state != EnumState.Off)
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
		else if (!isPowered && state == EnumState.Off)
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
				bell.setIsRinging(isPowered);
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
					world.setBlockState(pos, currentState.withProperty(BlockWigWag.ACTIVE, isPowered));
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
	
	public void setPowered(boolean isPowered)
	{
		this.isPowered = isPowered;
		alreadyNotifiedGates = false;
		alreadyNotifiedBells = false;
		alreadyNotifiedWigWags = false;
		
		markDirty();
	}
}
