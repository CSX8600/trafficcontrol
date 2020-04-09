package com.clussmanproductions.trafficcontrol.scanner;

import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class ScannerData extends WorldSavedData {

	private HashSet<BlockPos> tileEntitySubscriptions = new HashSet<BlockPos>();
	private ReentrantReadWriteLock tileEntitySubscriptionLock = new ReentrantReadWriteLock();
	
	public ScannerData(String mapName)
	{
		super(mapName);
	}
	
	public ScannerData()
	{
		super("TC_scanner_data");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int i = 0;
		WriteLock lock = tileEntitySubscriptionLock.writeLock();
		lock.lock();
		while(nbt.hasKey("blockpos" + i))
		{
			long serializedBlockPos = nbt.getLong("blockpos" + i);
			tileEntitySubscriptions.add(BlockPos.fromLong(serializedBlockPos));
			
			i++;
		}
		lock.unlock();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		int i = 0;
		ReadLock lock = tileEntitySubscriptionLock.readLock();
		lock.lock();
		for(BlockPos pos : tileEntitySubscriptions)
		{
			compound.setLong("blockpos" + i, pos.toLong());
		}
		lock.unlock();
		
		return compound;
	}
	
	public ImmutableList<BlockPos> getSubscribers()
	{
		ReadLock lock = tileEntitySubscriptionLock.readLock();
		
		lock.lock();
		ImmutableList<BlockPos> list = ImmutableList.copyOf(tileEntitySubscriptions);
		lock.unlock();
		
		return list;
	}
	
	public void addSubscriber(BlockPos pos)
	{
		WriteLock lock = tileEntitySubscriptionLock.writeLock();
		
		lock.lock();
		if (tileEntitySubscriptions.add(pos))
		{
			markDirty();
		}
		lock.unlock();
	}
	
	public void removeSubscriber(BlockPos pos)
	{
		WriteLock lock = tileEntitySubscriptionLock.writeLock();
		
		lock.lock();
		if (tileEntitySubscriptions.remove(pos))
		{
			markDirty();
		}
		lock.unlock();
	}
}
