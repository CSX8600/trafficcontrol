package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight1TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight2TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight1Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight2Renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLight2 extends BlockBaseTrafficLight
{
	public BlockTrafficLight2()
	{
		super("traffic_light_2");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLight2TileEntity.class, new TrafficLight2Renderer());
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		int rotation = state.getValue(BlockBaseTrafficLight.ROTATION);
		if (!CustomAngleCalculator.isCardinal(rotation))
		{
			return super.getBoundingBox(state, source, pos);
		}
		
		if (CustomAngleCalculator.isEast(rotation))
		{
			return new AxisAlignedBB(0.2375, 0.05, 0.1875, 0.65, 1.05, 0.8125);
		}
		else if (CustomAngleCalculator.isNorth(rotation))
		{
			return new AxisAlignedBB(0.1875, 0.05, 0.38, 0.8125, 1.05, 0.7625);
		}
		else if (CustomAngleCalculator.isSouth(rotation))
		{
			return new AxisAlignedBB(0.1875, 0.05, 0.25, 0.8125, 1.05, 0.5625);
		}
		else if (CustomAngleCalculator.isWest(rotation))
		{
			return new AxisAlignedBB(0.4375, 0.05, 0.1875, 0.75, 1.05, 0.8125);
		}
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TrafficLight2TileEntity();
	}
	
	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock()
	{
		return ModItems.traffic_light_2_frame;
	}
}
