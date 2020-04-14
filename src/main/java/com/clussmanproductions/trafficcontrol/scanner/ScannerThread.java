package com.clussmanproductions.trafficcontrol.scanner;

import java.util.HashMap;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.util.ImmersiveRailroadingHelper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ScannerThread extends Thread
{
	public static HashMap<World, ScannerThread> ThreadsByWorld = new HashMap<World, ScannerThread>();
	
	private World _world;
	private boolean _stop = false;
	private ScannerData _data;
	public ScannerThread(World world)
	{
		super();
		_world = world;
		_data = (ScannerData)_world.loadData(ScannerData.class, "RS_scanner_data");
		if (_data == null)
		{
			_data = new ScannerData();
			_world.setData(_data.mapName, _data);
		}
		setName("Traffic Control Train Scanner");
	}
	
	public void requestStop()
	{
		_stop = true;
	}
	
	public <T extends TileEntity & IScannerSubscriber> void subscribe(T subscriber)
	{
		_data.addSubscriber(subscriber.getPos());
	}
	
	@Override
	public void run() {
		while(true)
		{
			if (_stop)
			{
				return;
			}
			
			for(BlockPos pos : _data.getSubscribers())
			{
				if (!_world.isBlockLoaded(pos))
				{
					continue;
				}
				
				TileEntity tileEntity = _world.getTileEntity(pos);
				if (!(tileEntity instanceof IScannerSubscriber))
				{
					_data.removeSubscriber(pos);
					continue;
				}
				
				IScannerSubscriber subscriber = (IScannerSubscriber)tileEntity;
				boolean hasCanceled = false;
				for(ScanRequest req : subscriber.getScanRequests())
				{
					if (_stop)
					{
						return;
					}
					
					ScanCompleteData data = performScan(req);
					
					if (_stop)
					{
						return;
					}
					
					if (!_world.isBlockLoaded(pos))
					{
						break;
					}
					
					subscriber.onScanComplete(data);
					
					if (!data.getContinueScanningForTileEntity())
					{
						hasCanceled = true;
						break;
					}
				}
				
				if (!hasCanceled)
				{
					subscriber.onScanRequestsCompleted();
				}
			}
		}
	}
	
	private ScanCompleteData performScan(ScanRequest req)
	{
		Vec3d currentPosition = new Vec3d(req.getStartingPos().getX(), req.getStartingPos().getY(), req.getStartingPos().getZ());
		Vec3d motion = new Vec3d(req.getStartDirection().getDirectionVec());
		
		boolean foundTrain = false;
		boolean trainMovingTowardsDest = false;
		for(int i = 0; i < Config.borderTimeout; i++)
		{
			Vec3d nextPosition = ImmersiveRailroadingHelper.getNextPosition(currentPosition, motion, _world);
			
			if (nextPosition.equals(currentPosition))
			{
				return new ScanCompleteData(req, true, false, false);
			}
			
			motion = new Vec3d(nextPosition.x - currentPosition.x,
								nextPosition.y - currentPosition.y,
								nextPosition.z - currentPosition.z);
			
			currentPosition = nextPosition;
			
			Tuple<Boolean, Boolean> foundTrainData = checkPosition(req, currentPosition, motion);
			foundTrain = foundTrain || foundTrainData.getFirst();
			trainMovingTowardsDest = trainMovingTowardsDest || foundTrainData.getSecond();
			
			if (req.getEndingPositions().contains(new BlockPos(currentPosition.x, currentPosition.y, currentPosition.z)))
			{
				return new ScanCompleteData(req, false, foundTrain, trainMovingTowardsDest);
			}
		}
		
		return new ScanCompleteData(req, true, false, false);
	}
	
	private Tuple<Boolean, Boolean> checkPosition(ScanRequest req, Vec3d position, Vec3d motion)
	{
		Tuple<UUID, Vec3d> moveableRollingStockNearby = ImmersiveRailroadingHelper.getStockNearby(position, _world);
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
