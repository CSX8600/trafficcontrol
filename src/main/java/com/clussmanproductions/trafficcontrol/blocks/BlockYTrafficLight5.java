package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLight5TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.YTrafficLight5TileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLight5Renderer;
import com.clussmanproductions.trafficcontrol.tileentity.render.YTrafficLight5Renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockYTrafficLight5 extends BlockBaseTrafficLight {

	public BlockYTrafficLight5() {
		super("ytraffic_light_5");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(YTrafficLight5TileEntity.class, new YTrafficLight5Renderer());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new YTrafficLight5TileEntity();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
		{
			super.breakBlock(worldIn, pos, state);
			return;
		}
		
		if (worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.ytraffic_light_5_upper)
		{
			worldIn.setBlockToAir(pos.up());
		}
		
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	protected BaseItemTrafficLightFrame getItemVersionOfBlock() {
		return ModItems.ytraffic_light_5_frame;
	}
}
