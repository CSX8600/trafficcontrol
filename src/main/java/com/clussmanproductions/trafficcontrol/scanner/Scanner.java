package com.clussmanproductions.trafficcontrol.scanner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.ImmersiveRailroadingHelper;
import com.clussmanproductions.trafficcontrol.util.Tuple;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Scanner
{
	private ScannerData _data;
	public static HashMap<Integer, Scanner> ScannersByWorld = new HashMap<>();
	
	private ScanSession[] scansInProgress;
	private HashSet<IScannerSubscriber> requestsHandledThisTick = new HashSet<>();
	private int lastIndex = 0;
	
	public Scanner(World world)
	{
		super();
		_data = (ScannerData)world.loadData(ScannerData.class, "TC_scanner_data");
		if (_data == null)
		{
			_data = new ScannerData();
			world.setData(_data.mapName, _data);
		}
		
		scansInProgress = new ScanSession[Config.parallelScans];
		for(int i = 0; i < Config.parallelScans; i++)
		{
			scansInProgress[i] = new ScanSession();
		}
	}
	
	public <T extends TileEntity & IScannerSubscriber> void subscribe(T subscriber)
	{
		_data.addSubscriber(subscriber.getPos());
	}
	
	public void tick(World world) {
		try
		{
			if (_data.getSubscribers().size() == 0)
			{
				return;
			}
			
			for(ScanSession scanSession : scansInProgress)
			{
				if (scanSession.getScanSubscriber() == null)
				{
					tryFindNextSubscriber(scanSession, world);
				}
				
				if (scanSession.getScanRequest() == null)
				{
					scanSession.setScanSubscriber(null);
					continue;
				}
				
				ScanRequest request = scanSession.getScanRequest();
				while(scanSession.getBlocksScannedThisSession() < Config.borderTimeout && scanSession.getBlocksScannedThisTick() < Config.borderTick && request != null)
				{
					Vec3d lastPosition = scanSession.getLastPosition();
					Vec3d motion = scanSession.getMotion();
					if (lastPosition == null)
					{
						lastPosition = new Vec3d(request.getStartingPos());
						motion = new Vec3d(request.getStartDirection().getDirectionVec());
					}
					
					Vec3d nextPosition = ImmersiveRailroadingHelper.getNextPosition(lastPosition, motion, world);
					scanSession.addBlockScannedThisTick();
					if (nextPosition.equals(lastPosition))
					{
						ScanCompleteData completeData = new ScanCompleteData(request, true, false, false);
						scanSession.getScanSubscriber().onScanComplete(completeData);
						if (completeData.getContinueScanningForTileEntity())
						{
							scanSession.popRequest();
						}
						
						if (!completeData.getContinueScanningForTileEntity() || scanSession.getScanRequest() == null)
						{
							tryFindNextSubscriber(scanSession, world);
						}

						request = scanSession.getScanRequest();
						continue;
					}
					
					motion = new Vec3d(nextPosition.x - lastPosition.x,
									   nextPosition.y - lastPosition.y,
									   nextPosition.z - lastPosition.z);
					
					Tuple<Boolean, Boolean> trainResultHere = checkPosition(nextPosition, motion, world);
					if (trainResultHere.getFirst())
					{
						scanSession.setFoundTrain(true);
						scanSession.setTrainMovingTowardsDestination(scanSession.isTrainMovingTowardsDestination() || trainResultHere.getSecond());
					}
					
					boolean whileLoopContinue = false;
					for(BlockPos endingPos : request.getEndingPositions())
					{
						AxisAlignedBB endingBB = new AxisAlignedBB(endingPos);
						endingBB = endingBB.expand(-1, -1, -1).expand(1, 1, 1);
						
						if (endingBB.contains(nextPosition))
						{
							ScanCompleteData data = new ScanCompleteData(request, false, scanSession.isFoundTrain(), scanSession.isTrainMovingTowardsDestination());
							scanSession.getScanSubscriber().onScanComplete(data);
							if (data.getContinueScanningForTileEntity())
							{
								scanSession.popRequest();
							}
							
							if (!data.getContinueScanningForTileEntity() || data.getScanRequest() == null)
							{
								tryFindNextSubscriber(scanSession, world);
							}
							
							request = scanSession.getScanRequest();
							whileLoopContinue = true;
							break;
						}
					}
					
					if (whileLoopContinue)
					{
						continue;
					}
					
					scanSession.setLastPosition(nextPosition);
					scanSession.setMotion(motion);
				}
				
				if (request == null && scanSession.getScanSubscriber() != null)
				{
					scanSession.getScanSubscriber().onScanRequestsCompleted();
					tryFindNextSubscriber(scanSession, world);
				}
				
				if (scanSession.getBlocksScannedThisSession() >= Config.borderTimeout)
				{
					ScanCompleteData timeout = new ScanCompleteData(request, true, false, false);
					scanSession.getScanSubscriber().onScanComplete(timeout);
					
					if (timeout.getContinueScanningForTileEntity())
					{
						scanSession.popRequest();
					}
					
					if (!timeout.getContinueScanningForTileEntity() || scanSession.getScanRequest() == null)
					{
						tryFindNextSubscriber(scanSession, world);
					}
					
					request = scanSession.getScanRequest();
				}
			}
			
			// Cleanup tick-based variables
			requestsHandledThisTick.clear();
			for(ScanSession scanSession : scansInProgress)
			{
				scanSession.resetBlocksScannedThisTick();
			}
		}
		catch(Exception ex) // Something went wrong - report it and try again
		{
			ModTrafficControl.logger.error("Error in scanner", ex);
		} 
	}
	
	private void tryFindNextSubscriber(ScanSession scan, World world)
	{
		lastIndex++;
		if (lastIndex >= _data.getSubscribers().size())
		{
			lastIndex = 0;
		}
		
		IScannerSubscriber thisScanSubscriber = null;
		do
		{
			BlockPos subscriberPos = _data.getSubscribers().get(lastIndex);
			if (world.isBlockLoaded(subscriberPos, false))
			{
				TileEntity te = world.getTileEntity(subscriberPos);
				if (IScannerSubscriber.class.isAssignableFrom(te.getClass()))
				{
					thisScanSubscriber = (IScannerSubscriber)te;
					
					if (thisScanSubscriber.getScanRequests().size() != 0)
					{
						final IScannerSubscriber finalThisScanSubscriber = thisScanSubscriber;
						if (!requestsHandledThisTick.add(thisScanSubscriber) || Arrays.stream(scansInProgress).anyMatch(ss -> ss.getScanSubscriber() == finalThisScanSubscriber))
						{
							thisScanSubscriber = null;
						}
					}
				}
			}
			
			if (thisScanSubscriber == null)
			{
				lastIndex++;
			}
		}
		while(lastIndex < _data.getSubscribers().size() && thisScanSubscriber == null);
		
		scan.setScanSubscriber(thisScanSubscriber);
	}
		
	private Tuple<Boolean, Boolean> checkPosition(Vec3d position, Vec3d motion, World world)
	{		
		net.minecraft.util.Tuple<UUID, Vec3d> moveableRollingStockNearby = ImmersiveRailroadingHelper.getStockNearby(position, world);
		if (moveableRollingStockNearby != null)
		{
			Vec3d stockVelocity = moveableRollingStockNearby.getSecond();
			
			if (stockVelocity.x == 0 && stockVelocity.y == 0 && stockVelocity.z == 0)
			{
				return new Tuple<Boolean, Boolean>(true, false);
			}
			
			EnumFacing stockMovementFacing = EnumFacing.getFacingFromVector((float)stockVelocity.x, (float)stockVelocity.y, (float)stockVelocity.z);
			EnumFacing motionFacing = EnumFacing.getFacingFromVector((float)motion.x, (float)motion.y, (float)motion.z);
			
			boolean trainMovingTowardsDestination = motionFacing.equals(stockMovementFacing);
			return new Tuple<Boolean, Boolean>(true, trainMovingTowardsDestination);
		}
		
		return new Tuple<Boolean, Boolean>(false, false);
	}

	private static class ScanSession
	{
		private IScannerSubscriber scanSubscriber = null;
		private Queue<ScanRequest> scanRequestsToDo;
		private int blocksScannedThisTick = 0;
		private int blocksScannedThisSession = 0;
		private Vec3d lastPosition = null;
		private Vec3d motion = null;
		private boolean foundTrain = false;
		private boolean trainMovingTowardsDestination = false;
		
		public IScannerSubscriber getScanSubscriber() {
			return scanSubscriber;
		}
		
		public void setScanSubscriber(IScannerSubscriber scanSubscriber) {
			this.scanSubscriber = scanSubscriber;
			
			if (this.scanSubscriber != null)
			{
				scanRequestsToDo = new LinkedList<>(scanSubscriber.getScanRequests());
			}
			else
			{
				scanRequestsToDo = new LinkedList<>();
			}
			
			lastPosition = null;
			motion = null;
			blocksScannedThisSession = 0;
			foundTrain = false;
			trainMovingTowardsDestination = false;
		}
		
		public ScanRequest getScanRequest()
		{
			return scanRequestsToDo.peek();
		}
		
		public void popRequest()
		{
			scanRequestsToDo.poll();
			lastPosition = null;
			motion = null;
			blocksScannedThisSession = 0;
			foundTrain = false;
			trainMovingTowardsDestination = false;
		}
		
		public int getBlocksScannedThisTick()
		{
			return blocksScannedThisTick;
		}
		
		public void addBlockScannedThisTick()
		{
			blocksScannedThisTick++;
			blocksScannedThisSession++;
		}
		
		public void resetBlocksScannedThisTick()
		{
			blocksScannedThisTick = 0;
		}
		
		public int getBlocksScannedThisSession()
		{
			return blocksScannedThisSession;
		}
		
		public Vec3d getLastPosition() {
			return lastPosition;
		}

		public void setLastPosition(Vec3d lastPosition) {
			this.lastPosition = lastPosition;
		}
		
		public Vec3d getMotion() {
			return motion;
		}

		public void setMotion(Vec3d motion) {
			this.motion = motion;
		}

		public boolean isFoundTrain() {
			return foundTrain;
		}

		public void setFoundTrain(boolean foundTrain) {
			this.foundTrain = foundTrain;
		}

		public boolean isTrainMovingTowardsDestination() {
			return trainMovingTowardsDestination;
		}

		public void setTrainMovingTowardsDestination(boolean trainMovingTowardsDestination) {
			this.trainMovingTowardsDestination = trainMovingTowardsDestination;
		}
		
		
	}
}
