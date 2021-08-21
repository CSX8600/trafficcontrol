package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightFourTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightOneTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTwoTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightFourRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightOneRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightTwoRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLightFour extends BlockBaseTrafficLight
{
	public BlockTrafficLightFour()
	{
		super("traffic_light_4");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLightFourTileEntity.class, new TrafficLightFourRenderer());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TrafficLightFourTileEntity();
	}
	
	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock()
	{
		return ModItems.traffic_light_four_frame;
	}
}
