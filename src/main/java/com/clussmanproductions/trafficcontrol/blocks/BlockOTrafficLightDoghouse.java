package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.OTrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.YTrafficLightDoghouseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.OTrafficLightDoghouseRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightDoghouseRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.YTrafficLightDoghouseRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOTrafficLightDoghouse extends BlockBaseTrafficLight {

	public BlockOTrafficLightDoghouse()
	{
		super("otraffic_light_doghouse");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(OTrafficLightDoghouseTileEntity.class, new OTrafficLightDoghouseRenderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new OTrafficLightDoghouseTileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.otraffic_light_doghouse_frame;
	}
}
