package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.clussmanproductions.trafficcontrol.util.ImmersiveRailroadingHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class ShuntBaseTileEntity extends TileEntity {
	private BlockPos trackOrigin = new BlockPos(0, -1, 0);
	private ArrayList<BlockPos> relayBoxes = new ArrayList<>();
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		trackOrigin = arrayToBP(compound.getIntArray("origin"));
		
		int counter = 0;
		while(true)
		{
			if (!compound.hasKey("relayBox" + counter))
			{
				break;
			}
			
			relayBoxes.add(arrayToBP(compound.getIntArray("relayBox" + counter)));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setIntArray("origin", bpToArray(trackOrigin));
		
		for(int i = 0; i < relayBoxes.size(); i++)
		{
			compound.setIntArray("relayBox" + i, bpToArray(relayBoxes.get(i)));
		}
		
		return super.writeToNBT(compound);
	}
	
	private int[] bpToArray(BlockPos pos)
	{
		return new int[] { pos.getX(), pos.getY(), pos.getZ() };
	}
	
	private BlockPos arrayToBP(int[] array)
	{
		if(array.length < 3)
		{
			return new BlockPos(0, -1, 0);
		}
		
		return new BlockPos(array[0], array[1], array[2]);
	}

	protected abstract <T extends ShuntBaseTileEntity> Consumer<T> getRelayAddOrRemoveShuntMethod(RelayTileEntity relayTileEntity);
	
	public void onBreak()
	{
		for(BlockPos pos : relayBoxes)
		{
			TileEntity e = world.getTileEntity(pos);
			if (!(e instanceof RelayTileEntity))
			{
				continue;
			}
			
			RelayTileEntity rte = (RelayTileEntity)e;
			Consumer<ShuntBaseTileEntity> addOrRemoveConsumer = getRelayAddOrRemoveShuntMethod(rte);
			addOrRemoveConsumer.accept(this);
		}
	}

	public boolean setOrigin(EnumFacing facing)
	{
		Vec3d origin = ImmersiveRailroadingHelper.findOrigin(getPos(), facing, world);
		trackOrigin = new BlockPos(origin.x, origin.y, origin.z);
		
		return trackOrigin.getY() != -1;
	}
	
	public BlockPos getTrackOrigin()
	{
		return trackOrigin;
	}
}
