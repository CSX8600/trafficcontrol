package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.HashSet;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class PedestrianButtonTileEntity extends TileEntity {
	private HashSet<BlockPos> pairedBoxes = new HashSet<>();
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		pairedBoxes = new HashSet<>();
		
		NBTTagList pairList = compound.getTagList("pairedBoxes", NBT.TAG_LONG);
		for(NBTBase tagBase : pairList)
		{
			NBTTagLong tag = (NBTTagLong)tagBase;
			pairedBoxes.add(BlockPos.fromLong(tag.getLong()));
		}
		
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList pairList = new NBTTagList();
		for(BlockPos pos : pairedBoxes)
		{
			pairList.appendTag(new NBTTagLong(pos.toLong()));
		}
		
		compound.setTag("pairedBoxes", pairList);
		return super.writeToNBT(compound);
	}
	
	public void addPairedBox(BlockPos pos)
	{
		pairedBoxes.add(pos);
		markDirty();
	}
	
	public void removePairedBox(BlockPos pos)
	{
		pairedBoxes.remove(pos);
		markDirty();
	}
	
	public ImmutableList<BlockPos> getPairedBoxes()
	{
		return ImmutableList.copyOf(pairedBoxes);
	}

	public void onBreak(World world, boolean isNorthSouth)
	{
		for(BlockPos pos : pairedBoxes)
		{
			TileEntity prelimCtrlr = world.getTileEntity(pos);
			if (prelimCtrlr == null || !(prelimCtrlr instanceof TrafficLightControlBoxTileEntity))
			{
				continue;
			}
			
			TrafficLightControlBoxTileEntity ctrlr = (TrafficLightControlBoxTileEntity)prelimCtrlr;
			if (isNorthSouth)
			{
				ctrlr.addOrRemoveWestEastPedButton(getPos());
			}
			else
			{
				ctrlr.addOrRemoveNorthSouthPedButton(getPos());
			}
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return newSate.getBlock() != ModBlocks.pedestrian_button;
	}
}
