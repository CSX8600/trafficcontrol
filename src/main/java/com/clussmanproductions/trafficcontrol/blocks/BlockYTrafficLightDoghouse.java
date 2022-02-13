package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.YTrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightDoghouseRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.YTrafficLightDoghouseRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockYTrafficLightDoghouse extends BlockBaseTrafficLight {

	public BlockYTrafficLightDoghouse()
	{
		super("ytraffic_light_doghouse");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(YTrafficLightDoghouseTileEntity.class, new YTrafficLightDoghouseRenderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new YTrafficLightDoghouseTileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.ytraffic_light_doghouse_frame;
	}
}
