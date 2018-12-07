package com.clussmanproductions.roadstuffreborn.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private double gateDelay = 0;
	private float gateRotation = -60;
	private EnumGateStatuses gateStatus = EnumGateStatuses.Open;
	
	// Lamp information
	
	

	private ArrayList<CrossingGateLampsTileEntity> crossingLamps = new ArrayList<CrossingGateLampsTileEntity>();
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
		gateDelay = compound.getDouble("gatedelay");
		gateRotation = compound.getFloat("gaterotation");
		gateStatus = EnumGateStatuses.getStatus(compound.getInteger("gatestatus"));

		if (!world.isRemote) {
			int i = 0;
			while (true) {
				if (!compound.hasKey("lamps" + i)) {
					break;
				}

				int[] blockPos = compound.getIntArray("lamps" + i);
				BlockPos lampPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
				TileEntity te = world.getTileEntity(lampPos);

				if (te instanceof CrossingGateLampsTileEntity) {
					crossingLamps.add((CrossingGateLampsTileEntity) te);
				}
			}

			while (true) {
				if (!compound.hasKey("gate" + i)) {
					break;
				}

				int[] blockPos = compound.getIntArray("gate" + i);
				BlockPos gatePos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
				TileEntity te = world.getTileEntity(gatePos);

				if (te instanceof CrossingGateGateTileEntity) {
					crossingGates.add((CrossingGateGateTileEntity) te);
				}
			}

			while (true) {
				if (!compound.hasKey("bell" + i)) {
					break;
				}

				int[] blockPos = compound.getIntArray("bell" + i);
				BlockPos bellPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
				TileEntity te = world.getTileEntity(bellPos);

				if (te instanceof BellBaseTileEntity) {
					bells.add((BellBaseTileEntity) te);
				}
			}
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
		nbt.setDouble("gatedelay", gateDelay);
		nbt.setFloat("gaterotation", gateRotation);
		nbt.setInteger("gatestatus", gateStatus.index);

		for (int i = 0; i < crossingLamps.size(); i++) {
			CrossingGateLampsTileEntity lamps = crossingLamps.get(i);

			int[] lampPos = new int[] { lamps.getPos().getX(), lamps.getPos().getY(), lamps.getPos().getZ() };
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

		return nbt;
	}

	@Override
	public void update() {
		if (isPowered)
		{
			
		}
		else
		{
			
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

	public void addCrossingGateLamp(CrossingGateLampsTileEntity lamps) {
		crossingLamps.add(lamps);
	}

	public void addCrossingGateGate(CrossingGateGateTileEntity gate) {
		crossingGates.add(gate);
	}

	public void addBell(BellBaseTileEntity bell) {
		bells.add(bell);
	}

	public void setPowered(boolean isPowered)
	{
		this.isPowered = isPowered;
	}
	
	public enum EnumGateStatuses
	{
		Open(0),
		Closing(1),
		Closed(2),
		Opening(3);
		
		private int index;
		
		EnumGateStatuses(int index)
		{
			this.index = index;
		}
		
		public static EnumGateStatuses getStatus(int index)
		{
			for(EnumGateStatuses status : EnumGateStatuses.values())
			{
				if (status.index == index)
				{
					return status;
				}
			}
			
			return null;
		}
	}
	
}
