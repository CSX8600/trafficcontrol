package com.clussmanproductions.roadstuffreborn.blocks;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateLampsTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.SafetranType3TileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCrossingGateBase extends Block {
	public BlockCrossingGateBase()
	{
		super(Material.IRON);
		setRegistryName("crossing_gate_base");
		setUnlocalizedName(ModRoadStuffReborn.MODID + ".crossing_gate_base");
		setCreativeTab(ModRoadStuffReborn.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (worldIn.isRemote)
		{
			return;
		}
		
		Boolean powered = worldIn.isBlockPowered(pos);
		BlockPos nextPos = pos.offset(EnumFacing.UP);
		while(true)
		{
			Block block = worldIn.getBlockState(nextPos).getBlock();
			if (block instanceof BlockCrossingGateGate)
			{
				CrossingGateGateTileEntity gateTE = (CrossingGateGateTileEntity)worldIn.getTileEntity(nextPos);
				gateTE.setStatusByIsPowered(powered);
			}
			else if (block instanceof BlockCrossingGateLamps)
			{
				CrossingGateLampsTileEntity lampsTE = (CrossingGateLampsTileEntity)worldIn.getTileEntity(nextPos);
				lampsTE.setFlashOverride(powered);
			}
			else if (block instanceof BlockCrossingGatePole)
			{
				// do nothing
			} 
			else if (block instanceof BlockCrossingGateCrossbuck)
			{
				// do nothing
			}
			else if (block instanceof BlockSafetranType3)
			{
				SafetranType3TileEntity safetranType3 = (SafetranType3TileEntity)worldIn.getTileEntity(nextPos);
				safetranType3.setIsRinging(powered);
			}
			else
			{
				break;
			}
			
			nextPos = nextPos.offset(EnumFacing.UP);
		}
	}
}
