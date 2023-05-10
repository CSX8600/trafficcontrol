package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockType3BarrierBase;
import com.clussmanproductions.trafficcontrol.signs.Sign;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Type3BarrierTileEntity extends SyncableTileEntity {
	private boolean renderSign;
	private SignType signType = SignType.RoadClosed;
	private boolean renderThisSign;
	private int thisSignTypeLegacy = -1;
	private int thisSignVariantLegacy = -1;
	private UUID thisSignID;
	private ArrayList<String> thisSignTextLines;
	
	public boolean getRenderSign() {
		return renderSign;
	}
	
	public SignType getSignType()
	{
		return signType;
	}

	public boolean getRenderThisSign()
	{
		return renderThisSign;
	}
	
	public int getThisSignType()
	{
		return thisSignTypeLegacy;
	}
	
	public int getThisSignVariant()
	{
		return thisSignVariantLegacy;
	}
	
	public UUID getThisSignID()
	{
		return thisSignID;
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
	
	public void setRenderThisSign(boolean renderThisSign)
	{
		boolean shouldMarkDirty = renderThisSign != this.renderThisSign;
		
		this.renderThisSign = renderThisSign;
		
		if (shouldMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}
	
	public void setThisSignTypeLegacy(int thisSignType)
	{
		boolean shouldMarkDirty = thisSignType != this.thisSignTypeLegacy;
		
		this.thisSignTypeLegacy = thisSignType;
		
		if (shouldMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}
	
	public void setThisSignVariantLegacy(int thisSignVariant)
	{		
		boolean shouldMarkDirty = thisSignVariant != this.thisSignVariantLegacy;
		
		this.thisSignVariantLegacy = thisSignVariant;
		
		if (shouldMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}
	
	public void setThisSignID(UUID thisSignID)
	{
		boolean shouldMarkDirty = (thisSignID == null && this.thisSignID != null) || 
								  (thisSignID != null && this.thisSignID == null) ||
								  !thisSignID.equals(this.thisSignID);
		
		this.thisSignID = thisSignID;
		
		if (shouldMarkDirty)
		{
			markDirty();
			
			if (world != null)
			{
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			}
		}
	}
	
	public Sign getThisSign()
	{
		Sign sign = null;
		if (thisSignTypeLegacy >= 0)
		{
			sign = ModTrafficControl.instance.signRepo.getSignByTypeVariant(SignTileEntity.getSignTypeName(thisSignTypeLegacy), thisSignVariantLegacy);
		}
		else if (thisSignID != null)
		{
			sign = ModTrafficControl.instance.signRepo.getSignByID(thisSignID);
		}
		
		if (sign == null)
		{
			return ModTrafficControl.instance.signRepo.getSignByID(Sign.DEFAULT_ERROR_SIGN);
		}
		
		return sign;
	}
	
	public String getThisSignTextLine(int index)
	{
		if (thisSignTextLines == null || index >= thisSignTextLines.size())
		{
			return null;
		}
		
		return thisSignTextLines.get(index);
	}
	
	public void setThisSignTextLine(int index, String text)
	{
		if (thisSignTextLines == null)
		{
			thisSignTextLines = new ArrayList<String>();
		}
		
		if (thisSignTextLines.size() <= index)
		{
			for(int i = thisSignTextLines.size(); i <= index; i++)
			{
				thisSignTextLines.add(null);
			}
		}
		
		thisSignTextLines.set(index, text);
	}
	
	public void clearThisSignTextLines()
	{
		if (thisSignTextLines != null)
		{
			thisSignTextLines.clear();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setRenderSign(compound.getBoolean("renderSign"));
		setSignType(SignType.getByIndex(compound.getInteger("signType")));
		setRenderThisSign(compound.getBoolean("renderThisSign"));
		setThisSignTypeLegacy(compound.getInteger("thisSignType"));
		setThisSignVariantLegacy(compound.getInteger("thisSignVariant"));
		
		if (compound.hasKey("thisSignID"))
		{
			setThisSignID(NBTUtil.getUUIDFromTag(compound.getCompoundTag("thisSignID")));
		}
		
		if (thisSignTextLines != null)
		{
			thisSignTextLines.clear();
		}
		
		if (compound.hasKey("text0"))
		{
			thisSignTextLines = new ArrayList<>();
			int i = 0;
			while(compound.hasKey("text" + i))
			{
				thisSignTextLines.add(compound.getString("text" + i));
				i++;
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("renderSign", getRenderSign());
		compound.setInteger("signType", signType.index);
		compound.setBoolean("renderThisSign", getRenderThisSign());
		compound.setInteger("thisSignType", getThisSignType());
		compound.setInteger("thisSignVariant", getThisSignVariant());
		if (getThisSignID() != null)
		{
			compound.setTag("thisSignID", NBTUtil.createUUIDTag(getThisSignID()));
		}
		
		if (thisSignTextLines != null)
		{
			for(int i = 0; i < thisSignTextLines.size(); i++)
			{
				compound.setString("text" + i, thisSignTextLines.get(i));
			}
		}
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setBoolean("renderSign", renderSign);
		nbt.setInteger("signType", signType.index);
		nbt.setBoolean("renderThisSign", getRenderThisSign());
		nbt.setInteger("thisSignType", getThisSignType());
		nbt.setInteger("thisSignVariant", getThisSignVariant());
		if (getThisSignID() != null)
		{
			nbt.setTag("thisSignID", NBTUtil.createUUIDTag(getThisSignID()));
		}
		
		if (thisSignTextLines != null)
		{
			for(int i = 0; i < thisSignTextLines.size(); i++)
			{
				nbt.setString("text" + i, thisSignTextLines.get(i));
			}
		}
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		setRenderSign(tag.getBoolean("renderSign"));
		setSignType(SignType.getByIndex(tag.getInteger("signType")));
		setRenderThisSign(tag.getBoolean("renderThisSign"));
		setThisSignTypeLegacy(tag.getInteger("thisSignType"));
		setThisSignVariantLegacy(tag.getInteger("thisSignVariant"));
		if (tag.hasKey("thisSignID"))
		{
			setThisSignID(NBTUtil.getUUIDFromTag(tag.getCompoundTag("thisSignID")));
		}
		
		if (thisSignTextLines != null)
		{
			thisSignTextLines.clear();
		}
		
		if (tag.hasKey("text0"))
		{
			thisSignTextLines = new ArrayList<>();
			int i = 0;
			while(tag.hasKey("text" + i))
			{
				thisSignTextLines.add(tag.getString("text" + i));
				i++;
			}
		}
		
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
		BlockPos workingPos = findFurthestLeft().getPos();
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
		LaneClosed(1),
		RoadClosedThruTraffic(2);
		
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

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}
}
