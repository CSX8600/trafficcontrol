package com.clussmanproductions.trafficcontrol.scanner;

import net.minecraft.util.EnumFacing;

public class ScanCompleteData {
	private ScanRequest scanRequest;
	private boolean timedOut;
	private boolean trainFound;
	private boolean trainMovingTowardsDestination;
	private boolean continueScanningForTileEntity = true;
	
	public ScanCompleteData(ScanRequest scanRequest, boolean timedOut, boolean trainFound, boolean trainMovingTowardsDestination)
	{
		this.scanRequest = scanRequest;
		this.timedOut = timedOut;
		this.trainFound = trainFound;
		this.trainMovingTowardsDestination = trainMovingTowardsDestination;
	}
	
	public ScanRequest getScanRequest() { return scanRequest; }
	public boolean getTimedOut() { return timedOut; }
	public boolean getTrainFound() { return trainFound; }
	public boolean getTrainMovingTowardsDestination() { return trainMovingTowardsDestination; }
	public boolean getContinueScanningForTileEntity() { return continueScanningForTileEntity; }
	
	public void cancelScanningForTileEntity()
	{
		continueScanningForTileEntity = false;
	}
}
