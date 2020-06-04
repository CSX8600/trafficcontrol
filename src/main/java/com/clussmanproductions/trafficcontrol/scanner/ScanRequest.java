package com.clussmanproductions.trafficcontrol.scanner;

import java.util.List;
import java.util.UUID;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ScanRequest {
	private UUID requestID;
	private BlockPos startingPos;
	private List<BlockPos> endingPositions;
	private EnumFacing startDirection;
	
	public ScanRequest(UUID requestID, BlockPos startingPos, List<BlockPos> endingPositions, EnumFacing startDirection)
	{
		this.requestID = requestID;
		this.startingPos = startingPos;
		this.endingPositions = endingPositions;
		this.startDirection = startDirection;
	}
	
	public UUID getRequestID() { return requestID; }
	public BlockPos getStartingPos() { return startingPos; }
	public List<BlockPos> getEndingPositions() { return endingPositions; }
	public EnumFacing getStartDirection() { return startDirection; }
	
	@Override
	public int hashCode() {
		int hashCode = 19;
		
		hashCode = 487 * hashCode + requestID.hashCode();
		hashCode = 487 * hashCode + startingPos.hashCode();
		hashCode = 487 * hashCode + endingPositions.hashCode();
		hashCode = 487 * hashCode + startDirection.hashCode();
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) { return true; }
		if (obj == null) { return false; }
		if (obj.getClass() != this.getClass()) { return false; }
		
		ScanRequest scanRequest = (ScanRequest)obj;
		if (scanRequest.endingPositions.size() != endingPositions.size()) { return false; }
		
		boolean listEquals = true;
		for (int i = 0; i < scanRequest.endingPositions.size(); i++)
		{
			if (!endingPositions.get(i).equals(scanRequest.endingPositions.get(i)))
			{
				listEquals = false;
				break;
			}
		}
		
		return requestID.equals(scanRequest.requestID) &&
				startingPos.equals(scanRequest.startingPos) &&
				listEquals &&
				startDirection.equals(scanRequest.startDirection);
		
	}
}
