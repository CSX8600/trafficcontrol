package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WigWagTileEntity extends TileEntity implements ITickable {
	private int rotation = 0;
	private AnimationMode mode = AnimationMode.SwingPositive;
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return super.writeToNBT(compound);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if (newSate.getBlock() == ModBlocks.wig_wag)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void update() {
		if (!world.isRemote)
		{
			return;
		}
		
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() != ModBlocks.wig_wag)
		{
			return;
		}
		
		boolean active = state.getValue(BlockWigWag.ACTIVE);
		
		if (!active && rotation != 0)
		{
			if (rotation < 0 && mode != AnimationMode.SwingPositive)
			{
				mode = AnimationMode.SwingPositive;
			}
			
			if (rotation > 0 && mode != AnimationMode.SwingNegative)
			{
				mode = AnimationMode.SwingNegative;
			}
		}
		
		if (!active && rotation == 0)
		{
			return;
		}
		
		if (active)
		{
			if (rotation > 30)
			{
				mode = AnimationMode.SwingNegative;
			}
			
			if (rotation < -30)
			{
				mode = AnimationMode.SwingPositive;
			}
		}
		
		switch(mode)
		{
			case SwingNegative:
				rotation -= 4;
				break;
			case SwingPositive:
				rotation += 4;
				break;
		}
	}
	
	private enum AnimationMode
	{
		SwingNegative,
		SwingPositive
	}

	public int getRotation()
	{
		return rotation;
	}
}
