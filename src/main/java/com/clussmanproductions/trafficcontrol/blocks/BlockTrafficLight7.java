package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight7TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight1TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight2TileEntity;

import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight4Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight1Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight7Renderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLight7 extends BlockBaseTrafficLight {

	public BlockTrafficLight7()
	{
		super("traffic_light_7");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLight7TileEntity.class, new TrafficLight7Renderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TrafficLight7TileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.traffic_light_7_frame;
	}
}
