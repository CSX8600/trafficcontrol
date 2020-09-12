package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightDoghouseRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockTrafficLightDoghouse extends BlockBaseTrafficLight {

	public BlockTrafficLightDoghouse()
	{
		super("traffic_light_doghouse");
	}
	
	@Override
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLightDoghouseTileEntity.class, new TrafficLightDoghouseRenderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TrafficLightDoghouseTileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.traffic_light_doghouse_frame;
	}
}
