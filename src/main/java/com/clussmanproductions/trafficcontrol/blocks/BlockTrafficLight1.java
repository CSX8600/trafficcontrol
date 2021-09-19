package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight1TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight1Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLight1 extends BlockBaseTrafficLight
{
	public BlockTrafficLight1()
	{
		super("traffic_light_1");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLight1TileEntity.class, new TrafficLight1Renderer());
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TrafficLight1TileEntity();
	}
	
	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock()
	{
		return ModItems.traffic_light_1_frame;
	}
}
