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
}
