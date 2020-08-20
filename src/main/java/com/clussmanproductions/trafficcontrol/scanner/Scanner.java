package com.clussmanproductions.trafficcontrol.scanner;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.util.ImmersiveRailroadingHelper;
import com.clussmanproductions.trafficcontrol.util.Tuple;
import com.google.common.collect.ImmutableList;

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
	
	private Vec3d lastPosition = null;
	private Vec3d lastMotion = null;
	private IScannerSubscriber lastSubscriber = null;
	private ScanRequest lastRequest = null;
	private int blocksScannedThisRequest = 0;
	private boolean foundTrainThisRequest = false;
	private boolean trainMovingTowardsDestThisRequest = false;
	private int blocksScannedThisTick = 0;
	
	public Scanner(World world)
	{
		super();
		_data = (ScannerData)world.loadData(ScannerData.class, "TC_scanner_data");
		if (_data == null)
		{
			_data = new ScannerData();
			world.setData(_data.mapName, _data);
		}
	}
	
	public <T extends TileEntity & IScannerSubscriber> void subscribe(T subscriber)
	{
		_data.addSubscriber(subscriber.getPos());
	}
	
	public void tick(World world) {
		try
		{
//			for(BlockPos pos : _data.getSubscribers())
//			{
//				if (!_world.isBlockLoaded(pos))
//				{
//					continue;
//				}
//				
//				TileEntity tileEntity = _world.getTileEntity(pos);
//				if (!(tileEntity instanceof IScannerSubscriber))
//				{
//					_data.removeSubscriber(pos);
//					continue;
//				}
//				
//				IScannerSubscriber subscriber = (IScannerSubscriber)tileEntity;
//				boolean hasCanceled = false;
//				for(ScanRequest req : subscriber.getScanRequests())
//				{
//					ScanCompleteData data = performScan(req);
//					
//					if (!_world.isBlockLoaded(pos))
//					{
//						break;
//					}
//					
//					subscriber.onScanComplete(data);
//					
//					if (!data.getContinueScanningForTileEntity())
//					{
//						hasCanceled = true;
//						break;
//					}
//				}
//				
//				if (!hasCanceled)
//				{
//					subscriber.onScanRequestsCompleted();
//				}
//			}
			
			blocksScannedThisTick = 0;
			
			int subscribersCheckedThisTick = 0;
			int subscriberTotal = _data.getSubscribers().size();
			
			while(blocksScannedThisTick < Config.borderTick && subscribersCheckedThisTick < subscriberTotal)
			{
				if (lastSubscriber == null)
				{
					// Get next first available subscriber
					lastSubscriber = getNextSubscriber(null, world);
				}
				
				if (lastSubscriber == null)
				{
					return;
				}
				
				List<ScanRequest> scannerRequests = lastSubscriber.getScanRequests();
				if (lastRequest == null && scannerRequests.size() > 0)
				{
					lastRequest = scannerRequests.get(0);
				}
				
				while(lastRequest != null)
				{
					ScanCompleteData data = performScan(lastRequest, world);
					
					if (data == null)
					{
						break;
					}
					
					lastPosition = null;
					lastMotion = null;
					blocksScannedThisRequest = 0;
					lastSubscriber.onScanComplete(data);
					
					if (!data.getContinueScanningForTileEntity())
					{
						lastRequest = null;
						break;
					}
					
					int nextRequestIndex = scannerRequests.indexOf(lastRequest) + 1;
					if (nextRequestIndex >= scannerRequests.size())
					{
						lastSubscriber.onScanRequestsCompleted();
						lastRequest = null;
					}
					else
					{
						lastRequest = scannerRequests.get(nextRequestIndex);
					}
				}
				
				if (lastRequest == null)
				{
					blocksScannedThisRequest = 0;
					foundTrainThisRequest = false;
					trainMovingTowardsDestThisRequest = false;
					lastSubscriber = getNextSubscriber(lastSubscriber, world);
				}
				
				subscribersCheckedThisTick++;
			}
		}
		catch(Exception ex) // Something went wrong - report it and try again
		{
			ex.printStackTrace();
		} 
	}
	
	private IScannerSubscriber getNextSubscriber(IScannerSubscriber lastSubscriber, World world)
	{
		ImmutableList<BlockPos> scannerPoses = _data.getSubscribers();
		if (scannerPoses.size() == 0)
		{
			return null;
		}
		
		BlockPos lastSubscriberPos = null;
		if (lastSubscriber != null)
		{
			lastSubscriberPos = ((TileEntity)lastSubscriber).getPos();
		}
		
		int lastIndex = scannerPoses.indexOf(lastSubscriberPos);		
		int nextIndex = lastIndex + 1;
		if (nextIndex >= scannerPoses.size())
		{
			nextIndex = 0;
		}
		
		IScannerSubscriber retVal = null;
		while(nextIndex != lastIndex)
		{
			BlockPos nextPos = scannerPoses.get(nextIndex);
			if (nextPos != null && world.isBlockLoaded(nextPos))
			{
				TileEntity teAtPos = world.getTileEntity(nextPos);
				if (teAtPos == null || teAtPos.isInvalid() || !(teAtPos instanceof IScannerSubscriber))
				{
					_data.removeSubscriber(nextPos);
				}
				else
				{
					retVal = (IScannerSubscriber)teAtPos;
					break;
				}
			}
			
			nextIndex++;
			if (nextIndex >= scannerPoses.size())
			{
				nextIndex = 0;
			}
			
			// Special check in case lastSubscriber passed in is null
			if (nextIndex == 0 && lastIndex == -1)
			{
				break;
			}
		}
		
		return retVal;
	}
	
	private ScanCompleteData performScan(ScanRequest req, World world)
	{
		Vec3d currentPosition = lastPosition != null ? lastPosition : new Vec3d(req.getStartingPos().getX(), req.getStartingPos().getY(), req.getStartingPos().getZ());
		Vec3d motion = lastMotion != null ? lastMotion : new Vec3d(req.getStartDirection().getDirectionVec());
		
		while(blocksScannedThisTick < Config.borderTick)
		{					
			Vec3d nextPosition = ImmersiveRailroadingHelper.getNextPosition(currentPosition, motion, world);
			
			if (nextPosition.equals(currentPosition))
			{
				return new ScanCompleteData(req, true, false, false);
			}
			
			motion = new Vec3d(nextPosition.x - currentPosition.x,
								nextPosition.y - currentPosition.y,
								nextPosition.z - currentPosition.z);
			
			currentPosition = nextPosition;
			
			Tuple<Boolean, Boolean> foundTrainData = checkPosition(req, currentPosition, motion, world);
			foundTrainThisRequest = foundTrainThisRequest || foundTrainData.getFirst();
			trainMovingTowardsDestThisRequest = trainMovingTowardsDestThisRequest || foundTrainData.getSecond();
			
			for(BlockPos endingPos : req.getEndingPositions())
			{
				AxisAlignedBB bb = new AxisAlignedBB(endingPos.down().south(2).west(2), endingPos.up(3).east(2).north(2));
				if (bb.contains(currentPosition))
				{
					return new ScanCompleteData(req, false, foundTrainThisRequest, trainMovingTowardsDestThisRequest);
				}
			}
			
			blocksScannedThisTick++;
			blocksScannedThisRequest++;
			
			if (blocksScannedThisRequest >= Config.borderTimeout)
			{
				return new ScanCompleteData(req, true, false, false);
			}
		}
		
		lastPosition = currentPosition;
		lastMotion = motion;
		return null;
	}
	
	private Tuple<Boolean, Boolean> checkPosition(ScanRequest req, Vec3d position, Vec3d motion, World world)
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
}
