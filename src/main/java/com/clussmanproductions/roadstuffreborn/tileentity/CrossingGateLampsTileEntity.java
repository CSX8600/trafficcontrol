package com.clussmanproductions.roadstuffreborn.tileentity;

import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateGate;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGateLamps.EnumState;
import com.clussmanproductions.roadstuffreborn.blocks.BlockCrossingGatePole;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity.EnumStatuses;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrossingGateLampsTileEntity extends TileEntity implements ITickable {
	private boolean isFlashing = false;
	private boolean flashOverride = false;
	private int lastFlash = 0;
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		isFlashing = compound.getBoolean("flashing");
		flashOverride = compound.getBoolean("flashoverride");
		lastFlash = compound.getInteger("lastFlash");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = super.writeToNBT(compound);
		nbt.setBoolean("flashing", isFlashing);
		nbt.setBoolean("flashoverride", flashOverride);
		nbt.setInteger("lastFlash", lastFlash);
		return nbt;
	}
	
	public void setFlashOverride(boolean override)
	{
		this.flashOverride = override;
	}
	
	@Override
	public void update() {
		if (world.isRemote)
		{
			return;
		}
		
		IBlockState blockState = world.getBlockState(pos);
		BlockCrossingGateLamps.EnumState state = blockState.getValue(BlockCrossingGateLamps.STATE);
		
		if (!flashOverride) // See if there's a gate in this assembly
		{
			BlockPos lastPos = pos;
			while(true)
			{
				lastPos = lastPos.offset(EnumFacing.DOWN);
				IBlockState stateAtLastPos = world.getBlockState(lastPos);
				Block blockAtLastPos = stateAtLastPos.getBlock();
				
				if (!(blockAtLastPos instanceof BlockCrossingGateGate ||
						blockAtLastPos instanceof BlockCrossingGatePole))
				{
					isFlashing = false;
					break;
				}
				
				if (blockAtLastPos instanceof BlockCrossingGateGate)
				{
					CrossingGateGateTileEntity gateTE = (CrossingGateGateTileEntity)world.getTileEntity(lastPos);
					EnumStatuses status = gateTE.getStatus();
					if (status == EnumStatuses.Closing || status == EnumStatuses.Opening)
					{
						isFlashing = true;
					}
					else
					{
						isFlashing = false;
					}
					
					break;
				}
			}
		}
		
		if (isFlashing || flashOverride)
		{
			if (state == EnumState.Off)
			{
				world.setBlockState(pos, blockState.withProperty(BlockCrossingGateLamps.STATE, EnumState.Flash1));
				world.notifyBlockUpdate(pos, blockState, blockState, 3);
				return;
			}
			
			if (lastFlash >= 20)
			{
				lastFlash = 0;
				if (state == EnumState.Flash1)
				{
					world.setBlockState(pos, blockState.withProperty(BlockCrossingGateLamps.STATE, EnumState.Flash2));
					world.notifyBlockUpdate(pos, blockState, blockState, 3);
				}
				else
				{
					world.setBlockState(pos, blockState.withProperty(BlockCrossingGateLamps.STATE, EnumState.Flash1));
					world.notifyBlockUpdate(pos, blockState, blockState, 3);
				}
			}
			else
			{
				lastFlash++;
			}
		}
		else
		{
			if (state != EnumState.Off)
			{
				world.setBlockState(pos, blockState.withProperty(BlockCrossingGateLamps.STATE, EnumState.Off));
				world.notifyBlockUpdate(pos, blockState, blockState, 3);
			}
		}
	}

	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if (newSate.getBlock() instanceof BlockCrossingGateLamps)
		{
			return false;
		}
		
		return true;
	}
}
