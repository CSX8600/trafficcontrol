package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightFourTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightHorizontalTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightFourRenderer;

import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightHorizontalRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrafficLightHorizontal extends BlockBaseTrafficLight
{
    public BlockTrafficLightHorizontal()
    {
        super("traffic_light_horizontal");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TrafficLightHorizontalTileEntity.class, new TrafficLightHorizontalRenderer());
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TrafficLightHorizontalTileEntity();
    }

    @Override
    protected BaseItemTrafficLightFrame getItemVersionOfBlock()
    {
        return ModItems.traffic_light_horizontal_frame;
    }
}
