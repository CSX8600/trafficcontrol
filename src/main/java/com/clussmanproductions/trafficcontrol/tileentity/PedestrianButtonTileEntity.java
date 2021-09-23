package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.HashSet;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
}
