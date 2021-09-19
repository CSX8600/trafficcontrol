package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight4TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight1TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight2TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight4Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight1Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight2Renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLight4 extends BlockBaseTrafficLight
{
	public BlockTrafficLight4()
	{
		super("traffic_light_4");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLight4TileEntity.class, new TrafficLight4Renderer());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TrafficLight4TileEntity();
	}
	
	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock()
	{
		return ModItems.traffic_light_4_frame;
	}
}
