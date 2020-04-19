package com.clussmanproductions.trafficcontrol.util;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class MultithreadCapableEntityTracker extends EntityTracker {

	EntityTracker parent;
	private boolean tickInProgress = false;
	public MultithreadCapableEntityTracker(EntityTracker parent, WorldServer world) 
	{
		super(world);
		this.parent = parent;
	}
	@Override
	public void track(Entity entityIn) {
		// TODO Auto-generated method stub
		while(tickInProgress)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception ex) {}
		}
		parent.track(entityIn);
	}
	@Override
	public void track(Entity entityIn, int trackingRange, int updateFrequency) {
		// TODO Auto-generated method stub
		while(tickInProgress)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception ex) {}
		}
		parent.track(entityIn, trackingRange, updateFrequency);
	}
	@Override
	public void track(Entity entityIn, int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
		// TODO Auto-generated method stub
		while(tickInProgress)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception ex) {}
		}
		parent.track(entityIn, trackingRange, updateFrequency, sendVelocityUpdates);
	}
	@Override
	public void untrack(Entity entityIn) {
		// TODO Auto-generated method stub
		while(tickInProgress)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception ex) {}
		}
		parent.untrack(entityIn);
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		tickInProgress = true;
		parent.tick();
		tickInProgress = false;
	}
	@Override
	public void updateVisibility(EntityPlayerMP player) {
		// TODO Auto-generated method stub
		parent.updateVisibility(player);
	}
	@Override
	public void sendToTracking(Entity entityIn, Packet<?> packetIn) {
		// TODO Auto-generated method stub
		parent.sendToTracking(entityIn, packetIn);
	}
	@Override
	public Set<? extends EntityPlayer> getTrackingPlayers(Entity entity) {
		// TODO Auto-generated method stub
		return parent.getTrackingPlayers(entity);
	}
	@Override
	public void sendToTrackingAndSelf(Entity entityIn, Packet<?> packetIn) {
		// TODO Auto-generated method stub
		parent.sendToTrackingAndSelf(entityIn, packetIn);
	}
	@Override
	public void removePlayerFromTrackers(EntityPlayerMP player) {
		// TODO Auto-generated method stub
		parent.removePlayerFromTrackers(player);
	}
	@Override
	public void sendLeashedEntitiesInChunk(EntityPlayerMP player, Chunk chunkIn) {
		// TODO Auto-generated method stub
		parent.sendLeashedEntitiesInChunk(player, chunkIn);
	}
	@Override
	public void setViewDistance(int p_187252_1_) {
		// TODO Auto-generated method stub
		parent.setViewDistance(p_187252_1_);
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return parent.equals(obj);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return parent.hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return parent.toString();
	}
	
	
}
