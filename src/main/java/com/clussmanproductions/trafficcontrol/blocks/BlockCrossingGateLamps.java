package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy.GUI_IDs;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsPoleBasedTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.CrossingGateLampsRenderer;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockCrossingGateLamps extends BlockLampBase {

	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	
	@Override
	public void initModel() {
		super.initModel();
		
		ClientRegistry.bindTileEntitySpecialRenderer(CrossingLampsPoleBasedTileEntity.class, new CrossingGateLampsRenderer());
	}	
	
	@Override
	protected String getLampRegistryName() {
		return "crossing_gate_lamps";
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		int i = CustomAngleCalculator.getRotationForYaw(placer.rotationYaw);
		return getDefaultState().withProperty(ROTATION, i);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	public IProperty<?> getRotationalProperty() {
		return ROTATION;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new CrossingLampsPoleBasedTileEntity();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() == ModItems.crossing_relay_tuner)
		{
			return false;
		}
		
		playerIn.openGui(ModTrafficControl.instance, GUI_IDs.CROSSING_GATE_LAMPS, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
