package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.HashMap;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class BaseTrafficLightTileEntity extends TileEntity implements ITickable {

	private final int BULB_COUNT;
	HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>();
	HashMap<Integer, Boolean> activeBySlot = new HashMap<Integer, Boolean>();
	HashMap<Integer, Boolean> flashBySlot = new HashMap<Integer, Boolean>();
	HashMap<Integer, Integer> flashTimeBySlot = new HashMap<Integer, Integer>();
	HashMap<Integer, Boolean> flashCurrent = new HashMap<Integer, Boolean>();
	HashMap<Integer, Boolean> allowFlashBySlot = new HashMap<Integer, Boolean>();
	// Easter egg
	private int isPigAboveDelay;
	private boolean isPigAbove;
	
	public BaseTrafficLightTileEntity(int bulbCount) {
		super();
		BULB_COUNT = bulbCount;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		int[] bulbTypes = new int[BULB_COUNT];
		
		for(int i = 0; i < BULB_COUNT; i++)
		{
			EnumTrafficLightBulbTypes bulbTypeInSlot = getBulbTypeBySlot(i);
			bulbTypes[i] = bulbTypeInSlot != null ? bulbTypeInSlot.getIndex() : -1;
			compound.setBoolean("active" + i, getActiveBySlot(i));
			compound.setBoolean("flash" + i, getFlashBySlot(i));
			compound.setBoolean("allowflash" + i, getAllowFlashBySlot(i));
		}
		
		compound.setIntArray("bulbTypes", bulbTypes);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(BULB_COUNT);
		activeBySlot = new HashMap<Integer, Boolean>(BULB_COUNT);
		
		int[] bulbTypes = compound.getIntArray("bulbTypes");
		
		for(int i = 0; i < bulbTypes.length; i++)
		{
			bulbsBySlot.put(i, EnumTrafficLightBulbTypes.get(bulbTypes[i]));
			activeBySlot.put(i, compound.getBoolean("active" + i));
			flashBySlot.put(i, compound.getBoolean("flash" + i));
			allowFlashBySlot.put(i, compound.hasKey("allowflash" + i) ? compound.getBoolean("allowflash" + i) : true);
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		int[] bulbTypes = new int[BULB_COUNT];
		
		bulbsBySlot.forEach((key, value) ->
		{
			bulbTypes[key] = value != null ? value.getIndex() : -1;
		});
		
		tag.setIntArray("bulbTypes", bulbTypes);
		
		for(int i = 0; i < BULB_COUNT; i++)
		{
			tag.setBoolean("active" + i, getActiveBySlot(i));
			tag.setBoolean("flash" + i, getFlashBySlot(i));
			tag.setBoolean("allowflash" + i, getAllowFlashBySlot(i));
		}
		
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>();
		
		int[] bulbTypes = tag.getIntArray("bulbTypes");
		for(int i = 0; i < bulbTypes.length; i++)
		{
			bulbsBySlot.put(i, EnumTrafficLightBulbTypes.get(bulbTypes[i]));
		}
		
		for(int i = 0; i < BULB_COUNT; i++)
		{
			activeBySlot.put(i, tag.getBoolean("active" + i));
			flashBySlot.put(i, tag.getBoolean("flash" + i));
			allowFlashBySlot.put(i, tag.hasKey("allowflash" + i) ? tag.getBoolean("allowflash" + i) : true);
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public void setBulbsBySlot(HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot)
	{
		this.bulbsBySlot = bulbsBySlot;
		activeBySlot = new HashMap<Integer, Boolean>();
		
		for (int i = 0; i < BULB_COUNT; i++)
		{
			activeBySlot.put(i, false);
		}
		
		markDirty();
	}
	
	public boolean hasBulb(EnumTrafficLightBulbTypes bulbType)
	{
		return bulbsBySlot.containsValue(bulbType);
	}
	
	public void setActive(EnumTrafficLightBulbTypes bulbType, boolean active, boolean flash)
	{
		bulbsBySlot.forEach((slot, type) -> 
		{
			if (type == bulbType)
			{
				activeBySlot.put(slot, active);
				flashBySlot.put(slot, flash);
			}
		});
		
		markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}
	
	public void powerOff()
	{
		for(int i = 0; i < BULB_COUNT; i++)
		{
			activeBySlot.put(i, false);
			flashBySlot.put(i, false);
		}
		
		setActive(EnumTrafficLightBulbTypes.DontCross, true, false);
		
		markDirty();
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
	}
	
	public EnumTrafficLightBulbTypes getBulbTypeBySlot(int slot)
	{
		if (bulbsBySlot.containsKey(slot))
		{
			return bulbsBySlot.get(slot);
		}
		
		return null;
	}

	public boolean getActiveBySlot(int slot)
	{
		if (activeBySlot.containsKey(slot))
		{
			return activeBySlot.get(slot);
		}
		
		return false;
	}
	
	public boolean getFlashBySlot(int slot)
	{
		if (flashBySlot.containsKey(slot))
		{
			return flashBySlot.get(slot);
		}
		
		return false;
	}

	public boolean getFlashCurrentBySlot(int slot)
	{
		if (flashCurrent.containsKey(slot))
		{
			return flashCurrent.get(slot);
		}
		
		return true;
	}

	public void setAllowFlashBySlot(HashMap<Integer, Boolean> allowFlashBySlot)
	{
		this.allowFlashBySlot = new HashMap<>();
		
		for(int i = 0; i < BULB_COUNT; i++)
		{
			if (allowFlashBySlot.containsKey(i))
			{
				this.allowFlashBySlot.put(i, allowFlashBySlot.get(i));
			}
			else
			{
				this.allowFlashBySlot.put(i, true);
			}
		}
		
		markDirty();
	}
	
	public boolean getAllowFlashBySlot(int slot)
	{
		if (allowFlashBySlot.containsKey(slot))
		{
			return allowFlashBySlot.get(slot);
		}
		
		return true;
	}
	
	public boolean anyActive()
	{
		for(int i = 0; i < BULB_COUNT; i++)
		{
			if (getActiveBySlot(i) && (!getFlashBySlot(i) || getFlashCurrentBySlot(i)))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void update() {
		if (!world.isRemote)
		{
			return;
		}
		
		// Easter Egg
		isPigAboveDelay++;
		if (isPigAboveDelay >= 20)
		{
			isPigAbove = world.getEntitiesWithinAABB(EntityPig.class, new AxisAlignedBB(getPos(), getPos().up(2)).expand(0.5, 0, 0.5)).size() > 0;
			isPigAboveDelay = 0;
		}
		
		for (int i = 0; i < BULB_COUNT; i++)
		{
			if (getFlashBySlot(i) && getAllowFlashBySlot(i))
			{
				if (!flashTimeBySlot.containsKey(i))
				{
					flashTimeBySlot.put(i, 0);
				}
				
				if (!flashCurrent.containsKey(i))
				{
					flashCurrent.put(i, false);
				}
				
				flashTimeBySlot.put(i, flashTimeBySlot.get(i) + 1);
				
				if (flashTimeBySlot.get(i) > 20)
				{
					flashCurrent.put(i, !flashCurrent.get(i));
					flashTimeBySlot.put(i, 0);
					world.checkLight(getPos());
				}
			}
			else if (getFlashBySlot(i) && !getAllowFlashBySlot(i))
			{
				flashCurrent.put(i, false);
			}
		}
	}
	
	// Easter egg
	public boolean getIsPigAbove()
	{
		return isPigAbove;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}
}
