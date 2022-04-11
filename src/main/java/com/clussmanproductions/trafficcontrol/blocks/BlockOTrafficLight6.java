package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight6TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.OTrafficLight6TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight1TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.YTrafficLight6TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight2TileEntity;

import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight4Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.OTrafficLight6Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight1Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.YTrafficLight6Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight6Renderer;
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

public class BlockOTrafficLight6 extends BlockBaseTrafficLight {

	public BlockOTrafficLight6()
	{
		super("otraffic_light_6");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(OTrafficLight6TileEntity.class, new OTrafficLight6Renderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new OTrafficLight6TileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.otraffic_light_6_frame;
	}
}
