package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.OTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.YTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.OTrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.YTrafficLightRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOTrafficLight extends BlockBaseTrafficLight {

	public BlockOTrafficLight()
	{
		super("otraffic_light");
		
		
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(OTrafficLightTileEntity.class, new OTrafficLightRenderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new OTrafficLightTileEntity();
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.otraffic_light_frame;
	}

}
