package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.blocks.BlockType3BarrierBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Type3BarrierTileEntity extends SyncableTileEntity {
	private boolean renderSign;
	private SignType signType = SignType.RoadClosed;
	
	public boolean getRenderSign() {
		return renderSign;
	}
	
	public SignType getSignType()
	{
		return signType;
	}

	public void setRenderSign(boolean renderSign) {
		boolean shouldMarkDirty = renderSign != this.renderSign;
		
		this.renderSign = renderSign;
		
		if (shouldMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}

	public void setSignType(SignType signType)
	{
		boolean doMarkDirty = signType != this.signType;
		
		this.signType = signType;
		
		if (doMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setRenderSign(compound.getBoolean("renderSign"));
		setSignType(SignType.getByIndex(compound.getInteger("signType")));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("renderSign", getRenderSign());
		compound.setInteger("signType", signType.index);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setBoolean("renderSign", renderSign);
		nbt.setInteger("signType", signType.index);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		setRenderSign(tag.getBoolean("renderSign"));
		setSignType(SignType.getByIndex(tag.getInteger("signType")));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getClientToServerUpdateTag() {
		return getUpdateTag();
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound compound) {
		handleUpdateTag(compound);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return !(newSate.getBlock() instanceof BlockType3BarrierBase);
	}

	public Type3BarrierTileEntity findFurthestLeft()
	{
		BlockPos workingPos = pos.toImmutable();
		IBlockState currentState = world.getBlockState(workingPos).getActualState(world, workingPos);
		Block currentStateBlockInstance = ((BlockType3BarrierBase)currentState.getBlock()).getBlockInstance();
		EnumFacing facing = currentState.getValue(BlockType3BarrierBase.FACING);
		
		while(currentState.getBlock() == currentStateBlockInstance && !currentState.getValue(BlockType3BarrierBase.ISFURTHESTLEFT))
		{
			workingPos = workingPos.offset(facing.rotateYCCW());
			currentState = world.getBlockState(workingPos).getActualState(world, workingPos);
		}
		
		return (Type3BarrierTileEntity)world.getTileEntity(workingPos);
	}

	public void syncConnectedBarriers(boolean doClientToServerSync)
	{
		BlockPos workingPos = pos.toImmutable();
		IBlockState currentState = world.getBlockState(workingPos).getActualState(world, workingPos);
		Block currentStateBlockInstance = ((BlockType3BarrierBase)currentState.getBlock()).getBlockInstance();
		EnumFacing facing = currentState.getValue(BlockType3BarrierBase.FACING);
		
		while(currentState.getBlock() == currentStateBlockInstance && !currentState.getValue(BlockType3BarrierBase.ISFURTHESTRIGHT))
		{
			TileEntity teInPos = world.getTileEntity(workingPos);
			if (teInPos instanceof Type3BarrierTileEntity)
			{
				Type3BarrierTileEntity barrierTE = (Type3BarrierTileEntity)teInPos;
				barrierTE.setRenderSign(getRenderSign());
				barrierTE.setSignType(getSignType());
				
				if (doClientToServerSync)
				{
					barrierTE.performClientToServerSync();
				}
			}
			
			workingPos = workingPos.offset(facing.rotateY());
			currentState = world.getBlockState(workingPos).getActualState(world, workingPos);
		}
		
		// Do one last time since the loop will stop on the last one
		TileEntity teInPos = world.getTileEntity(workingPos);
		if (teInPos instanceof Type3BarrierTileEntity)
		{
			Type3BarrierTileEntity barrierTE = (Type3BarrierTileEntity)teInPos;
			barrierTE.setRenderSign(getRenderSign());
			barrierTE.setSignType(getSignType());
			
			if (doClientToServerSync)
			{
				barrierTE.performClientToServerSync();
			}
		}
	}

	public void nextSignType()
	{
		int nextIndex = getSignType().index + 1;
		if (nextIndex > SignType.getMaxIndex())
		{
			nextIndex = 0;
		}
		
		setSignType(SignType.getByIndex(nextIndex));
	}
	
	public void prevSignType()
	{
		int nextIndex = getSignType().index - 1;
		if (nextIndex < 0)
		{
			nextIndex = SignType.getMaxIndex();
		}
		
		setSignType(SignType.getByIndex(nextIndex));
	}
	
	public enum SignType
	{
		RoadClosed(0),
		LaneClosed(1);
		
		private int index;
		private SignType(int index)
		{
			this.index = index;
		}
		
		public int getIndex()
		{
			return index;
		}
		
		public static SignType getByIndex(int index)
		{
			for(SignType type : SignType.values())
			{
				if (type.index == index)
				{
					return type;
				}
			}
			
			return null;
		}
	
		public static int getMaxIndex()
		{
			int maxIndex = -1;
			for(SignType type : SignType.values())
			{
				if (type.index > maxIndex)
				{
					maxIndex = type.index;
				}
			}
			
			return maxIndex;
		}
	}
}
