package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLightSource;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightDouble;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StreetLightDoubleTileEntity extends TileEntity  {
	private boolean powered;
	private int[] blockPos1 = new int[] { 0, -1, 0 };
	private int[] blockPos2 = new int[] { 0, -1, 0 };
	private int[] blockPos3 = new int[] { 0, -1, 0 };
	private int[] blockPos4 = new int[] { 0, -1, 0 };
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		blockPos1 = compound.getIntArray("blockPos1");
		blockPos2 = compound.getIntArray("blockPos2");
		blockPos3 = compound.getIntArray("blockPos3");
		blockPos4 = compound.getIntArray("blockPos4");		
		powered = compound.getBoolean("powered");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setIntArray("blockPos1", blockPos1);
		compound.setIntArray("blockPos2", blockPos2);
		compound.setIntArray("blockPos3", blockPos3);
		compound.setIntArray("blockPos4", blockPos4);
		compound.setBoolean("powered", powered);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote || isInvalid())
		{
			return;
		}
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == ModBlocks.street_light_double &&
			!powered)
		{
			addLightSources();
		}
	}
	
	private void setBlockPosArray(BlockPos pos)
	{
		if (blockPos1[1] == -1)
		{
			blockPos1 = new int[] { pos.getX(), pos.getY(), pos.getZ() };
		}
		else if (blockPos2[1] == -1)
		{
			blockPos2 = new int[] { pos.getX(), pos.getY(), pos.getZ() };
		}
		else if (blockPos3[1] == -1)
		{
			blockPos3 = new int[] { pos.getX(), pos.getY(), pos.getZ() };
		}
		else
		{
			blockPos4 = new int[] { pos.getX(), pos.getY(), pos.getZ() };
		}
	}

	private void tryPlaceLightSource(BlockPos pos)
	{
		IBlockState proposedBlockState = world.getBlockState(pos);
		
		if (proposedBlockState.getBlock() != Blocks.AIR)
		{
			pos = pos.up();
			proposedBlockState = world.getBlockState(pos);
			
			if (proposedBlockState.getBlock() != Blocks.AIR)
			{
				proposedBlockState = null;
			}
		}
		
		if (proposedBlockState != null)
		{
			world.setBlockState(pos, ModBlocks.light_source.getDefaultState());
			setBlockPosArray(pos);
		}
	}

	public void removeLightSources()
	{
		BlockPos pos1 = getBlockPos(1);
		if (pos1 != null)
		{
			IBlockState state1 = world.getBlockState(pos1);
			if (state1.getBlock() instanceof BlockLightSource)
			{
				world.setBlockState(pos1, Blocks.AIR.getDefaultState());
			}
		}
		
		BlockPos pos2 = getBlockPos(2);
		if (pos2 != null)
		{
			IBlockState state2 = world.getBlockState(pos2);
			if (state2.getBlock() instanceof BlockLightSource)
			{
				world.setBlockState(pos2, Blocks.AIR.getDefaultState());
			}
		}
		
		BlockPos pos3 = getBlockPos(3);
		if (pos3 != null)
		{
			IBlockState state3 = world.getBlockState(pos3);
			if (state3.getBlock() instanceof BlockLightSource)
			{
				world.setBlockState(pos3, Blocks.AIR.getDefaultState());
			}
		}
		
		BlockPos pos4 = getBlockPos(4);
		
		if (pos4 != null)
		{
			IBlockState state4 = world.getBlockState(pos4);
			if (state4.getBlock() instanceof BlockLightSource)
			{
				world.setBlockState(pos4, Blocks.AIR.getDefaultState());
			}
		}
	}

	private BlockPos getBlockPos(int index)
	{
		switch(index)
		{
			case 1:
				if (blockPos1[1] != -1)
				{
					return new BlockPos(blockPos1[0], blockPos1[1], blockPos1[2]);
				}
			case 2:
				if (blockPos2[1] != -1)
				{
					return new BlockPos(blockPos2[0], blockPos2[1], blockPos2[2]);
				}
			case 3:
				if (blockPos3[1] != -1)
				{
					return new BlockPos(blockPos3[0], blockPos3[1], blockPos3[2]);
				}
			case 4:
				if (blockPos4[1] != -1)
				{
					return new BlockPos(blockPos4[0], blockPos4[1], blockPos4[2]);
				}
			default:
				return null;
		}
	}

	public void addLightSources()
	{
		BlockPos pos = getPos();
		
		pos = pos.north(2).west(2);
		tryPlaceLightSource(pos);
		pos = pos.east(4);
		tryPlaceLightSource(pos);
		pos = pos.south(4);
		tryPlaceLightSource(pos);
		pos = pos.west(4);
		tryPlaceLightSource(pos);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		if (newSate.getBlock() instanceof BlockStreetLightDouble)
		{
			return false;
		}
		
		return super.shouldRefresh(world, pos, oldState, newSate);
	}

	public boolean isPowered()
	{
		return powered;
	}
	
	public void setPowered(boolean powered)
	{
		this.powered = powered;
		markDirty();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setBoolean("powered", powered);
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		powered = tag.getBoolean("powered");
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos).expand(2, 5, 2).expand(-2, 0, -2);
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}}
