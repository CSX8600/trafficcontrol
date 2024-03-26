package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModSounds;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateGate;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.util.ILoopableSoundTileEntity;
import com.clussmanproductions.trafficcontrol.util.LoopableTileEntitySound;
import com.clussmanproductions.trafficcontrol.util.NBTUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrossingGateGateTileEntity extends SyncableTileEntity implements ITickable, ILoopableSoundTileEntity {
	private float gateRotation = -60;
	private float gateDelay = 0;
	private EnumStatuses status = EnumStatuses.Open;
	private BlockLampBase.EnumState flashState = EnumState.Off;
	private boolean soundPlaying = false;
	private float crossingGateLength = 4;
	private float upperRotationLimit = 60;
	private float lowerRotationLimit = 0;
	private float delay = 4;
	private float lightStartOffset = 1;
	private GateLightCount gateLightCount = GateLightCount.ThreeLights;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setFloat("gateRotation", gateRotation);
		compound.setFloat("gateDelay", gateDelay);
		compound.setInteger("status", getCodeFromEnum(status));
		compound.setInteger("flashState", flashState.getID());
		compound.setFloat("length", crossingGateLength);
		compound.setFloat("upperRotation", upperRotationLimit);
		compound.setFloat("lowerRotation", lowerRotationLimit);
		compound.setFloat("delay", delay);
		compound.setFloat("lightStartOffset", lightStartOffset);
		compound.setInteger("gateLightCount", gateLightCount.getID());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		gateRotation = compound.getFloat("gateRotation");
		gateDelay = compound.getFloat("gateDelay");
		status = getStatusFromCode(compound.getInteger("status"));
		flashState = BlockLampBase.EnumState.getStateByID(compound.getInteger("flashState"));
		crossingGateLength = NBTUtils.getFloatOrDefault(compound, "length", 4);
		upperRotationLimit = NBTUtils.getFloatOrDefault(compound, "upperRotation", 60);
		lowerRotationLimit = NBTUtils.getFloatOrDefault(compound, "lowerRotation", 0);
		delay = NBTUtils.getFloatOrDefault(compound, "delay", 4);
		lightStartOffset = NBTUtils.getFloatOrDefault(compound, "lightStartOffset", 1);
		gateLightCount = GateLightCount.getByID(NBTUtils.getIntOrDefault(compound, "gateLightCount", GateLightCount.ThreeLights.getID()));
	}
		
	public float getFacingRotation()
	{
		IBlockState blockState = world.getBlockState(getPos());
		
		if (!(blockState.getBlock() instanceof BlockCrossingGateGate))
		{
			return 0;
		}
		
		return -blockState.getValue(BlockCrossingGateGate.ROTATION).floatValue() * 22.5F;
	}
	
	public float getGateRotation()
	{
		return gateRotation;
	}
		
	private int getCodeFromEnum(EnumStatuses status)
	{
		switch(status)
		{
			case Closed:
				return 0;
			case Closing:
				return 1;
			case Open:
				return 2;
			case Opening:
				return 3;
			default:
				return -1;
		}
	}
	
	private EnumStatuses getStatusFromCode(int code)
	{
		switch(code)
		{
			case 0:
				return EnumStatuses.Closed;
			case 1:
				return EnumStatuses.Closing;
			case 2:
				return EnumStatuses.Open;
			case 3:
				return EnumStatuses.Opening;
			default:
				return null;
		}
	}
	
	public enum EnumStatuses
	{
		Open,
		Closing,
		Closed,
		Opening
	}
	
	public enum GateLightCount
	{
		ThreeLights(0),
		OneLight(1);
		
		private int id;
		public int getID()
		{
			return id;
		}
		
		private GateLightCount(int id)
		{
			this.id = id;
		}
		
		public static GateLightCount getByID(int id)
		{
			for(GateLightCount count : GateLightCount.values())
			{
				if (count.getID() == id)
				{
					return count;
				}
			}
			
			return null;
		}
	}

	@Override
	public void update() {
		switch(status)
		{
			case Closing:
				if (gateDelay <= (delay * 20))
				{
					gateDelay++;
					
					if (!world.isRemote)
					{
						markDirty();
					}
					
					return;
				}
				
				if (gateRotation >= -lowerRotationLimit)
				{
					status = EnumStatuses.Closed;
					if (!world.isRemote)
					{
						markDirty();
					}
					return;
				}
				
				if (world.isRemote)
				{
					handlePlaySound();
				}
				
				gateRotation += 0.5F;
				break;
			case Opening:
				if (gateRotation <= -upperRotationLimit)
				{
					gateDelay = 0;
					status = EnumStatuses.Open;
					if (!world.isRemote)
					{
						markDirty();
					}
				}
				
				if (world.isRemote)
				{
					handlePlaySound();
				}
				
				gateRotation -= 0.5F;
				break;
			case Open:
			case Closed:
				float idealAngle = (status == EnumStatuses.Open) ? -upperRotationLimit : -lowerRotationLimit;
				if (gateRotation > idealAngle)
				{
					gateRotation -= 0.5F;
				}
				else if (gateRotation < idealAngle)
				{
					gateRotation += 0.5F;
				}
				
				if (world.isRemote)
				{
					soundPlaying = false;
				}
				break;
			default:
				return;
		}
	}
	
	private void sendUpdates(Boolean markDirty)
	{
		if (markDirty)
		{
			markDirty();
		}
		
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setFloat("gateRotation", gateRotation);
		nbt.setFloat("gateDelay", gateDelay);
		nbt.setInteger("status", getCodeFromEnum(status));
		nbt.setInteger("flashState", flashState.getID());
		nbt.setFloat("length", crossingGateLength);
		nbt.setFloat("upperRotation", upperRotationLimit);
		nbt.setFloat("lowerRotation", lowerRotationLimit);
		nbt.setFloat("delay", delay);
		nbt.setFloat("lightStartOffset", lightStartOffset);
		nbt.setInteger("gateLightCount", gateLightCount.getID());
		
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		gateRotation = tag.getFloat("gateRotation");
		gateDelay = tag.getFloat("gateDelay");
		status = getStatusFromCode(tag.getInteger("status"));
		flashState = BlockLampBase.EnumState.getStateByID(tag.getInteger("flashState"));
		crossingGateLength = tag.getFloat("length");
		upperRotationLimit = tag.getFloat("upperRotation");
		lowerRotationLimit = tag.getFloat("lowerRotation");
		delay = tag.getFloat("delay");
		lightStartOffset = tag.getFloat("lightStartOffset");
		gateLightCount = GateLightCount.getByID(tag.getInteger("gateLightCount"));
	}
	
	@SideOnly(Side.CLIENT)
	public void handlePlaySound()
	{
		if (!soundPlaying)
		{
			LoopableTileEntitySound gateSound = new LoopableTileEntitySound(ModSounds.gateEvent, this, pos, 0.3f, 1);
			
			Minecraft.getMinecraft().getSoundHandler().playSound(gateSound);
			
			soundPlaying = true;
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
		handleUpdateTag(nbt);
	}

	@Override
	public boolean isDonePlayingSound() {
		return !soundPlaying;
	}

	public EnumStatuses getStatus()
	{
		return status;
	}

	public void setStatus(EnumStatuses status)
	{
		if ((status == EnumStatuses.Opening && this.status == EnumStatuses.Open) ||
				(status == EnumStatuses.Closing && this.status == EnumStatuses.Closed))
		{
			return;
		}
		
		this.status = status;
		sendUpdates(true);
	}

	public float getCrossingGateLength()
	{
		return crossingGateLength;
	}
	
	public void setCrossingGateLength(float length)
	{
		boolean shouldMarkDirty = length != crossingGateLength;
		
		crossingGateLength = length;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public float getUpperRotationLimit() {
		return upperRotationLimit;
	}
	
	public void setUpperRotationLimit(float upperRotationLimit) {
		boolean shouldMarkDirty = upperRotationLimit != this.upperRotationLimit;
		
		this.upperRotationLimit = upperRotationLimit;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public float getLowerRotationLimit() {
		return lowerRotationLimit;
	}
	
	public void setLowerRotationLimit(float lowerRotationLimit) {
		boolean shouldMarkDirty = lowerRotationLimit != this.lowerRotationLimit;
		
		this.lowerRotationLimit = lowerRotationLimit;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public float getDelay() {
		return delay;
	}
	
	public void setDelay(float delay) {
		boolean shouldMarkDirty = delay != this.delay;
		
		this.delay = delay;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public BlockLampBase.EnumState getFlashState()
	{
		return flashState;
	}
	
	public void setFlashState(BlockLampBase.EnumState state)
	{
		boolean shouldMarkDirty = state != flashState;
		flashState = state;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public float getLightStartOffset()
	{
		return lightStartOffset;
	}
	
	public void setLightStartOffset(float lightStartOffset)
	{
		boolean shouldMarkDirty = lightStartOffset != this.lightStartOffset;
		this.lightStartOffset = lightStartOffset;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	public GateLightCount getGateLightCount()
	{
		return gateLightCount;
	}
	
	public void setGateLightCount(GateLightCount count)
	{
		boolean shouldMarkDirty = gateLightCount != count;
		gateLightCount = count;
		
		if (shouldMarkDirty)
		{
			sendUpdates(true);
		}
	}
	
	@Override
	public void onChunkUnload() {
		soundPlaying = false;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public NBTTagCompound getClientToServerUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("length", crossingGateLength);
		tag.setFloat("upperRotation", upperRotationLimit);
		tag.setFloat("lowerRotation", lowerRotationLimit);
		tag.setFloat("delay", delay);
		tag.setFloat("lightStartOffset", lightStartOffset);
		tag.setInteger("gateLightCount", gateLightCount.getID());
		return tag;
	}

	@Override
	public void handleClientToServerUpdateTag(NBTTagCompound compound) {
		setCrossingGateLength(compound.getFloat("length"));
		setUpperRotationLimit(compound.getFloat("upperRotation"));
		setLowerRotationLimit(compound.getFloat("lowerRotation"));
		setDelay(compound.getFloat("delay"));
		setLightStartOffset(compound.getFloat("lightStartOffset"));
		setGateLightCount(GateLightCount.getByID(compound.getInteger("gateLightCount")));
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return newSate.getBlock() != ModBlocks.crossing_gate_gate;
	}
}
